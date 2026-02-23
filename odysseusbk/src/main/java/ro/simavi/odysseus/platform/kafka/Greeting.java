package ro.simavi.odysseus.platform.kafka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greeting {

    private String msg;
    private String name;

    public Greeting() {

    }

    public Greeting(String msg, String name) {
        this.msg = msg;
        this.name = name;
    }

    @Override
    public String toString() {
        return msg + ", " + name + "!";
    }
}