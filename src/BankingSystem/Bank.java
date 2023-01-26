package BankingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;

public class Bank {
    String bankName;
    LocalDate bankCreationDate;
    float bankResources;
    int ownerID;
    String ownerPassword;
    int bic;
    float bankInterestRate;
    float bankCreditInterestRate;
    ArrayList<Client> clients;
    ArrayList<Long> individualClientNumbers;
    ArrayList<Integer> individualCardNumbers;
    static ArrayList<Integer> ownerIDNumbers = new ArrayList<>();

    Bank(String bankName, float bankResources, float bankInterestRate,
         float bankCreditInterestRate, LocalDate bankCreationDate, int BIC) {

        this.bankName = bankName;
        this.bankResources = bankResources;
        this.bankInterestRate = bankInterestRate;
        this.bankCreditInterestRate = bankCreditInterestRate;
        this.bankCreationDate = Objects.requireNonNullElseGet(bankCreationDate, LocalDate::now);
        this.clients = new ArrayList<>();
        if (BIC == 0) {
            this.bic = setBIC();
        } else {
            this.bic = BIC;
        }
        this.individualClientNumbers = new ArrayList<>();
        this.individualCardNumbers = new ArrayList<>();
    }

    static void creatingBankMenu() throws IOException {
        String name;
        float res, interest, creditInterest;
        while (true) {
            try {
                name = "";
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.print("\nEnter your Bank name: ");
                Main.scanner.nextLine();
                name += Main.scanner.nextLine();
                System.out.print("Enter initial Bank resources: ");
                res = Main.scanner.nextFloat();
                System.out.print("Enter Bank interest rate in percents: ");
                interest = Main.scanner.nextFloat() / 100;
                System.out.print("Enter Bank credit interest rate in percents: ");
                creditInterest = Main.scanner.nextFloat() / 100;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        Bank newBank = new Bank(name, res, interest, creditInterest, null, 0);
        Main.banks.add(newBank);
        Main.clearScreen();
        System.out.println(Main.logo);
        newBank.setOwner();
        Main.bankDataBase.insertBank(newBank.ownerID, newBank.bankName, java.sql.Date.valueOf(newBank.bankCreationDate),
                newBank.bankResources, newBank.ownerPassword, newBank.bic, newBank.bankInterestRate,
                newBank.bankCreditInterestRate);
        System.out.println("The bank has been successfully created");
    }

    void bankDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBank name: " + this.bankName);
        System.out.println("Creation date: " + this.bankCreationDate);
        System.out.println("Bank resources: " + Main.df.format(this.bankResources) + '$');
        System.out.println("Bank interest rate: " + this.bankInterestRate * 100 + '%');
        System.out.println("Bank credit interest rate: " + this.bankCreditInterestRate * 100 + '%');
        System.out.println("Clients: " + this.clients.size());
        System.out.println(
                "\nSelect:\n" +
                        "1. Manage your Bank\n" +
                        "2. Show clients\n" +
                        "3. Import clients\n" +
                        "4. Export clients\n" +
                        "5. Log out\n" +
                        "0. Exit\n");

        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = Main.scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
                bankDashboard();
            }
        }

        switch (choice) {
            case 1 -> manageBank();
            case 2 -> {
                Main.clearScreen();
                System.out.println(Main.logo);
                if (this.clients.size() == 0) {
                    System.out.println("\nNo clients yet");
                    Main.waitForUser();
                    bankDashboard();
                    break;
                }
                System.out.println("\nBank customer list :\n");
                for (int counter = 0; counter < this.clients.size(); counter++) {
                    System.out.println((counter + 1) + ". Client ID: "
                            + this.clients.get(counter).clientID
                            + "\n   Name: " + this.clients.get(counter).clientPersonalData.name
                            + " " + this.clients.get(counter).clientPersonalData.surname + "\n");
                }
                Main.waitForUser();
                bankDashboard();
            }
            case 3 -> {
                if (Main.banks.size() <= 1) {
                    Main.clearScreen();
                    System.out.println(Main.logo);
                    System.out.println("\nThere are no banks to import from");
                    Main.waitForUser();
                    Main.bankMenu();
                }
                int choiceBank;
                while (true) {
                    Main.clearScreen();
                    System.out.println(Main.logo);
                    System.out.println("\nSelect a bank: ");
                    for (int counter = 0; counter < Main.banks.size(); counter++) {
                        System.out.println(counter + 1 + ". " + Main.banks.get(counter).getBankName());
                    }
                    try {
                        System.out.print("\nchoice: ");
                        choiceBank = Main.scanner.nextInt();
                        Main.banks.get(choiceBank - 1);
                        if (Main.banks.get(choiceBank - 1).equals(this)){
                            System.out.println("Cannot import from the same bank");
                            Main.waitForUser();
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Wrong choice, please try again");
                        Main.waitForUser();
                    }
                }
                CsvCreator.importClientsCsv(Main.banks.get(choiceBank - 1), this);
                Main.waitForUser();
                bankDashboard();
            }
            case 4 -> {
                CsvCreator.exportClientsCSV(this.clients, this.bankName);
                Main.waitForUser();
                bankDashboard();
            }
            case 5 -> Main.bankMenu();
            case 0 -> System.exit(1);
            default -> {
                System.out.println("Wrong choice, please try again");
                Main.waitForUser();
                bankDashboard();
            }
        }

    }

