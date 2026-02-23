package ro.simavi.odysseus.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ro.simavi.odysseus.platform.entities.OdsDocumentValidationDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BehaviourBiometricsDTO {
    @JsonProperty("isAuthenticated")
    private boolean isAuthenticated;
    @JsonProperty("status")
    private String status;
    @JsonProperty("metadata")
    private Metadata metadata;

    public BehaviourBiometricsDTO() {
            metadata = new Metadata();
    }


    @Getter
    @Setter
    public static class Metadata {
        @JsonProperty("features")
        private List<Feature> features;

        public Metadata() {
            features = new ArrayList<>();
        }
    }

    @Getter
    @Setter
    public static class Feature extends OdsDocumentValidationDTO.Feature {
        @JsonProperty("name")
        private String name;
        @JsonProperty("value")
        private String value;
    }
}
