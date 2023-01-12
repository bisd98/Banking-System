package Banking_System;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static String logo = "   ___            __    _             ____         __          \n" +
            "  / _ )___ ____  / /__ (_)__  ___ _  / __/_ _____ / /____ __ _ \n" +
            " / _  / _ `/ _ \\/  '_// / _ \\/ _ `/ _\\ \\/ // (_-</ __/ -_)  ' \\\n" +
            "/____/\\_,_/_//_/_/\\_\\/_/_//_/\\_, / /___/\\_, /___/\\__/\\__/_/_/_/\n" +
            "                            /___/      /___/                   \n" +
            "_______________________________________________________________";
    private static final Scanner scanner = new Scanner(System.in);

    static ArrayList<Bank> banks = new ArrayList<>();
    public static void main(String[] args) throws IOException {


        menu();

    }

    private static void menu() throws IOException {
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                "1. Banking_System.Bank menu\n" +
                "2. Banking_System.Client menu\n" +
                "0. Exit\n");

        System.out.print("choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                bankMenu();
            case 2:
                System.out.println("coming soon...");
                waitForUser();
                //client menu
            case 0:
                clearScreen();
                System.exit(1);
        }
    }

    private static void bankMenu() throws IOException {
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                "1. Manage your Banking_System.Bank\n" +
                "2. Create a new Banking_System.Bank\n" +
                "3. Exit\n");

        System.out.print("choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Select bank: ");
                for (int counter = 0; counter < banks.size(); counter++){
                    System.out.println(String.valueOf(counter) + ". " + banks.get(counter).getBankName());
                }
                System.out.print("choice: ");
                int choiceBank = scanner.nextInt(); //<- wybierz metode arraylist do uruchomienia metody bank
            case 2:
                System.out.println(logo);
                System.out.println("Enter your Banking_System.Bank name: ");
                String name = scanner.next();
                System.out.println("Enter initial Banking_System.Bank resources: ");
                float res = scanner.nextFloat();
                System.out.println("Set owner login: ");
                String login = scanner.next();
                System.out.println("Set owner password: ");
                String password = scanner.next();
                banks.add(new Bank(name, res));
                banks.get(banks.size() - 1).setOwner(login, password);
                System.out.println("The bank has been successfully created");
                waitForUser();
                bankMenu();
            case 3:
                System.exit(1);
        }
    }

    public static void waitForUser() throws IOException {
        System.out.println("press any key to continue...");
        System.in.read();
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}