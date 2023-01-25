package BankingSystem;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static String logo = "   ___            __    _             ____         __          \n" +
            "  / _ )___ ____  / /__ (_)__  ___ _  / __/_ _____ / /____ __ _ \n" +
            " / _  / _ `/ _ \\/  '_// / _ \\/ _ `/ _\\ \\/ // (_-</ __/ -_)  ' \\\n" +
            "/____/\\_,_/_//_/_/\\_\\/_/_//_/\\_, / /___/\\_, /___/\\__/\\__/_/_/_/\n" +
            "                            /___/      /___/                   \n" +
            "_______________________________________________________________";
    static final Scanner scanner = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.00");
    static Random rand = new Random();
    static ArrayList<Bank> banks = new ArrayList<>();
    static BankDB bankDataBase = new BankDB();

    public static void main(String[] args) throws IOException{
        bankDataBase.createTables();
        banks = bankDataBase.selectBanks();
        menu();
    }

    private static void menu() throws IOException{
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Bank menu\n" +
                        "2. Client menu\n" +
                        "0. Exit\n");
        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                scanner.next();
                waitForUser();
                menu();
            }
        }
        switch (choice) {
            case 1 -> bankMenu();
            case 2 -> clientMenu();
            case 0 -> {
                clearScreen();
                System.exit(1);
            }
            default -> {
                System.out.println("Wrong choice, please try again");
                waitForUser();
                menu();
            }
        }
    }

    static void bankMenu() throws IOException{
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Log in to the Bank\n" +
                        "2. Create a new Bank\n" +
                        "3. Back\n" +
                        "0. Exit\n");
        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                scanner.next();
                waitForUser();
                bankMenu();
            }
        }

        switch (choice) {
            case 1 -> {

                if (banks.size() == 0) {
                    clearScreen();
                    System.out.println(logo);
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    bankMenu();
                }
                int choiceBank;
                while (true) {
                    clearScreen();
                    System.out.println(logo);
                    System.out.println("\nSelect a bank: ");
                    for (int counter = 0; counter < banks.size(); counter++) {
                        System.out.println(counter + 1 + ". " + banks.get(counter).getBankName());
                    }
                    try {
                        System.out.print("\nchoice: ");
                        choiceBank = scanner.nextInt();
                        banks.get(choiceBank - 1);
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        scanner.next();
                        waitForUser();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Wrong choice, please try again");
                        waitForUser();
                    }
                }
                clearScreen();
                System.out.println(logo);
                if (banks.get(choiceBank - 1).logInToBank()) {
                    banks.get(choiceBank - 1).bankDashboard();
                } else {
                    bankMenu();
                }
            }
            case 2 -> {
                Bank.creatingBankMenu();
                waitForUser();
                bankMenu();
            }
            case 3 -> menu();
            case 0 -> System.exit(1);
            default -> {
                System.out.println("Wrong choice, please try again");
                waitForUser();
                bankMenu();
            }
        }
    }

    static void clientMenu() throws IOException{
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Log in to the Bank\n" +
                        "2. Become a Bank customer\n" +
                        "3. Back\n" +
                        "0. Exit\n");
        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                scanner.next();
                waitForUser();
                clientMenu();
            }
        }
        switch (choice) {
            case 1 -> {
                if (banks.size() == 0) {
                    clearScreen();
                    System.out.println(logo);
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    clientMenu();
                }
                int choiceBank;
                while (true) {
                    clearScreen();
                    System.out.println(logo);
                    System.out.println("\nSelect your bank: ");
                    for (int counter = 0; counter < banks.size(); counter++) {
                        System.out.println(counter + 1 + ". " + banks.get(counter).getBankName());
                    }
                    try {
                        System.out.print("\nchoice: ");
                        choiceBank = scanner.nextInt();
                        banks.get(choiceBank - 1);
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        scanner.next();
                        waitForUser();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Wrong choice, please try again");
                        waitForUser();
                    }
                }
                int inClientID;
                while (true) {
                    clearScreen();
                    System.out.println(logo);
                    System.out.print("\nEnter client ID: ");
                    try {
                        inClientID = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        scanner.next();
                        waitForUser();
                    }
                }
                for (Client acc : banks.get(choiceBank - 1).clients) {
                    if (inClientID == acc.clientID) {
                        if (acc.logInToClient()) {
                            acc.clientDashboard();
                        } else {
                            clientMenu();
                        }
                    }
                }
                System.out.println("There is no customer with this ID in this bank");
                waitForUser();
                clientMenu();
            }
            case 2 -> {
                Client.creatingClientMenu();
                waitForUser();
                clientMenu();
            }
            case 3 -> menu();
            case 0 -> System.exit(1);
            default -> {
                System.out.println("Wrong choice, please try again");
                waitForUser();
                clientMenu();
            }
        }
    }

    public static void waitForUser() throws IOException {
        System.out.println("\npress any key to continue...");
        System.in.read();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}