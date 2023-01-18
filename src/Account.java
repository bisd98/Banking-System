import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Account {
    String accountNumber;
    String accountType;
    float accountResources = 0;
    LocalDate accountCreationDate;

    static Account creatingAccountMenu(Bank clientBank) throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("Select a new account type:\n" +
                "1. Regular account\n" +
                "2. Savings account\n" +
                "0. Back\n");
        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();
        Main.clearScreen();
        System.out.println(Main.logo);
        switch (choice){
            case 1:
                System.out.println("Enter initial Account resources: ");
                float res = Main.scanner.nextFloat();
                return new Account(accountNumberGenerator(clientBank),
                        "Regular account", res);
            case 2:
                System.out.println("coming soon...");
                Main.waitForUser();
                return null;
            case 0:
                return null;
        }
        return null;
    }

    void accountDashboard(){

    }
    static String accountNumberGenerator(Bank clientBank) {
        long range = 9999999999999999L - 1000000000000000L + 1;
        long individualAccNumber;
        do {
            individualAccNumber = Main.rand.nextLong(range) + 100000000;
        } while (!accountCheckNumber(individualAccNumber, clientBank));
        clientBank.individualNumbers.add(individualAccNumber);
        return ("PL"
                + accountCheckDigitGenerator(Integer.toString(clientBank.bic)
                + Long.toString(individualAccNumber))
                + clientBank.bic + individualAccNumber);
    }

    static boolean accountCheckNumber(long accountGeneratedNumber, Bank clientBank) {
        if (clientBank.individualNumbers.isEmpty()) {
            return true;
        }
        for (Long number : clientBank.individualNumbers) {
            if (accountGeneratedNumber == number) {
                return false;
            }
        }
        return true;
    }

    static String accountCheckDigitGenerator(String accountNumber) {
        int checkSum = calculateCheckSum(accountNumber);
        if (checkSum < 10) {
            return "0" + Integer.toString(checkSum);
        } else {
            return Integer.toString(checkSum);
        }
    }

    static int calculateCheckSum(String accountNumber) {
        String accNrWithPL = accountNumber + "252100";
        BigDecimal bigDecimal = new BigDecimal(accNrWithPL);
        BigDecimal remainder = bigDecimal.remainder(new BigDecimal(97));
        int checkSum = remainder.intValue();
        return 98 - checkSum;
    }

    Account(String accountNumber, String accountType, float accountResources) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountResources = accountResources;
        this.accountCreationDate = LocalDate.now();
    }
}
