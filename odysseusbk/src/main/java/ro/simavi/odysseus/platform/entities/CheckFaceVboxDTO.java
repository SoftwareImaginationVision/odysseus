package ro.simavi.odysseus.platform.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CheckFaceVboxDTO {

    @JsonProperty("language")
    private String language;

    @JsonProperty("document")
    private Document document;

    @JsonProperty("boardingPass")
    private BoardingPass boardingPass;

    @JsonProperty("biometrics")
    private List<Biometric> biometrics;

    @JsonProperty("validationStatus")
    private ValidationStatus validationStatus;

    @Getter
    @Setter
    public static class Document {
        @JsonProperty("expirationOn")
        private LocalDateTime expirationOn;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("dateOfBirth")
        private LocalDateTime dateOfBirth;

        @JsonProperty("givenName")
        private String givenName;

        @JsonProperty("surname")
        private String surname;

        @JsonProperty("mrz")
        private String mrz;

        @JsonProperty("electronic")
        private String electronic;

        @JsonProperty("issuedOn")
        private String issuedOn;

        @JsonProperty("nationality")
        private String nationality;

        @JsonProperty("type")
        private String type;

        @JsonProperty("typeId")
        private String typeId;

        @JsonProperty("typeName")
        private String typeName;

        @JsonProperty("number")
        private String number;

        @JsonProperty("issuerState")
        private String issuerState;

    }
    @Getter
    @Setter
    public static class BoardingPass {
        @JsonProperty("formatCode")
        private String formatCode;

        @JsonProperty("type")
        private String type;

        @JsonProperty("numberOfLegsEncoded")
        private int numberOfLegsEncoded;

        @JsonProperty("passengerName")
        private String passengerName;

        @JsonProperty("electronicTicketIndicator")
        private String electronicTicketIndicator;

        @JsonProperty("raw")
        private String raw;

        @JsonProperty("expirationOn")
        private LocalDateTime expirationOn;

        @JsonProperty("legs")
        private List<Leg> legs;

        @Getter
        @Setter
        public static class Leg {
            @JsonProperty("flightNumber")
            private String flightNumber;

            @JsonProperty("frequentFlyerNumber")
            private String frequentFlyerNumber;

            @JsonProperty("documentSerialNumber")
            private String documentSerialNumber;

            @JsonProperty("airlineNumericCode")
            private String airlineNumericCode;

            @JsonProperty("passengerStatus")
            private String passengerStatus;

            @JsonProperty("sequenceNumber")
            private String sequenceNumber;

            @JsonProperty("seatNumber")
            private String seatNumber;

            @JsonProperty("compartmentCode")
            private String compartmentCode;

            @JsonProperty("flightDate")
            private String flightDate;

            @JsonProperty("legOrder")
            private int legOrder;

            @JsonProperty("carrierDesignator")
            private String carrierDesignator;

            @JsonProperty("destinationAirport")
            private String destinationAirport;

            @JsonProperty("originAirport")
            private String originAirport;

            @JsonProperty("carrierPnrCode")
            private String carrierPnrCode;

            @JsonProperty("frequentFlyerAirlineDesignator")
            private String frequentFlyerAirlineDesignator;

        }
    }
    @Getter
    @Setter
    public static class Biometric {
        @JsonProperty("position")
        private String position;

        @JsonProperty("type")
        private String type;

        @JsonProperty("source")
        private String source;

        @JsonProperty("image")
        private String image;

        @JsonProperty("format")
        private String format;

    }
    @Getter
    @Setter
    public static class ValidationStatus {
        @JsonProperty("documentAuthenticated")
        private boolean documentAuthenticated;

        @JsonProperty("livenessCheckPassed")
        private boolean livenessCheckPassed;

        @JsonProperty("biometryMatched")
        private boolean biometryMatched;

    }
}
