package com.techelevator.utilities;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Audit {

    private File auditFile;  // holds the logFile object
    private PrintWriter writer; // writer instantiation of the PrintWriter class
    private DateTimeFormatter auditFileDateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private String auditEntryTime  = auditFileDateTimeFormatter.format(LocalDateTime.now());

    public Audit(String pathName){  // constructor will set up File object
        this.auditFile = new File(pathName);
        if (this.auditFile.exists()){
            // if this logFile exists we want to append
            try {
                this.writer = new PrintWriter(new FileWriter(this.auditFile, true));
            } catch (IOException e) {
                System.out.println("Process has been interrupted. Please review your audit file settings.");
            }
        }
        else {
            try {
                this.writer = new PrintWriter(this.auditFile);
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please review your audit file settings.");
            }
        }
    }

    public void write(String message) {
        this.writer.println(message);
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }

    public void logEntryToAuditFile(Audit auditFile, BigDecimal addedAmount, BigDecimal machineBalance, String transactionType, String slotId) {
        try {
            String formattedAuditFileEntry = String.format("%-20s %-16s %2s %7s %7s", auditEntryTime, transactionType, slotId, "$" + addedAmount, "$" + machineBalance);
            auditFile.write(formattedAuditFileEntry);
        } catch (Exception e) {
            System.out.println("Logging error.");
        }
    }
}
