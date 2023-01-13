import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Client {

    int clientID;

    PersonalData customerPersonalData;
    String clientPassword;
    LocalDate clientJoinDate;
    String customerBankName;

    ArrayList<Account> bankAccounts;

    static int generatorID(int indexBank) {
        Random rand = new Random();
        int range = 999999999 - 100000000 + 1;
        int clientGeneratedID;
        boolean flag = true;
        do {
            clientGeneratedID = rand.nextInt(range) + 100000000;
        } while (!checkID(clientGeneratedID, indexBank));
        return clientGeneratedID;
    }

    static boolean checkID(int clientGeneratedID, int indexBank) {
        if (Main.banks.get(indexBank - 1).accounts.isEmpty()) {
            return true;
        }
        for (Client client : Main.banks.get(indexBank - 1).accounts) {
            if (clientGeneratedID == client.clientID) {
                return false;
            }
        }
        return true;
    }

    static void creatingAccountMenu() throws IOException {
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
        int newID = generatorID(userChoiceBank);
        Main.banks.get(userChoiceBank - 1).accounts.add(new Client(newID,
                PersonalData.personalDataForm(), Main.banks.get(userChoiceBank - 1).bankName));
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nYour client ID number: " + newID);
        System.out.println("\nSet password for your account: ");
        Main.banks.get(userChoiceBank - 1).accounts.get(Main.banks.get(userChoiceBank - 1).accounts.size()
                - 1).setClientPassword(Main.scanner.next());
        System.out.println("The account has been successfully created");
    }

    boolean logInToAccount() throws IOException {
        System.out.println("Enter password:");
        String inClientPassword = Main.scanner.next();
            if (!Objects.equals(inClientPassword, this.clientPassword)) {
                System.out.println("\nPassword incorrect!");
                Main.waitForUser();
                return false;
            }
        return true;
    }

    void accountDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBank name: " + this.customerBankName);
        System.out.println("Client ID: " + this.clientID);
        System.out.println("Join date: " + this.clientJoinDate);
        System.out.println(
                "\nSelect:\n" +
                        "1. Your accounts\n" +
                        "2. ---\n" +
                        "3. ---\n" +
                        "4. Manage your personal data\n" +
                        "5. Log out\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("coming soon...");
                Main.waitForUser();
                accountDashboard();
            case 2:
                System.out.println("coming soon...");
                Main.waitForUser();
                accountDashboard();
            case 3:
                System.out.println("coming soon...");
                Main.waitForUser();
                accountDashboard();
            case 4:
                if(this.customerPersonalData.managePersonalData()){
                    accountDashboard();
                }
            case 5:
                Main.bankMenu();
            case 0:
                System.exit(1);
        }

    }

    Client(int clientID, PersonalData customerPersonalData, String customerBankName) {
        this.clientID = clientID;
        this.clientJoinDate = LocalDate.now();
        this.customerPersonalData = customerPersonalData;
        this.customerBankName = customerBankName;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }
}
