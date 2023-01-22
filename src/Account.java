import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Account {
    String accountNumber;
    String accountType;
    float accountResources;
    LocalDate accountCreationDate;
    Bank accountBank;
    Card accountCard;
    ArrayList<Transfer> accountTransfers;

    Account(String accountNumber, String accountType, float accountResources,
            Bank accountBank) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountResources = accountResources;
        this.accountCreationDate = LocalDate.now();
        this.accountTransfers = new ArrayList<>();
        this.accountBank = accountBank;
        this.accountCard = null;
    }

    static Account creatingAccountMenu(Bank clientBank) {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nSelect a new account type:\n" +
                "1. Regular account\n" +
                "2. Savings account (monthly interest - "
                + clientBank.bankInterestRate * 100 + "%)\n" +
                "0. Back");
        System.out.print("\nchoice: ");
        int choice = Main.scanner.nextInt();
        Main.clearScreen();
        System.out.println(Main.logo);
        switch (choice) {
            case 1:
                System.out.print("\nEnter initial Account resources: ");
                float res = Main.scanner.nextFloat();
                return new Account(accountNumberGenerator(clientBank),
                        "Regular account", res, clientBank);
            case 2:
                System.out.print("\nEnter initial Account resources: ");
                float resSavings = Main.scanner.nextFloat();
                return new SavingsAccount(accountNumberGenerator(clientBank),
                        "Savings account", clientBank,
                        resSavings, clientBank.bankInterestRate);
            case 0:
                return null;
        }
        return null;
    }

    static String accountNumberGenerator(Bank clientBank) {
        long range = 9999999999999999L - 1000000000000000L + 1;
        long individualAccNumber;
        do {
            individualAccNumber = Main.rand.nextLong(range) + 100000000;
        } while (!accountCheckNumber(individualAccNumber, clientBank));
        clientBank.individualClientNumbers.add(individualAccNumber);
        return ("PL"
                + accountCheckDigitGenerator((clientBank.bic)
                + Long.toString(individualAccNumber))
                + clientBank.bic + individualAccNumber);
    }

    void accountDashboard() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.print(("\nAccount number: "
                + this.accountNumber + "\nType: " + this.accountType
                + "\nResources: " + Main.df.format(this.accountResources) + "$"
                + "\nCreation date: " + this.accountCreationDate));
        if (!Objects.isNull(this.accountCard)){
            System.out.print("\nAccount card number: " + this.accountCard.cardNumber);
        }
        System.out.print("\n\nSelect:" +
                "\n1. Show transfers\n" +
                "2. Export transfers\n");
        if (Objects.isNull(this.accountCard) && !Objects.equals(this.accountType, "Credit account")) {
            System.out.println("4. Create a card for the account");
        }else if(!Objects.equals(this.accountType, "Credit account")){
            System.out.println("4. Change card PIN");
        }
        System.out.println("0. Back");
        System.out.print("\nchoice: ");
        int choice = Main.scanner.nextInt();
        Main.clearScreen();
        System.out.println(Main.logo);
        switch (choice) {
            case 1:
                if (this.accountTransfers.size() == 0) {
                    System.out.println("\nThere are no transfers yet");
                    Main.waitForUser();
                    accountDashboard();
                    break;
                }
                for (Transfer anyTransfer : this.accountTransfers) {
                    System.out.println("\nTransfer date: " + anyTransfer.transferDate
                            + "\nTransfer sender: " + anyTransfer.transferSender
                            + "\nRecipient of the transfer: " + anyTransfer.transferRecipient
                            + "\nResources: " + Main.df.format(anyTransfer.transferAmount)
                            + "\nDescription: " + anyTransfer.transferDescription);
                }
                Main.waitForUser();
                accountDashboard();
                break;
            case 2:
                System.out.println("coming soon...");
                Main.waitForUser();
                accountDashboard();
                break;
            case 3:
                if (Objects.isNull(this.accountCard)) {
                    this.accountCard = Card.creatingCardMenu(this.accountBank);
                }else {
                    System.out.print("\nCurrent card PIN: " + this.accountCard.cardPIN
                    + "\n\nEnter a new card pin: ");
                    this.accountCard.setCardPIN(Main.scanner.nextInt());
                    System.out.println("Card pin changed successfully");
                }
                Main.waitForUser();
                accountDashboard();
                break;
            case 0:
                break;
        }
    }

    static boolean accountCheckNumber(long accountGeneratedNumber, Bank clientBank) {
        if (clientBank.individualClientNumbers.isEmpty()) {
            return true;
        }
        for (Long number : clientBank.individualClientNumbers) {
            if (accountGeneratedNumber == number) {
                return false;
            }
        }
        return true;
    }

    static String accountCheckDigitGenerator(String accountNumber) {
        int checkSum = calculateCheckSum(accountNumber);
        if (checkSum < 10) {
            return "0" + checkSum;
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
}