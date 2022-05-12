package Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Countries {
    private int countryId;

    private String country;

    private LocalDateTime createdDate;

    private String createdBy;

    private Timestamp lastUpdate;

    private String lastUpdatedby;



    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedby() {
        return lastUpdatedby;
    }

    public void setLastUpdatedby(String lastUpdatedby) {
        this.lastUpdatedby = lastUpdatedby;
    }

    public Countries(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    @Override
    public String toString() {
        return (country);
    }

}
