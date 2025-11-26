package model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSVDetail {
    @CsvBindByPosition(position = 0)
    private String TITLE;
    @CsvBindByPosition(position = 1)
    private String DOI;

    @CsvBindByPosition(position = 2)
    private String ONLINEISSN;

    @Override
    public String toString() {
        return "CSVDetails{" +
                "TITLE='" + TITLE + '\'' +
                ", DOI='" + DOI + '\'' +
                ", ONLINEISSN='" + ONLINEISSN + '\'' +
                '}';
    }
}
