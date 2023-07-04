package com.techelevator.models;

import java.io.*;

public class SalesLog {
    private String pathName;
    private File logFile;  // holds the logFile object
    private PrintWriter writer; // writer instantiation of the PrintWriter class

    public SalesLog(String pathName){  // constructor will set up File object
        this.logFile = new File(pathName);
        if (this.logFile.exists()){
            // if this logFile exists we want to append
            try {
                this.writer = new PrintWriter(new FileWriter(this.logFile, true));
            } catch (IOException e) {
                System.out.println("Process has been interrupted. Please review your audit file settings.");
            }
        }
        else {
            try {
                this.writer = new PrintWriter(this.logFile);
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please review your audit file settings.");
            }
        }
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void write(String message) {
        this.writer.println(message);  // this puts the message in the buffer!
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }


}
