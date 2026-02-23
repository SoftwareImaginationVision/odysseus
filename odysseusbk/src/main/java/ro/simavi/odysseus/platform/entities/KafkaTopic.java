package ro.simavi.odysseus.platform.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaTopic {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
