import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Bank {
    String bankName;

    LocalDate creationDate;
    float bankResources = 0;

    ArrayList<Client> accounts = new ArrayList<>();

    String ownerLogin, password;

    static void creatingMenu() {
        System.out.println("\nEnter your Bank name: ");
        String name = Main.scanner.next();
        name += Main.scanner.nextLine();
        System.out.println("Enter initial Bank resources: ");
        float res = Main.scanner.nextFloat();
        Main.banks.add(new Bank(name, res));
        Main.banks.get(Main.banks.size() - 1).setOwner();
        System.out.println("The bank has been successfully created");
    }

    void bankDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nBank name: " + this.bankName);
        System.out.println("Creation date: " + this.creationDate);
        System.out.println("Bank resources: " + this.bankResources + '$');
        System.out.println("Clients: " + this.accounts.size());
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
            case 2:
                System.out.println("coming soon...");
                Main.waitForUser();
                bankDashboard();
            case 3:
                System.out.println("coming soon...");
                Main.waitForUser();
                bankDashboard();
            case 4:
                System.out.println("coming soon...");
                Main.waitForUser();
                bankDashboard();
            case 5:
                Main.bankMenu();
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
                System.out.println("\nEnter new Bank name: ");
                String name = Main.scanner.next();
                name += Main.scanner.nextLine();
                setBankName(name);
                System.out.println("Bank name successfully changed");
                Main.waitForUser();
                manageBank();
            case 2:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("Owner login: " + this.ownerLogin);
                System.out.println("\nEnter new password: ");
                setPassword(Main.scanner.next());
                System.out.println("\nPassword successfully changed");
                Main.waitForUser();
                manageBank();
            case 3:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("coming soon...");
                Main.waitForUser();
                manageBank();
            case 4:
                bankDashboard();
            case 0:
                System.exit(1);
        }
    }

    boolean logInToBank() throws IOException {
        System.out.println("\nEnter owner login:");
        String inLogin = Main.scanner.next();
        System.out.println("Enter password:");
        String inPassword = Main.scanner.next();
        if (!Objects.equals(inLogin, this.ownerLogin)) {
            System.out.println("\nLogin incorrect!");
            Main.waitForUser();
            return false;
        } else {
            if (!Objects.equals(inPassword, this.password)) {
                System.out.println("\nPassword incorrect!");
                Main.waitForUser();
                return false;
            }
        }
        return true;
    }

    Bank(String bankName, float bankResources) {

        this.bankName = bankName;
        this.bankResources = bankResources;
        this.creationDate = LocalDate.now();
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
        this.password = password;
    }

    void setBankName(String bankName) {
        this.bankName = bankName;
    }

    void setPassword(String password) {
        this.password = password;
    }
}