import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Bank {
    String bankName;
    LocalDate bankCreationDate;
    float bankResources = 0;
    String ownerLogin, ownerPassword;
    int bic;

    float bankInterestRate;
    ArrayList<Client> clients;
    ArrayList<Long> individualNumbers;

    Bank(String bankName, float bankResources, float bankInterestRate) {

        this.bankName = bankName;
        this.bankResources = bankResources;
        this.bankInterestRate = bankInterestRate;
        this.bankCreationDate = LocalDate.now();
        this.clients = new ArrayList<>();
        this.bic = setBIC();
        this.individualNumbers = new ArrayList<>();
    }

    static void creatingBankMenu() {
        System.out.print("\nEnter your Bank name: ");
        String name = "";
        Main.scanner.nextLine();
        name += Main.scanner.nextLine();
        System.out.print("Enter initial Bank resources: ");
        float res = Main.scanner.nextFloat();
        System.out.print("Enter Bank interest rate in percents: ");
        float interest = Main.scanner.nextFloat()/100;
        Main.banks.add(new Bank(name, res, interest));
        Main.banks.get(Main.banks.size() - 1).setOwner();
        System.out.println("The bank has been successfully created");
    }

    void bankDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBank name: " + this.bankName);
        System.out.println("Creation date: " + this.bankCreationDate);
        System.out.println("Bank resources: " + this.bankResources + '$');
        System.out.println("Clients: " + this.clients.size());
        System.out.println(
                "\nSelect:\n" +
                        "1. Manage your Bank\n" +
                        "2. Show clients\n" +
                        "3. Import clients\n" +
                        "4. Export clients\n" +
                        "5. Log out\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                manageBank();
                break;
            case 2:
                Main.clearScreen();
                System.out.println(Main.logo);
                if (this.clients.size() == 0){
                    System.out.println("\nNo clients yet");
                    Main.waitForUser();
                    bankDashboard();
                    break;
                }
                System.out.println("\nBank customer list :\n");
                for (int counter = 0; counter < this.clients.size(); counter++){
                    System.out.println(String.valueOf(counter + 1) + ". Client ID: "
                            + this.clients.get(counter).clientID
                            + ", Name: " + this.clients.get(counter).clientPersonalData.name
                            + " " + this.clients.get(counter).clientPersonalData.surname);
                }
                Main.waitForUser();
                bankDashboard();
                break;
            case 3:
                System.out.println("coming soon...");
                Main.waitForUser();
                bankDashboard();
                break;
            case 4:
                System.out.println("coming soon...");
                Main.waitForUser();
                bankDashboard();
                break;
            case 5:
                Main.bankMenu();
                break;
            case 0:
                System.exit(1);
        }

    }

    void manageBank() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println(
                "\nSelect:\n" +
                        "1. Rename your Bank\n" +
                        "2. Change owner password\n" +
                        "3. Delete Bank\n" +
                        "4. Back\n" +
                        "0. Exit\n");

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nName of your Bank: " + this.bankName);
                System.out.print("\nEnter new Bank name: ");
                String name = Main.scanner.next();
                name += Main.scanner.nextLine();
                setBankName(name);
                System.out.println("Bank name successfully changed");
                Main.waitForUser();
                manageBank();
                break;
            case 2:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("Owner login: " + this.ownerLogin);
                System.out.print("\nEnter new password: ");
                setPassword(Main.scanner.next());
                System.out.println("\nPassword successfully changed");
                Main.waitForUser();
                manageBank();
                break;
            case 3:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("coming soon...");
                Main.waitForUser();
                manageBank();
                break;
            case 4:
                bankDashboard();
                break;
            case 0:
                System.exit(1);
        }
    }

    boolean logInToBank() throws IOException {
        System.out.print("\nEnter owner login:");
        String inLogin = Main.scanner.next();
        System.out.print("Enter password:");
        String inPassword = Main.scanner.next();
        if (!Objects.equals(inLogin, this.ownerLogin)) {
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
        System.out.println("Set owner login: ");
        String login = Main.scanner.next();
        System.out.println("Set owner password: ");
        String password = Main.scanner.next();
        this.ownerLogin = login;
        this.ownerPassword = password;
    }

    void setBankName(String bankName) {
        this.bankName = bankName;
    }

    void setPassword(String password) {
        this.ownerPassword = password;
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

    int setBIC(){
        int range = 99999999 - 10000000 + 1;
        int newBIC;
        do {
            newBIC = Main.rand.nextInt(range) + 10000000;
        } while (!checkBIC(newBIC));
        return newBIC;
    }
}