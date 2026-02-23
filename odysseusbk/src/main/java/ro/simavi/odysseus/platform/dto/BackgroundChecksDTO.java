package ro.simavi.odysseus.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BackgroundChecksDTO {
    @JsonProperty("person")
    private Person person;

    @JsonProperty("items")
    private List<Item> items;

    @JsonProperty("events")
    private List<Event> events;

    public BackgroundChecksDTO(){
        person = new Person();
        items = new ArrayList<>();
        events = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Person {
        @JsonProperty("givenName")
        private String givenName;

        @JsonProperty("surname")
        private String surname;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("dateOfBirth")
        private String dateOfBirth;

        @JsonProperty("nationality")
        private String nationality;
    }

    @Getter
    @Setter
    public static class Item {
        @JsonProperty("type")
        private String type;

        @JsonProperty("description")
        private Description description;

        public Item() {
            this.description = new Description();
        }

        @Getter
        @Setter
        public static class Description {
            @JsonProperty("issuerState")
            private String issuerState;

            @JsonProperty("issuePlace")
            private String issuePlace;

            @JsonProperty("validFor")
            private String validFor;

            @JsonProperty("type")
            private String type;

            @JsonProperty("typeName")
            private String typeName;

            @JsonProperty("duration")
            private String duration;

            @JsonProperty("numberOfEntries")
            private String numberOfEntries;

            @JsonProperty("number")
            private String number;

            @JsonProperty("validFrom")
            private String validFrom;

            @JsonProperty("validTo")
            private String validTo;
        }
    }

    @Getter
    @Setter
    public static class Event {
        @JsonProperty("type")
        private String type;

        @JsonProperty("description")
        private Description description;

        public Event() {
            this.description = new Description();
        }

        @Getter
        @Setter
        public static class Description {
            @JsonProperty("category")
            private String category;

            @JsonProperty("alertDescription")
            private String alertDescription;

            @JsonProperty("requiredAction")
            private String requiredAction;

            @JsonProperty("departureDate")
            private String departureDate;

            @JsonProperty("entryBan")
            private String entryBan;
        }
    }
}
