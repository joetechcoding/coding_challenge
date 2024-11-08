package org.example;

import java.util.List;

/**
 * Adobe Coding Challenge
 *
 */
public class Main
{
    public static void main(String[] args) throws Exception {
        //Step 1: Read Leads from file
        //change your local file path here
        String filePath = "src/main/resources/leads.json";
        List<Lead> originalLeads = LeadProcessor.readLeadsFromFile(filePath);

        //Step 2: Get deduplicated leads then print
        List<Lead> deduplicatedLeads = LeadProcessor.deduplicateLeads(originalLeads);
        System.out.println("Deduplicated Leads:");
        deduplicatedLeads.forEach(System.out::println);

        //Step 3: Print the change logs
        System.out.println("\nChange Logs:");
        LeadProcessor.getChangeLogs().forEach(log -> {
            System.out.println("----------------------------------------");
            System.out.println(log);
        });
    }
}