    void manageBank() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Rename your Bank\n" +
                        "2. Change interest rate\n" +
                        "3. Change credit interest rate\n" +
                        "4. Change owner password\n" +
                        "5. Back\n" +
                        "0. Exit\n");

        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = Main.scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
                manageBank();
            }
        }

        switch (choice) {
            case 1 -> {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nName of your Bank: " + this.bankName);
                System.out.print("\nEnter new Bank name: ");
                String name = Main.scanner.next();
                name += Main.scanner.nextLine();
                setBankName(name);
                Main.bankDataBase.changeBankName(this.ownerID, name);
                System.out.println("Bank name successfully changed");
                Main.waitForUser();
                manageBank();
            }
            case 2 -> {
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nCurrent interest rate: "
                                + this.bankInterestRate * 100 + "%");
                        System.out.print("\nEnter new interest rate in percents: ");
                        float newInterestRate = (Main.scanner.nextFloat() / 100);
                        setBankInterestRate(newInterestRate);
                        Main.bankDataBase.changeBankInterestRate(this.ownerID, newInterestRate);
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }
                System.out.println("\nInterest rate successfully changed");
                Main.waitForUser();
                manageBank();
            }
            case 3 -> {
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nCurrent credit interest rate: "
                                + this.bankCreditInterestRate * 100 + "%");
                        System.out.print("\nEnter new credit interest rate in percents: ");
                        float newCreditInterestRate = (Main.scanner.nextFloat() / 100);
                        setBankCreditInterestRate(newCreditInterestRate);
                        Main.bankDataBase.changeBankCreditInterestRate(this.ownerID, newCreditInterestRate);
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }
                System.out.println("\nCredit interest rate successfully changed");
                Main.waitForUser();
                manageBank();
            }
            case 4 -> {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nOwner ID: " + this.ownerID);
                System.out.print("\nEnter new password: ");
                String newPassword = Main.scanner.next();
                setPassword(newPassword);
                Main.bankDataBase.changeOwnerPassword(this.ownerID, newPassword);
                System.out.println("\nPassword successfully changed");
                Main.waitForUser();
                manageBank();
            }
            case 5 -> bankDashboard();
            case 0 -> System.exit(1);
            default -> {
                System.out.println("Wrong choice, please try again");
                Main.waitForUser();
                manageBank();
            }
        }
    }

    boolean logInToBank() throws IOException {
        int inLogin;
        while (true) {
            Main.clearScreen();
            System.out.println(Main.logo);
            System.out.print("\nEnter owner login: ");
            try {
                inLogin = Main.scanner.nextInt();
                break;
            }catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        System.out.print("Enter password: ");
        String inPassword = Main.scanner.next();
        if (inLogin != this.ownerID) {
            System.out.println("\nLogin incorrect!");
            Main.waitForUser();
            return false;
        } else {
            if (!Objects.equals(inPassword, this.ownerPassword)) {
                System.out.println("\nPassword incorrect!");
                Main.waitForUser();
                return false;
            }
        }
        return true;
    }

    public String getBankName() {
        return bankName;
    }

    void setOwner() {
        this.ownerID = ownerIDGenerator();
        System.out.println("\nYour owner ID number: " + this.ownerID);
        System.out.print("\nSet owner password: ");
        this.ownerPassword = Main.scanner.next();
    }

    static int ownerIDGenerator() {
        int range = 999999999 - 100000000 + 1;
        int ownerGeneratedID;
        do {
            ownerGeneratedID = Main.rand.nextInt(range) + 100000000;
        } while (!ownerCheckID(ownerGeneratedID));
        ownerIDNumbers.add(ownerGeneratedID);
        return ownerGeneratedID;
    }

    static boolean ownerCheckID(int ownerGeneratedID) {
        if (ownerIDNumbers.isEmpty()) {
            return true;
        }
        for (int ownerID : ownerIDNumbers) {
            if (ownerGeneratedID == ownerID) {
                return false;
            }
        }
        return true;
    }

    void setBankName(String bankName) {
        this.bankName = bankName;
    }

    void setPassword(String password) {
        this.ownerPassword = password;
    }

    void setBankInterestRate(Float interestRate) {
        this.bankInterestRate = interestRate;
    }

    void setBankCreditInterestRate(float bankCreditInterestRate) {
        this.bankCreditInterestRate = bankCreditInterestRate;
    }

    boolean checkBIC(int generatedBIC) {
        if (Main.banks.isEmpty()) {
            return true;
        }
        for (Bank bank : Main.banks) {
            if (generatedBIC == bank.bic) {
                return false;
            }
        }
        return true;
    }

    int setBIC() {
        int range = 99999999 - 10000000 + 1;
        int newBIC;
        do {
            newBIC = Main.rand.nextInt(range) + 10000000;
        } while (!checkBIC(newBIC));
        return newBIC;
    }
}