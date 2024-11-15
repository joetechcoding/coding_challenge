# Lead Deduplication Program

## Description
This project takes a list of JSON records and deduplicates them based on specific rules:
1. The newest data is preferred.
2. Duplicate IDs and emails are treated as duplicates.
3. If dates are identical, the last record in the list is preferred.

## Prerequisites
- **Java SDK**: 17 (Make sure Java 17 is installed on your system)
- **Maven**: Ensure Maven is installed and added to your system's PATH.

## How to Run
1. Ensure you have Java version
2. Use Maven to build the project. 
3. Run the main class. 
4. The program uses a default sample JSON (`leads.json`) located in the `src/main/resources/` directory.

## Dependencies
- SLF4J for logging.
- Jackson for JSON parsing.

## Example Input/Output
Input: leads.json (sample file)
Output: Deduplicated leads with change logs(5 records after)
