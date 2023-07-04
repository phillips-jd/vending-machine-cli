package com.techelevator.models;

import java.io.*;

public class Audit {

    private File auditFile;  // holds the logFile object
    private PrintWriter writer; // writer instantiation of the PrintWriter class

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
        this.writer.println(message);  // this puts the message in the buffer!
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }

}
