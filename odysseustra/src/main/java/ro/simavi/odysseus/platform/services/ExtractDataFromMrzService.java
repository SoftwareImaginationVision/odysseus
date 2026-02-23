package ro.simavi.odysseus.platform.services;

import java.util.Map;

public interface ExtractDataFromMrzService {

    //
    public Map<String, String> extractFromTd1(String mrz);
    public Map<String, String> extractFromTd2(String mrz);
    public Map<String, String> extractFromTd3(String mrz);
}
