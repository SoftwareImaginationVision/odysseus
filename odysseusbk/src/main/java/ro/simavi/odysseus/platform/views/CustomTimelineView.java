package ro.simavi.odysseus.platform.views;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import ro.simavi.odysseus.platform.entities.OdsTravel;
import ro.simavi.odysseus.platform.repositories.OdsTravelRepository;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Named("customTimelineView")
@ViewScoped
public class CustomTimelineView implements Serializable {

    private TimelineModel<String, ?> model;
    private LocalDateTime start;
    private LocalDateTime end;

    @Autowired
    private OdsTravelRepository odsTravelRepository;
    @PostConstruct
    public void init() {
        // set initial start / end dates for the axis of the timeline
        start = LocalDateTime.now().minusHours(24);
        end = LocalDateTime.now().plusHours(48);


        List<OdsTravel> travelList = odsTravelRepository.findAll();

        // create timeline model
        model = new TimelineModel<>();

        for (OdsTravel travel : travelList) {
            LocalDate travelStart = travel.getStartDate();
            LocalDate travelEnd = travel.getEndDate();


                System.out.println("Start din init = " + start);
                System.out.println("End din init = " + end);

                String availability = travel.getTravelStatus();

                // create an event with content, start / end dates, editable flag, group name and custom style class
                TimelineEvent event = TimelineEvent.builder()
                        .data(availability)
                        .startDate(travelStart)
                        .endDate(travelEnd)
                        .editable(true)
                        .group(travel.getTravelName())
                        .build();

                model.add(event);


        }
    }

    public TimelineModel<String, ?> getModel() {
        return model;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}