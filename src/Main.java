import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    static Random rand = new Random();
    static ArrayList<Bank> banks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        testCode();
        menu();
    }

    static void testCode() {
        Bank testBank = new Bank("Test Bank", 1000, 0.03F);
        testBank.ownerLogin = "admin";
        testBank.ownerPassword = "admin";
        banks.add(testBank);
        Client testClient = new Client(Client.clientIDGenerator(1),
                new PersonalData("Luke", "Lucky", 77777777777L,
                        LocalDate.parse("1997-07-07"), "luke@gmail.com",
                        777777777L), 1);
        testClient.clientID = 777;
        testClient.clientPassword = "admin";
        testBank.clients.add(testClient);
    }

    private static void menu() throws IOException {
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Bank menu\n" +
                        "2. Client menu\n" +
                        "0. Exit\n");
        System.out.print("choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                bankMenu();
            case 2:
                clientMenu();
            case 0:
                clearScreen();
                System.exit(1);
        }
    }

    static void bankMenu() throws IOException {
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Log in to the Bank\n" +
                        "2. Create a new Bank\n" +
                        "3. Back\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearScreen();
                System.out.println(logo);
                if (banks.size() == 0) {
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    bankMenu();
                }
                System.out.println("\nSelect a bank: ");
                for (int counter = 0; counter < banks.size(); counter++) {
                    System.out.println(counter + 1 + ". " + banks.get(counter).getBankName());
                }
                System.out.print("\nchoice: ");
                int choiceBank = scanner.nextInt();
                clearScreen();
                System.out.println(logo);
                if (banks.get(choiceBank - 1).logInToBank()) {
                    banks.get(choiceBank - 1).bankDashboard();
                } else {
                    bankMenu();
                }
            case 2:
                clearScreen();
                System.out.println(logo);
                Bank.creatingBankMenu();
                waitForUser();
                bankMenu();
            case 3:
                menu();
            case 0:
                System.exit(1);
        }
    }

    static void clientMenu() throws IOException {
        clearScreen();
        System.out.println(logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Log in to the Bank\n" +
                        "2. Become a Bank customer\n" +
                        "3. Back\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                clearScreen();
                System.out.println(logo);
                if (banks.size() == 0) {
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    clientMenu();
                }
                System.out.println("\nSelect your bank: ");
                for (int counter = 0; counter < banks.size(); counter++) {
                    System.out.println(counter + 1 + ". " + banks.get(counter).getBankName());
                }
                System.out.print("\nchoice: ");
                int choiceBank = scanner.nextInt();
                clearScreen();
                System.out.println(logo);
                System.out.print("\nEnter client ID: ");
                int inClientID = scanner.nextInt();
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
                break;
            case 2:
                Client.creatingClientMenu();
                waitForUser();
                clientMenu();
                break;
            case 3:
                menu();
                break;
            case 0:
                System.exit(1);
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