import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Client {

    int clientID;
    PersonalData clientPersonalData;
    String clientPassword;
    LocalDate clientJoinDate;
    Bank clientBank;
    ArrayList<Account> clientBankAccounts;

    Client(int clientID, PersonalData customerPersonalData, int customerBankIndex) {
        this.clientID = clientID;
        this.clientJoinDate = LocalDate.now();
        this.clientPersonalData = customerPersonalData;
        this.clientBank = Main.banks.get(customerBankIndex - 1);
        this.clientBankAccounts = new ArrayList<>();
    }

    static int clientIDGenerator(int indexBank) {
        int range = 999999999 - 100000000 + 1;
        int clientGeneratedID;
        do {
            clientGeneratedID = Main.rand.nextInt(range) + 100000000;
        } while (!clientCheckID(clientGeneratedID, indexBank));
        return clientGeneratedID;
    }

    static boolean clientCheckID(int clientGeneratedID, int indexBank) {
        if (Main.banks.get(indexBank - 1).clients.isEmpty()) {
            return true;
        }
        for (Client client : Main.banks.get(indexBank - 1).clients) {
            if (clientGeneratedID == client.clientID) {
                return false;
            }
        }
        return true;
    }

    static void creatingClientMenu() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        if (Main.banks.size() == 0) {
            System.out.println("\nThere are no banks yet");
            Main.waitForUser();
            Main.clientMenu();
        }
        System.out.println("\nSelect a bank: ");
        for (int counter = 0; counter < Main.banks.size(); counter++) {
            System.out.println(String.valueOf(counter + 1) + ". " + Main.banks.get(counter).getBankName());
        }
        System.out.print("\nchoice: ");
        int userChoiceBank = Main.scanner.nextInt();
        Main.clearScreen();
        System.out.println(Main.logo);
        int newID = clientIDGenerator(userChoiceBank);
        Main.banks.get(userChoiceBank - 1).clients.add(new Client(newID,
                PersonalData.personalDataForm(), (userChoiceBank)));
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nYour client ID number: " + newID);
        System.out.println("\nSet password for your account: ");
        Main.banks.get(userChoiceBank - 1).clients.get(Main.banks.get(userChoiceBank - 1).clients.size()
                - 1).setClientPassword(Main.scanner.next());
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

    void showClientAccounts() throws IOException {
        if (this.clientBankAccounts.size() == 0) {
            System.out.println("\nNo bank accounts yet");
            Main.waitForUser();
            clientDashboard();
        }
        for (int counter = 0; counter < this.clientBankAccounts.size(); counter++) {
            System.out.println("\n" + String.valueOf(counter + 1) + ". Account number: "
                    + this.clientBankAccounts.get(counter).accountNumber
                    + "\n   Type: " + this.clientBankAccounts.get(counter).accountType
                    + "\n   Resources: " + this.clientBankAccounts.get(counter).accountResources
                    + "$");
                    if (this.clientBankAccounts.get(counter) instanceof SavingsAccount){
                        System.out.println("   Monthly interest - "
                                + clientBank.bankInterestRate * 100 + "%");
                    }
        }
    }

    void clientDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBank name: " + this.clientBank.bankName);
        System.out.println("Client ID: " + this.clientID);
        System.out.println("Join date: " + this.clientJoinDate);
        System.out.println(
                "\nSelect:\n" +
                        "1. Your accounts\n" +
                        "2. Create account\n" +
                        "3. Make a transfer\n" +
                        "4. -(Take credit)-\n" +
                        "5. Manage your personal data\n" +
                        "6. Log out\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                while (choice!=0){
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nYour bank accounts :");
                showClientAccounts();
                System.out.println("\n0. Back");
                System.out.print("\nchoice: ");
                choice = Main.scanner.nextInt();
                if (choice == 0){
                    clientDashboard();
                }
                this.clientBankAccounts.get(choice - 1).accountDashboard();}
                clientDashboard();
            case 2:
                Account newAccount = Account.creatingAccountMenu(this.clientBank);
                if (Objects.isNull(newAccount)) {
                    System.out.println("No new bank account opened");
                } else {
                    if(newAccount instanceof SavingsAccount){
                        ((SavingsAccount) newAccount).increaseAccountResources();
                    }
                    this.clientBankAccounts.add(newAccount);
                    System.out.println("\nBank account successfully opened");
                }
                Main.waitForUser();
                clientDashboard();
                break;
            case 3:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nSelect the bank account from which you want to make the transfer:");
                showClientAccounts();
                System.out.print("\nchoice: ");
                choice = Main.scanner.nextInt();
                if (Transfer.createTransferMenu(this.clientBankAccounts.get(choice - 1))){
                    System.out.println("The transfer was successful");
                }else {
                    System.out.println("The transfer was unsuccessful");
                }
                Main.waitForUser();
                clientDashboard();
                break;
            case 4:
                System.out.println("coming soon...");
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
        }

    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }
}
