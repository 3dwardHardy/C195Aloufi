package Models;

public class Contacts {
    /**
     * declarations for contact class
     */
    private int contactId;

    private String contactName;

    private String email;

    /**
     * Contact class getters and setters
     * @return
     */
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contacts(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * allows the contact name to be returned as string
     * @return
     */
    @Override
    public String toString() {
        return contactName;
    }
}
