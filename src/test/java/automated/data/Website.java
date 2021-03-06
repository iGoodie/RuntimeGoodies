package automated.data;

import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

public class Website {

    @Goodie
    private String domain;

    @Goodie
    private String extension;

    @Override
    public String toString() {
        return "Website{" +
                "domain='" + domain + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }

}
