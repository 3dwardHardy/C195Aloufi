package Models;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Appointments {
/**
 * declarations for appointment class
 */

private int apptId;

private String title;

private String description;

private String location;

private String type;

private LocalDateTime startTime;

private LocalDate startDate;

private LocalDateTime endTime;

private LocalDate endDate;

private LocalDateTime createDate;

private String createdBy;

private int customerId;

private int userId;

private int contactId;

private String contactName;


public ObservableList<Appointments> appointments;

public Appointments (int apptId,
                     String title,
                     String description,
                     String location,
                     int contactId,
                     String type,
                     LocalDate startDate,
                     LocalDateTime startTime,
                     LocalDate endDate,
                     LocalDateTime endTime,
                     int customerId,
                     int userId,
                     String contactName) {
    this.apptId = apptId;
    this.title = title;
    this.description = description;
    this.location = location;
    this.contactId = contactId;
    this.type = type;
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
    this.customerId = customerId;
    this.userId = userId;
    this.contactName = contactName;
}

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
    return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ObservableList<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(ObservableList<Appointments> appointments) {
        this.appointments = appointments;
    }
}

