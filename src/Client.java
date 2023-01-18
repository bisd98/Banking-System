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
        System.out.println("Enter password:");
        String inClientPassword = Main.scanner.next();
        if (!Objects.equals(inClientPassword, this.clientPassword)) {
            System.out.println("\nPassword incorrect!");
            Main.waitForUser();
            return false;
        }
        return true;
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
                        "3. -(Make a transfer)-\n" +
                        "4. -(Take credit)-\n" +
                        "5. Manage your personal data\n" +
                        "6. Log out\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                Main.clearScreen();
                System.out.println(Main.logo);
                if (this.clientBankAccounts.size() == 0) {
                    System.out.println("\nNo bank accounts yet");
                    Main.waitForUser();
                    clientDashboard();
                }
                System.out.println("\nYour bank accounts :\n");
                for (int counter = 0; counter < this.clientBankAccounts.size(); counter++) {
                    System.out.println(String.valueOf(counter + 1) + ". Account number: "
                            + this.clientBankAccounts.get(counter).accountNumber
                            + "\nType: " + this.clientBankAccounts.get(counter).accountType
                            + "\nResources: " + this.clientBankAccounts.get(counter).accountResources
                            + "$");
                }
                Main.waitForUser();
                clientDashboard();
            case 2:
                Account newAccount = Account.creatingAccountMenu(this.clientBank);
                if (Objects.isNull(newAccount)) {
                    System.out.println("No new bank account opened");
                } else {
                    this.clientBankAccounts.add(newAccount);
                    System.out.println("Bank account successfully opened");
                }
                Main.waitForUser();
                clientDashboard();
            case 3:
                System.out.println("coming soon...");
                Main.waitForUser();
                clientDashboard();
            case 4:
                System.out.println("coming soon...");
                Main.waitForUser();
                clientDashboard();
            case 5:
                if (this.clientPersonalData.managePersonalData()) {
                    clientDashboard();
                }
            case 6:
                Main.bankMenu();
            case 0:
                System.exit(1);
        }

    }

    Client(int clientID, PersonalData customerPersonalData, int customerBankIndex) {
        this.clientID = clientID;
        this.clientJoinDate = LocalDate.now();
        this.clientPersonalData = customerPersonalData;
        this.clientBank = Main.banks.get(customerBankIndex - 1);
        this.clientBankAccounts = new ArrayList<>();
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }
}
