package BankingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;

public class Client {

    int clientID;
    PersonalData clientPersonalData;
    String clientPassword;
    LocalDate clientJoinDate;
    Bank clientBank;
    ArrayList<Account> clientBankAccounts;

    static ArrayList<Integer> clientIDNumbers = new ArrayList<>();

    Client(int clientID, PersonalData customerPersonalData, int customerBankIndex, LocalDate clientJoinDate) {
        this.clientID = clientID;
        this.clientJoinDate = Objects.requireNonNullElseGet(clientJoinDate, LocalDate::now);
        this.clientPersonalData = customerPersonalData;
        if (customerBankIndex == 0){
            this.clientBank = null;
        }else {
            this.clientBank = Main.banks.get(customerBankIndex - 1);
        }
        this.clientBankAccounts = new ArrayList<>();
    }

    static int clientIDGenerator() {
        int range = 999999999 - 100000000 + 1;
        int clientGeneratedID;
        do {
            clientGeneratedID = Main.rand.nextInt(range) + 100000000;
        } while (!clientCheckID(clientGeneratedID));
        clientIDNumbers.add(clientGeneratedID);
        return clientGeneratedID;
    }

    static boolean clientCheckID(int clientGeneratedID) {
        if (clientIDNumbers.isEmpty()) {
            return true;
        }
        for (int clientID : clientIDNumbers) {
            if (clientGeneratedID == clientID) {
                return false;
            }
        }
        return true;
    }

