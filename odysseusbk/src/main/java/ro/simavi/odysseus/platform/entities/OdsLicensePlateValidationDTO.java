package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;
import ro.simavi.odysseus.platform.dto.PassengersDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OdsLicensePlateValidationDTO {
    // Main class fields
    private PlateRecognition plateRecognition;
    private List<PassengersDTO> passengers;
    private CarTransaction carTransaction;

    // PlateRecognition class
    @Getter
    @Setter
    public static class PlateRecognition {
        private String carColor;
        private String colorConfidence;
        private String colorReasoning;
        private String carType;
        private String typeConfidence;
        private String typeReasoning;
        private String licensePlate;
        private String licensePlateConfidence;
        private String licensePlateImage;
        private String xaiLicensePlateImage;
        private String peopleCounting;
        private List<String> peopleCountingConfidence;
        private String peopleCountingImage;
        private String xaiPeopleCountingImage;
        private String datetime;
    }

    // CarTransaction class
    @Getter
    @Setter
    public static class CarTransaction {
        private String transactionUuid;
        private String transactionStatus;
        private String licensePlate;
        private String licensePlateConfidence;
        private String personDecision;
    }

    public OdsLicensePlateValidationDTO() {
        plateRecognition = new PlateRecognition();
        carTransaction = new CarTransaction();
        passengers = new ArrayList<>();
    }
}
