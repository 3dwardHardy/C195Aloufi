package Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FirstLevelDivisions  {
    private int divisionId;

    private String division;

    private LocalDateTime createDate;

    private String createdBy;

    private Timestamp lastUpdate;

    private String lastUpdatedBy;

    private int countryId;

    public FirstLevelDivisions(int divisionId, String division) {
        this.divisionId = divisionId;
        this.division = division;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return (division);
    }
}
