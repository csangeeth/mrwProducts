package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MRWProduct {
    private String NAME;
    private String DOI;
    private String URL;

    @Override
    public String toString() {
        return "MRWProduct{" +
                "NAME='" + NAME + '\'' +
                ", DOI='" + DOI + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
