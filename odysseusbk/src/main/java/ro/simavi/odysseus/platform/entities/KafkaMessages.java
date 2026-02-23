package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaMessages {
    private String message;

    @Override
    public String toString() {
        return message;
    }
}
