package ro.simavi.odysseus.platform.util;

import com.github.jaiimageio.jpeg2000.impl.J2KImageReader;
import com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConversionJp2ToJpeg {
    public byte[] convertJP2ToJPEG(byte[] jp2Bytes) throws IOException {
        ImageInputStream inputStream = null;
        ImageOutputStream outputStream = null;
        ImageReader reader = null;
        ImageWriter writer = null;

        try {
            // Create input stream
            inputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(jp2Bytes));

            // Create JPEG2000 reader
            reader = new J2KImageReader(new J2KImageReaderSpi());
            reader.setInput(inputStream, true, true);

            // Read image parameters
            ImageReadParam readParam = reader.getDefaultReadParam();

            // Read the image
            BufferedImage image = reader.read(0, readParam);

            if (image == null) {
                throw new IOException("Failed to read JPEG2000 image: Image is null");
            }

            // Prepare output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            outputStream = ImageIO.createImageOutputStream(baos);

            // Get JPEG writer
            writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            writer.setOutput(outputStream);

            // Write the image
            writer.write(new IIOImage(image, null, null));

            // Flush and close
            outputStream.flush();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new IOException("Conversion from JPEG2000 to JPEG failed: " + e.getMessage(), e);
        } finally {
            // Cleanup resources
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Log or ignore
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Log or ignore
                }
            }
            if (reader != null) {
                reader.dispose();
            }
            if (writer != null) {
                writer.dispose();
            }
        }
    }
    public String getImageContentType(byte[] image) {
        // Check or infer the content type based on image data or format
        if (image != null && image.length > 1) {
            byte firstByte = image[0];
            byte secondByte = image[1];

            // Check JPEG signature (FFD8)
            if (firstByte == (byte) 0xFF && secondByte == (byte) 0xD8) {
                return "image/jpeg";  // JPEG
            }

            // Check JPEG 2000 signature (0000000C6A502020)
            if (image.length > 7 &&
                    image[0] == 0x00 && image[1] == 0x00 && image[2] == 0x00 && image[3] == 0x0C &&
                    image[4] == 0x6A && image[5] == 0x50 && image[6] == 0x20 && image[7] == 0x20) {
                return "image/jp2";  // JPEG 2000
            }
        }

        // Default if format is not detected or unknown (although not expected with this logic)
        return "image/jpeg";
    }

}
