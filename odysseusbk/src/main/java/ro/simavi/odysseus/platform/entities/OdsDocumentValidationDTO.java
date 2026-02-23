package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OdsDocumentValidationDTO {
    private String language;
    private DTC dtoVC;
    private DTC dtoPC;
    private DTC emrtd;
    private List<Biometric> biometrics;

    private BehaviourBiometrics behaviourBiometrics;

    private BackgroundChecks backgroundChecks;
    private ValidationStatus validationStatus;
    private Transaction transaction;

    public OdsDocumentValidationDTO() {
        dtoVC = new DTC();
        dtoPC = new DTC();
        emrtd = new DTC();
        biometrics = new ArrayList<>();
        behaviourBiometrics = new BehaviourBiometrics();
        validationStatus = new ValidationStatus();
        transaction= new Transaction();
        backgroundChecks = new BackgroundChecks();
    }

    @Getter
    @Setter
    public static class DTC {
        private String expirationOn;
        private String gender;
        private String dateOfBirth;
        private String givenName;
        private String surname;
        private String mrz;
        private String electronic;
        private String issuedOn;
        private String nationality;
        private String type;
        private String typeId;
        private String typeName;
        private String number;
        private String issuerState;

        @Override
        public String toString() {
            return "{\n" +
                    "    \"expirationOn\": \"" + expirationOn + "\",\n" +
                    "    \"gender\": \"" + gender + "\",\n" +
                    "    \"dateOfBirth\": \"" + dateOfBirth + "\",\n" +
                    "    \"givenName\": \"" + givenName + "\",\n" +
                    "    \"surname\": \"" + surname + "\",\n" +
                    "    \"mrz\": \"" + mrz + "\",\n" +
                    "    \"electronic\": " + (electronic == null ? "null" : "\"" + electronic + "\"") + ",\n" +
                    "    \"issuedOn\": " + (issuedOn == null ? "null" : "\"" + issuedOn + "\"") + ",\n" +
                    "    \"nationality\": \"" + nationality + "\",\n" +
                    "    \"type\": \"" + type + "\",\n" +
                    "    \"typeId\": \"" + typeId + "\",\n" +
                    "    \"typeName\": \"" + typeName + "\",\n" +
                    "    \"number\": \"" + number + "\",\n" +
                    "    \"issuerState\": " + (issuerState == null ? "null" : "\"" + issuerState + "\"") + "\n" +
                    "}";
        }
    }
    @Getter
    @Setter
    public static class Biometric {
        private String position;
        private String type;
        private String source;
        private String image;
        private String format;
    }
    @Getter
    @Setter
    public static class ValidationStatus {
        private boolean documentAuthenticated;
        private boolean livenessCheckPassed;
        private boolean biometryMatched;
    }
    @Getter
    @Setter
    public static class Transaction {
        private String transactionUuid;
        private String transactionStatus;
        private String travelerId;
        private String travelId;
        private String personDecision;
    }
    @Getter
    @Setter
    public class BehaviourBiometrics {
        private boolean isAuthenticated;
        private String status;
        private Metadata metadata;

        public BehaviourBiometrics() {
            metadata = new Metadata();
        }
    }

    @Getter
    @Setter
    public static class Metadata {
        private List<Feature> features;

        public Metadata() {
            features = new ArrayList<>();
        }
    }

    @Getter
    @Setter
    public static class Feature {
        private String name;
        private String value;
    }

    @Getter
    @Setter
    public class BackgroundChecks {
        private ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Person person;
        private List<ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Item> items;
        private List<ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Event> events;

        public BackgroundChecks(){
            person = new ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Person();
            items = new ArrayList<>();
            events = new ArrayList<>();
        }

        @Getter
        @Setter
        public class Person {
            private String givenName;
            private String surname;
            private String gender;
            private String dateOfBirth;
            private String nationality;
        }

        @Getter
        @Setter
        public class Item {
            private String type;
            private ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Item.Description description;

            public Item() {
                this.description = new ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Item.Description();
            }

            @Getter
            @Setter
            public class Description {
                private String issuerState;
                private String issuePlace;
                private String validFor;
                private String type;
                private String typeName;
                private String duration;
                private String numberOfEntries;
                private String number;
                private String validFrom;
                private String validTo;
            }
        }

        @Getter
        @Setter
        public class Event {
            private String type;
            private ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Event.Description description;

            public Event() {
                this.description = new ro.simavi.odysseus.platform.dto.BackgroundChecksDTO.Event.Description();
            }

            @Getter
            @Setter
            public class Description {
                private String category;
                private String alertDescription;
                private String requiredAction;
                private String departureDate;
                private String entryBan;
            }
        }
    }
}
