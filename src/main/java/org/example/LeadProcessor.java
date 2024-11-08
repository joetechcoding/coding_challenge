package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @Classname LeadProcessor
 * @Description Processing Lead
 * @Author zjj
 */
public class LeadProcessor {
    private static List<ChangeLog> changeLogs = new ArrayList<>();

    /**
     * Get the change logs from the last deduplication operation
     */
    public static List<ChangeLog> getChangeLogs() {
        return changeLogs;
    }

    /**
     * Clear existing change logs
     */
    public static void clearChangeLogs() {
        changeLogs.clear();
    }

    /**
     * Given file path string, get list of Leads
     * @param filePath
     * @return List<Lead>
     * @throws Exception
     */
    public static List<Lead> readLeadsFromFile(String filePath) throws Exception {
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) {
            throw new FileNotFoundException("File not found at: " + filePath);
        }

        ObjectMapper mapper = new ObjectMapper();

        // Register JavaTimeModule to handle ZonedDateTime and other Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());

        // Deserialize into LeadWrapper instead of List<Lead>
        LeadWrapper leadWrapper = mapper.readValue(new File(filePath), LeadWrapper.class);

        // Return the list of leads from the wrapper
        return leadWrapper.getLeads();
    }

    public static List<Lead> deduplicateLeads(List<Lead> leads) {
        // Clear previous change logs
        clearChangeLogs();

        Map<String, Lead> finalUniqueLeads = new HashMap<>();

        // Process leads in reverse order
        for (int i = leads.size() - 1; i >= 0; i--) {
            Lead currentLead = leads.get(i);

            Lead existingLeadById = finalUniqueLeads.get(currentLead.getId());
            Lead existingLeadByEmail = findLeadByEmail(finalUniqueLeads.values(), currentLead.getEmail());

            if (existingLeadById == null && existingLeadByEmail == null) {
                // Case 1: No duplicates found - add the lead
                finalUniqueLeads.put(currentLead.getId(), currentLead);
            } else if (existingLeadById != null) {
                // Case 2: Duplicate ID found
                handleDuplicateId(currentLead, existingLeadById, finalUniqueLeads);
            } else {
                // Case 3: Duplicate email found (but different ID)
                handleDuplicateEmail(currentLead, existingLeadByEmail, finalUniqueLeads);
            }
        }

        return new ArrayList<>(finalUniqueLeads.values());
    }

    private static void handleDuplicateId(Lead currentLead, Lead existingLead, Map<String, Lead> finalUniqueLeads) {
        if (shouldReplace(currentLead, existingLead)) {
            // Log changes before replacing
            createChangeLog(existingLead, currentLead, "UPDATED");
            finalUniqueLeads.put(currentLead.getId(), currentLead);
        } else {
            // Log dropped record
            createChangeLog(currentLead, existingLead, "DROPPED");
        }
    }

    private static void handleDuplicateEmail(Lead currentLead, Lead existingLead, Map<String, Lead> finalUniqueLeads) {
        if (shouldReplace(currentLead, existingLead)) {
            // Log changes before replacing
            createChangeLog(existingLead, currentLead, "UPDATED");
            finalUniqueLeads.remove(existingLead.getId());
            finalUniqueLeads.put(currentLead.getId(), currentLead);
        } else {
            // Log dropped record
            createChangeLog(currentLead, existingLead, "DROPPED");
        }
    }

    private static void createChangeLog(Lead sourceLead, Lead resultLead, String changeType) {
        ChangeLog log = new ChangeLog(sourceLead, resultLead, changeType);

        // Compare all fields and log changes
        log.addFieldChange("id", sourceLead.getId(), resultLead.getId());
        log.addFieldChange("email", sourceLead.getEmail(), resultLead.getEmail());
        log.addFieldChange("firstName", sourceLead.getFirstName(), resultLead.getFirstName());
        log.addFieldChange("lastName", sourceLead.getLastName(), resultLead.getLastName());
        log.addFieldChange("address", sourceLead.getAddress(), resultLead.getAddress());
        log.addFieldChange("entryDate",
                sourceLead.getEntryDate().toString(),
                resultLead.getEntryDate().toString());

        changeLogs.add(log);
    }

    /**
     * Helper method to find a lead by email in a collection
     * @param leads
     * @param email
     * @return
     */
    private static Lead findLeadByEmail(Collection<Lead> leads, String email) {
        return leads.stream()
                .filter(lead -> lead.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Determines if newLead should replace existingLead based on dates
     * @param newLead
     * @param existingLead
     * @return
     */
    private static boolean shouldReplace(Lead newLead, Lead existingLead) {
        ZonedDateTime newDate = newLead.getEntryDate();
        ZonedDateTime existingDate = existingLead.getEntryDate();

        // If dates are different, prefer newer date
        if (!newDate.isEqual(existingDate)) {
            return newDate.isAfter(existingDate);
        }

        // For equal dates, we're processing in reverse order,
        // so the existing lead is already the "later" one in the list
        return false;
    }
}
