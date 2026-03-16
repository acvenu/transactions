package com.iispl.utility;

import java.util.Scanner;

import com.iispl.service.SettlementBatchServiceDAOImpl;
import com.iispl.service.SettlementReport;

public class UtilityMain {

    static Scanner input = new Scanner(System.in);

    static SettlementBatchServiceDAOImpl service = new SettlementBatchServiceDAOImpl();

    public static void main(String[] args) {

        int choice;
        //hello

        while (true) {

            System.out.println("\n========== Batch Settlement System ==========");
            System.out.println("1. Create Batch");
            System.out.println("2. View Batch Transactions By ID");
            System.out.println("3. View Settlement History");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = input.nextInt();

            switch (choice) {

                case 1:
                    createBatch();
                    break;

                case 2:
                    viewBatchTransactions();
                    break;

                case 3:
                    viewSettlementHistory();
                    break;

                case 4:
                    System.out.println("Exiting system...");
                    System.exit(0);

                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // -----------------------------
    // Create batch
    // -----------------------------
    public static void createBatch() {

        System.out.println("\nStarting batch processing...\n");

        service.createBatch();

        System.out.println("\nBatch processing completed.");
    }

    // -----------------------------
    // View transactions of a batch
    // -----------------------------
    public static void viewBatchTransactions() {

        System.out.print("Enter Batch ID: ");
        String batchId = input.next();

        service.viewBatchTransactions(batchId);
    }

    // -----------------------------
    // View settlement history
    // -----------------------------
    public static void viewSettlementHistory() {

        SettlementReport report = new SettlementReport();

        report.printSettlementHistory();
    }
}