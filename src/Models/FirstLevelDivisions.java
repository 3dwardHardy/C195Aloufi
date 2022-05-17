package Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FirstLevelDivisions  {
    /**
     * declarations for FirstLevelDivisions class
     */
    private int divisionId;

    private String division;

    private LocalDateTime createDate;

    private String createdBy;

    private Timestamp lastUpdate;

    private String lastUpdatedBy;

    private int countryId;

    /**
     * Constructors for first level divisions
     * @param divisionId
     * @param division
     */
    public FirstLevelDivisions(int divisionId, String division) {
        this.divisionId = divisionId;
        this.division = division;
    }

    /**
     * getter's and setter's for first level division class.
     * @return
     */
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

    /**
     * allows division to be returned as a string
     * @return
     */
    @Override
    public String toString() {
        return (division);
    }
}
