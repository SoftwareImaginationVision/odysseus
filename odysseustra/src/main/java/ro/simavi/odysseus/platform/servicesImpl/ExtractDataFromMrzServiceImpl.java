package ro.simavi.odysseus.platform.servicesImpl;

import org.springframework.stereotype.Service;
import ro.simavi.odysseus.platform.services.ExtractDataFromMrzService;
import java.util.LinkedHashMap;
import java.util.Map;


@Service
public class ExtractDataFromMrzServiceImpl implements ExtractDataFromMrzService {

    private static final int[] WEIGHTS = {7, 3, 1};
    public Map<String, String> extractFromTd1(String mrz) {
        Map<String, String> data = new LinkedHashMap<>();

        if (mrz == null || mrz.length() != 90) {
            throw new IllegalArgumentException("Invalid TD1 MRZ format");
        }

        String line1 = mrz.substring(0, 30);
        String line2 = mrz.substring(30, 60);
        String line3 = mrz.substring(60, 90);

        // Linia 1
        data.put("DocumentType", line1.substring(0, 1));
        data.put("IssuingCountry", line1.substring(2, 5));
        data.put("DocumentNumber", line1.substring(5, 14));
        data.put("CheckDigitForDocumentNumber", line1.substring(14, 15));

        // Linia 2
        data.put("DateOfBirth", line2.substring(0, 6));
        data.put("CheckDigitOnDateOfBirth", line2.substring(6,7));
        data.put("Sex", line2.substring(7,8));
        data.put("ExpiryDate", line2.substring(8, 14));
        data.put("CheckDigitOnDateOfExpiry", line2.substring(14, 15));
        data.put("CheckDigitForUpperAndMiddleMachine", line2.substring(29, 30));

        // Linia 3
        String[] nameParts = line3.split("<<");
        data.put("LastName", nameParts[0].replace("<", " "));
        data.put("FirstName", nameParts[1].replace("<", " "));

        validateCheckDigit(data.get("DocumentNumber"), data.get("CheckDigitForDocumentNumber"));
        validateCheckDigit(data.get("DateOfBirth"), data.get("CheckDigitOnDateOfBirth"));
        validateCheckDigit(data.get("ExpiryDate"), data.get("CheckDigitOnDateOfExpiry"));


        return data;
    }


    public Map<String, String> extractFromTd2(String mrz) {
        Map<String, String> data = new LinkedHashMap<>();
        if (mrz == null || mrz.length() != 72) { // TD2 2 rows, 1 row -> 36 characters
            throw new IllegalArgumentException("Invalid TD2 MRZ format");
        }

        String line1 = mrz.substring(0, 36);
        String line2 = mrz.substring(36, 72);

        data.put("DocumentType", line1.substring(0, 1));
        data.put("IssuingCountry", line1.substring(2, 5));
        data.put("LastName", line1.substring(5).split("<<")[0]);
        data.put("FirstName", line1.substring(5).split("<<")[1].replace("<", " "));
        data.put("DocumentNumber", line2.substring(0, 9).replace("<", ""));
        data.put("CheckDigitForDocumentNumber", line2.substring(9, 10));
        data.put("DateOfBirth", line2.substring(13, 19));
        data.put("CheckDigitOnDateOfBirth", line2.substring(19, 20));
        data.put("Sex", line2.substring(20, 21));
        data.put("ExpiryDate", line2.substring(21, 27));
        data.put("CheckDigitOnDateOfExpiry", line2.substring(27, 28));

        validateCheckDigit(data.get("DocumentNumber"), data.get("CheckDigitForDocumentNumber"));
        validateCheckDigit(data.get("DateOfBirth"), data.get("CheckDigitOnDateOfBirth"));
        validateCheckDigit(data.get("ExpiryDate"), data.get("CheckDigitOnDateOfExpiry"));


        return data;
    }

    public Map<String, String> extractFromTd3(String mrz) {
        Map<String, String> data = new LinkedHashMap<>();
        if (mrz == null || mrz.length() != 88) { // TD3 2 rows, 1 row -> 44 characters
            throw new IllegalArgumentException("Invalid TD3 MRZ format");
        }

        String line1 = mrz.substring(0, 44);
        String line2 = mrz.substring(44, 88);

        data.put("DocumentType", line1.substring(0, 1));
        data.put("IssuingCountry", line1.substring(2, 5));
        data.put("LastName", line1.substring(5).split("<<")[0]);
        data.put("FirstName", line1.substring(5).split("<<")[1].replace("<", " "));
        data.put("DocumentNumber", line2.substring(0, 9).replace("<", ""));
        data.put("CheckDigitForDocumentNumber", line2.substring(9, 10));
        data.put("Nationality", line2.substring(10, 13));
        data.put("DateOfBirth", line2.substring(13, 19));
        data.put("CheckDigitOnDateOfBirth", line2.substring(19, 20));
        data.put("Sex", line2.substring(20, 21));
        data.put("ExpiryDate", line2.substring(21, 27));
        data.put("CheckDigitOnDateOfExpiry", line2.substring(27, 28));

        validateCheckDigit(data.get("DocumentNumber"), data.get("CheckDigitForDocumentNumber"));
        validateCheckDigit(data.get("DateOfBirth"), data.get("CheckDigitOnDateOfBirth"));
        validateCheckDigit(data.get("ExpiryDate"), data.get("CheckDigitOnDateOfExpiry"));


        return data;
    }

    private void validateCheckDigit(String value, String checkDigit) {
        if (value == null || checkDigit == null) {

            throw new IllegalArgumentException("Value or check digit is null");
        }

        int calculatedCheckDigit = calculateCheckDigit(value);
        int providedCheckDigit = Character.getNumericValue(checkDigit.charAt(0));
        System.out.println("calculatedCheckDigit: " + calculatedCheckDigit + "\t" + "providedCheckDigit: " + providedCheckDigit);

        if (calculatedCheckDigit != providedCheckDigit) {
            throw new IllegalArgumentException("Invalid check digit for value: " + value);
        }
    }

    private int calculateCheckDigit(String value) {
        int sum = 0;
        for (int i = 0; i < value.length(); i++) {
            int weight = WEIGHTS[i % WEIGHTS.length];
            sum += getCharacterValue(value.charAt(i)) * weight;
        }
        return sum % 10;
    }

    private int getCharacterValue(char c) {
        if (Character.isDigit(c)) {
            return Character.getNumericValue(c);
        } else if (Character.isUpperCase(c)) {
            return c - 'A' + 10; // A=10, B=11, ..., Z=35
        } else if (c == '<') {
            return 0; // Padding character in MRZ
        } else {
            throw new IllegalArgumentException("Invalid character in MRZ: " + c);
        }
    }
}