    static void creatingClientMenu() throws IOException{
        if (Main.banks.size() == 0) {
            Main.clearScreen();
            System.out.println(Main.logo);
            System.out.println("\nThere are no banks yet");
            Main.waitForUser();
            Main.clientMenu();
        }
        int userChoiceBank;
        while (true) {
            Main.clearScreen();
            System.out.println(Main.logo);
            System.out.println("\nSelect a bank: ");
            for (int counter = 0; counter < Main.banks.size(); counter++) {
                System.out.println((counter + 1) + ". " + Main.banks.get(counter).getBankName());
            }
            try {
                System.out.print("\nchoice: ");
                userChoiceBank = Main.scanner.nextInt();
                Main.banks.get(userChoiceBank - 1);
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
        Client newClient = new Client(clientIDGenerator(),
                PersonalData.personalDataForm(), (userChoiceBank), null);
        Main.banks.get(userChoiceBank - 1).clients.add(newClient);
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nYour client ID number: " + newClient.clientID);
        System.out.print("\nSet password for your account: ");
        newClient.setClientPassword(Main.scanner.next());
        Main.bankDataBase.insertClient(newClient.clientID, newClient.clientPersonalData.personalID,
                newClient.clientPassword, java.sql.Date.valueOf(newClient.clientJoinDate),
                newClient.clientBank.ownerID);

        System.out.println("The account has been successfully created");
    }

    boolean logInToClient() throws IOException {
        System.out.print("Enter password: ");
        String inClientPassword = Main.scanner.next();
        if (!Objects.equals(inClientPassword, this.clientPassword)) {
            System.out.println("\nPassword incorrect!");
            Main.waitForUser();
            return false;
        }
        return true;
    }

    void showClientAccounts() throws IOException{
        if (this.clientBankAccounts.size() == 0) {
            System.out.println("\nNo bank accounts yet");
            Main.waitForUser();
            clientDashboard();
        }
        for (int counter = 0; counter < this.clientBankAccounts.size(); counter++) {
            System.out.println("\n" + (counter + 1) + ". BankingSystem.Account number: "
                    + this.clientBankAccounts.get(counter).accountNumber
                    + "\n   Type: " + this.clientBankAccounts.get(counter).accountType
                    + "\n   Resources: " + Main.df.format(this.clientBankAccounts.get(counter).accountResources)
                    + "$");
            if (this.clientBankAccounts.get(counter) instanceof SavingsAccount) {
                System.out.println("   Monthly interest - "
                        + clientBank.bankInterestRate * 100 + "%");
            } else if (Objects.equals(this.clientBankAccounts.get(counter).accountType, "Credit account")) {
                System.out.println("   Credit interest rate - "
                        + clientBank.bankCreditInterestRate * 100 + "%");
            }
        }
    }

    void clientDashboard() throws IOException{
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBankingSystem.Bank name: " + this.clientBank.bankName);
        System.out.println("BankingSystem.Client ID: " + this.clientID);
        System.out.println("Join date: " + this.clientJoinDate);
        System.out.println(
                "\nSelect:\n" +
                        "1. Your accounts\n" +
                        "2. Create account\n" +
                        "3. Make a transfer\n" +
                        "4. Take credit\n" +
                        "5. Manage your personal data\n" +
                        "6. Log out\n" +
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
                clientDashboard();
            }
        }
        switch (choice) {
            case 1:
                int accountChoice;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nYour bank accounts :");
                        showClientAccounts();
                        System.out.println("\n0. Back");
                        System.out.print("\nchoice: ");
                        accountChoice = Main.scanner.nextInt();
                        if (accountChoice == 0) {
                            break;
                        }
                        this.clientBankAccounts.get(accountChoice - 1).accountDashboard();
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Wrong choice, please try again");
                        Main.waitForUser();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }
                clientDashboard();
                break;
            case 2:
                Account newAccount = Account.creatingAccountMenu(this.clientBank);
                if (Objects.isNull(newAccount)) {
                    System.out.println("No new bank account opened");
                } else {
                    if (newAccount instanceof SavingsAccount) {
                        ((SavingsAccount) newAccount).increaseAccountResources();
                    }
                    Main.bankDataBase.insertAccount(newAccount.accountNumber, this.clientID, this.clientBank.ownerID,
                            newAccount.accountType, newAccount.accountResources,
                            java.sql.Date.valueOf(newAccount.accountCreationDate), null);
                    this.clientBankAccounts.add(newAccount);
                    System.out.println("\nBankingSystem.Bank account successfully opened");
                }
                Main.waitForUser();
                clientDashboard();
                break;
            case 3:
                while (true) {
                    Main.clearScreen();
                    System.out.println(Main.logo);
                    System.out.println("\nSelect the bank account from which you want to make the transfer:");
                    showClientAccounts();

                    System.out.print("\nchoice: ");
                    choice = Main.scanner.nextInt();
                    if (Objects.equals(this.clientBankAccounts.get(choice - 1).accountType, "Credit account")) {
                        System.out.println("It is not possible to make a transfer from a credit account");
                        Main.waitForUser();
                        continue;
                    }
                    break;
                }
                if (Transfer.createTransferMenu(this.clientBankAccounts.get(choice - 1))) {
                    System.out.println("The transfer was successful");
                } else {
                    System.out.println("The transfer was unsuccessful");
                }
                Main.waitForUser();
                clientDashboard();
                break;
            case 4:
                if (this.clientBankAccounts.isEmpty()) {
                    Main.clearScreen();
                    System.out.println(Main.logo);
                    System.out.println("\nNo bank accounts yet");
                    Main.waitForUser();
                    clientDashboard();
                }
                float creditAmount;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.print("\nAvailable credit amount: "
                                + Main.df.format(this.clientBank.bankResources)
                                + "\n\nEnter the credit amount: ");
                        creditAmount = Main.scanner.nextFloat();
                        if (creditAmount > this.clientBank.bankResources || creditAmount <= 0) {
                            throw new IllegalArgumentException();
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid amount, please try again");
                        Main.waitForUser();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nSelect the account to which the bank will transfer the funds:");
                        showClientAccounts();
                        System.out.print("\nchoice: ");
                        choice = Main.scanner.nextInt();
                        this.clientBankAccounts.get(choice - 1);
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
                this.clientBank.bankResources -= creditAmount;
                this.clientBankAccounts.get(choice - 1).accountResources += creditAmount;
                this.clientBankAccounts.add(
                        new Account(Account.accountNumberGenerator(this.clientBank), "Credit account",
                                -(creditAmount + creditAmount * this.clientBank.bankCreditInterestRate),
                                this.clientBank, null));
                System.out.println("\nCredit was successfully granted");
                Main.waitForUser();
                clientDashboard();
                break;
            case 5:
                if (this.clientPersonalData.managePersonalData()) {
                    clientDashboard();
                }
                break;
            case 6:
                Main.clientMenu();
                break;
            case 0:
                System.exit(1);
            default: {
                System.out.println("Wrong choice, please try again");
                Main.waitForUser();
                clientDashboard();
            }
        }

    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

}
