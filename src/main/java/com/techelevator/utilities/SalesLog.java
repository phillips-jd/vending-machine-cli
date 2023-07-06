package com.techelevator.utilities;

import com.techelevator.models.Item;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesLog {
    private DateTimeFormatter salesLogDateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yy_hh:mm:ssa");
    private String pathName = String.format("%s%s",salesLogDateTimeFormatter.format(LocalDateTime.now()),"_SalesLog.txt");
    private File salesLogFile;
    private static PrintWriter writer;

    public SalesLog(List<Item> itemList, BigDecimal totalSales){
        this.salesLogFile = new File(pathName);
        if (this.salesLogFile.exists()){
            try {
                this.writer = new PrintWriter(new FileWriter(this.salesLogFile, true));
            } catch (IOException e) {
                System.out.println("Process has been interrupted. Please review your audit file settings.");
            }
        }
        else {
            try {
                this.writer = new PrintWriter(this.salesLogFile);
                write("Taste Elevator Sales Report");
                for (Item item : itemList) {
                    try {
                        String formattedEntry = String.format("%s|%s|%s", item.getItemName(), item.getFullPriceSold() , item.getDiscountedSold());
                        write(formattedEntry);
                    } catch (Exception e) {
                        System.out.println("Logging error.");
                    }
                }
                write("TOTAL SALES " + totalSales);
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please review your audit file settings.");
            }
        }
    }

    public static void write(String message) {
        writer.println(message);  // this puts the message in the buffer!
        writer.flush();
    }

    public static void close() throws IOException {
        writer.close();
    }
}
