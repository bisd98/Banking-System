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
    static final Scanner scanner = new Scanner(System.in);

    static ArrayList<Bank> banks = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        menu();
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
                if (banks.size() == 0){
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    bankMenu();
                }
                System.out.println("\nSelect a bank: ");
                for (int counter = 0; counter < banks.size(); counter++){
                    System.out.println(String.valueOf(counter + 1) + ". " + banks.get(counter).getBankName());
                }
                System.out.print("\nchoice: ");
                int choiceBank = scanner.nextInt();
                clearScreen();
                System.out.println(logo);
                if (banks.get(choiceBank - 1).logInToBank()){
                    banks.get(choiceBank - 1).bankDashboard();
                }
                else {bankMenu();}
            case 2:
                clearScreen();
                System.out.println(logo);
                Bank.creatingMenu();
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
                if (banks.size() == 0){
                    System.out.println("\nThere are no banks yet");
                    waitForUser();
                    clientMenu();
                }
                System.out.println("\nSelect your bank: ");
                for (int counter = 0; counter < banks.size(); counter++){
                    System.out.println(String.valueOf(counter + 1) + ". " + banks.get(counter).getBankName());
                }
                System.out.print("\nchoice: ");
                int choiceBank = scanner.nextInt();
                clearScreen();
                System.out.println(logo);
                System.out.println("\nEnter client ID:");
                int inClientID = scanner.nextInt();
                for (Client acc : banks.get(choiceBank - 1).accounts){
                    if (inClientID == acc.clientID){
                        if (acc.logInToAccount()){
                            acc.accountDashboard();
                        }
                        else {clientMenu();}
                    }
                }
                System.out.println("There is no customer with this ID in this bank");
                waitForUser();
                clientMenu();
            case 2:
                Client.creatingAccountMenu();
                waitForUser();
                clientMenu();
            case 3:
                menu();
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