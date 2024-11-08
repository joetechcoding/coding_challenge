package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * @Classname Lead
 * @Description TODO
 * @Author zjj
 */

public class Lead {
    @JsonProperty("_id")
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private ZonedDateTime entryDate;

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public ZonedDateTime getEntryDate() { return entryDate; }
    public void setEntryDate(ZonedDateTime entryDate) { this.entryDate = entryDate; }

    @Override
    public String toString() {
        return "Lead{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", entryDate=" + entryDate +
                '}';
    }
}
