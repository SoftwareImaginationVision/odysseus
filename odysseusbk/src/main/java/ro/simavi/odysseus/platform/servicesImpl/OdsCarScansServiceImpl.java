package ro.simavi.odysseus.platform.servicesImpl;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.simavi.odysseus.platform.entities.OdsCarScans;
import ro.simavi.odysseus.platform.repositories.OdsCarScansRepository;
import ro.simavi.odysseus.platform.services.OdsCarScansService;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class OdsCarScansServiceImpl implements OdsCarScansService {

    private final OdsCarScansRepository odsCarScansRepository;

    @Autowired
    public OdsCarScansServiceImpl(OdsCarScansRepository odsCarScansRepository) {
        this.odsCarScansRepository = odsCarScansRepository;
    }

    @Override
    public void processUffFile(MultipartFile uffFile) throws IOException, ImageReadException {
        File tempFile = File.createTempFile("uff", ".tmp");
        uffFile.transferTo(tempFile);

        Map<String, Object> data = extractDataFromUff(tempFile);

        Map<String, byte[]> images = (Map<String, byte[]>) data.get("images");
        Map<String, String> xmlData = (Map<String, String>) data.get("xmlData");

        if (!images.containsKey("hemd1") || !images.containsKey("xray1") || !images.containsKey("xray2")) {
            throw new IllegalArgumentException("The UFF file doesn't contain 3 pictures");
        }

        String occupancyRectanglesJson = xmlData.get("OccupancyRectangles");
        if (occupancyRectanglesJson != null) {
            images.put("xray1", convertToByteArray(drawRectanglesOnImage(images.get("xray1"), occupancyRectanglesJson)));
        }

        OdsCarScans odsCarScans = new OdsCarScans();
        odsCarScans.setPicture1(images.get("hemd1"));
        odsCarScans.setPicture2(images.get("xray1"));
        odsCarScans.setPicture3(images.get("xray2"));

        for(Map.Entry<String, String> m: xmlData.entrySet()) {
            System.out.print(m.getKey() + ":");
            System.out.println(m.getValue());
        }

        String occupancyCountStr = xmlData.get("OccupancyCount");
        if (occupancyCountStr != null) {
            odsCarScans.setNumberOfPassengers(Integer.parseInt(occupancyCountStr));
        } else {
            throw new IllegalArgumentException("OccupancyCount is missing in the XML data");
        }

        odsCarScansRepository.save(odsCarScans);
    }

    public BufferedImage drawRectanglesOnImage(byte[] imageData, String occupancyRectanglesJson) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

        List<Rectangle> rectangles = parseOccupancyRectangles(occupancyRectanglesJson);

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));


        for (Rectangle rect : rectangles) {
            g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

        g2d.dispose();

        return image;
    }

    private List<Rectangle> parseOccupancyRectangles(String json) {
        List<Rectangle> rectangles = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            int left = obj.getInt("L");
            int top = obj.getInt("T");
            int right = obj.getInt("R");
            int bottom = obj.getInt("B");
            int width = right - left;
            int height = bottom - top;

            rectangles.add(new Rectangle(left, top, width, height));
        }
        return rectangles;
    }


    @Override
    public List<OdsCarScans> getAllCarScans() {
        return odsCarScansRepository.findAll();
    }


    private Map<String, Object> extractDataFromUff(File uffFile) throws IOException, ImageReadException {
        Map<String, byte[]> images = new HashMap<>();
        Map<String, String> xmlData = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(uffFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String normalizedName = entry.getName().replace("\\", "/");

                if (normalizedName.startsWith("event") && (normalizedName.endsWith(".tif") || normalizedName.endsWith(".png"))) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }

                    byte[] imageData = baos.toByteArray();

                    if (normalizedName.contains("hemd1.png")) {
                        images.put("hemd1", convertToJpeg(imageData));
                    } else if (normalizedName.contains("xray1.tif")) {
                        images.put("xray1", convertToJpeg(imageData));
                    } else if (normalizedName.contains("xray2.tif")) {
                        images.put("xray2", convertToJpeg(imageData));
                    }
                } else if (normalizedName.endsWith(".xml")) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }

                    String xmlContent = baos.toString("UTF-8");
                    parseXmlItems(xmlContent, xmlData);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("images", images);
        result.put("xmlData", xmlData);

        return result;
    }
    private void parseXmlItems(String xmlContent, Map<String, String> items) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
            Document doc = builder.parse(is);

            NodeList itemNodes = doc.getElementsByTagName("Item");

            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    String key = itemElement.getElementsByTagName("Key").item(0).getTextContent();
                    String value = itemElement.getElementsByTagName("Value").item(0).getTextContent();

                    items.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing XML: " + e.getMessage());
        }
    }



    private byte[] convertToJpeg(byte[] tiffData) throws IOException, ImageReadException {

        ByteArrayInputStream tiffInputStream = new ByteArrayInputStream(tiffData);
        BufferedImage tiffImage = Imaging.getBufferedImage(tiffInputStream);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(tiffImage, "jpeg", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    private byte[] convertToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        return baos.toByteArray();
    }

}
