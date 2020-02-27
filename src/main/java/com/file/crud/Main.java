package com.file.crud;

import com.file.crud.exception.ServiceException;
import com.file.crud.service.OperationService;
import com.file.crud.service.OperationServiceImpl;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ServiceException {

        String databaseFile = "database.json";
        String currentPath = System.getProperty("user.dir");
        String absolutePath = currentPath + File.separator + databaseFile;

        OperationService operationService = new OperationServiceImpl(absolutePath);
//        operationService.createData("k2", "1",100);
//        operationService.createData("k4", "1",100);
//        operationService.createData("k5", "1",100);
//        operationService.createData("k6", "1",100);
//
//
        int choice;
        String flush;
        System.out.println("*** Welcome to File Database ***");
        do {
            System.out.println("# Choose your option #");
            System.out.println("1. CREATE \n2. READ \n3. DELETE");
            choice = scanner.nextInt();
            flush = scanner.nextLine();
            switch (choice) {
                case 1:
                    createData(operationService);
                    break;
                case 2:
                    readData(operationService);
                    break;
                case 3:
                    deleteData(operationService);
                    break;
                default:
                    System.out.println("Thank You!");
                    System.exit(0);
            }
        } while (true);


    }

    private static void createData(OperationService operationService) {
        System.out.println("Enter Key: ");
        String key = scanner.nextLine();
        System.out.println("Enter Value: ");
        String value = scanner.nextLine();
        System.out.println("Enter Time To Live(Optional): ");
        String timeToLive = scanner.nextLine();
        System.out.println(key + value + timeToLive);
        try {
            if (timeToLive.length() != 0) {
                Integer time = Integer.valueOf(timeToLive);
                operationService.createData(key, value, time);
            } else {
                operationService.createData(key, value);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void readData(OperationService operationService) {
        System.out.println("Enter Key: ");
        String key = scanner.nextLine();
        try {
            operationService.readData(key);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void deleteData(OperationService operationService) {
        System.out.println("Enter Key: ");
        String key = scanner.nextLine();
        try {
            operationService.deleteData(key);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}