package BankingSystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
            Bank accountBank, LocalDate accountCreationDate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountResources = accountResources;
        this.accountCreationDate = Objects.requireNonNullElseGet(accountCreationDate, LocalDate::now);
        this.accountTransfers = new ArrayList<>();
        this.accountBank = accountBank;
        this.accountCard = null;
    }

    static Account creatingAccountMenu(Bank clientBank) throws IOException {
        int choice;
        while (true) {
            try {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nSelect a new account type:\n" +
                        "1. Regular account\n" +
                        "2. Savings account (monthly interest - "
                        + clientBank.bankInterestRate * 100 + "%)\n" +
                        "0. Back");
                System.out.print("\nchoice: ");
                choice = Main.scanner.nextInt();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong choice, please try again");
                Main.waitForUser();
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        switch (choice) {
            case 1:
                float res;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.print("\nEnter initial Account resources: ");
                        res = Main.scanner.nextFloat();
                        if (res < 0) {
                            throw new IllegalArgumentException();
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }

                return new Account(accountNumberGenerator(clientBank),
                        "Regular account", res, clientBank, null);
            case 2:
                float resSavings;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.print("\nEnter initial Account resources: ");
                        resSavings = Main.scanner.nextFloat();
                        if (resSavings < 0) {
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
                return new SavingsAccount(accountNumberGenerator(clientBank),
                        "Savings account", clientBank,
                        resSavings, clientBank.bankInterestRate);
            case 0:
                return null;
            default: {
                System.out.println("\nWrong choice, please try again");
                Main.waitForUser();
                creatingAccountMenu(clientBank);
                break;
            }
        }
        return null;
    }



    static String accountNumberGenerator(Bank clientBank) {
        long range = 9999999999999999L - 1000000000000000L + 1;
        long individualAccNumber;
        do {
            individualAccNumber = Main.rand.nextLong(range) + 100000000;
        } while (!accountCheckNumber(individualAccNumber, clientBank));

        Main.bankDataBase.insertIndividualClientNumber(individualAccNumber, clientBank.ownerID);

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
        if (!Objects.isNull(this.accountCard)) {
            System.out.print("\nAccount card number: " + this.accountCard.cardNumber);
        }
        System.out.print("\n\nSelect:" +
                "\n1. Show transfers\n" +
                "2. Export transfers\n");
        if (Objects.isNull(this.accountCard) && !Objects.equals(this.accountType, "Credit account")) {
            System.out.println("3. Create a card for the account");
        } else if (!Objects.equals(this.accountType, "Credit account")) {
            System.out.println("3. Change card PIN");
        }
        System.out.println("0. Back");
        int choice;
        while (true) {
            try {
                System.out.print("\nchoice: ");
                choice = Main.scanner.nextInt();
                if (choice == 3 && Objects.equals(this.accountType, "Credit account")) {
                    System.out.println("Wrong choice, please try again");
                    Main.waitForUser();
                    accountDashboard();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
                accountDashboard();
            }
        }
        if (Objects.equals(this.accountType, "Credit account") && choice == 3) {
            choice = 9;
        }

        switch (choice) {
            case 1:
                Main.clearScreen();
                System.out.println(Main.logo);
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

                    Main.bankDataBase.insertCard(this.accountCard.cardNumber,
                            this.accountCard.cardType, this.accountCard.cardPIN);

                    Main.bankDataBase.addCardToAccount(this.accountNumber, this.accountCard.cardNumber);
                } else {
                    int newPIN;
                    while (true) {
                        try {
                            Main.clearScreen();
                            System.out.println(Main.logo);
                            System.out.print("\nCurrent card PIN: " + this.accountCard.cardPIN
                                    + "\n\nEnter a new card pin: ");
                            newPIN = Main.scanner.nextInt();
                            if (Integer.toString(newPIN).length() != 4) {
                                throw new IllegalArgumentException();
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid PIN length, please try again");
                            Main.waitForUser();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid format, please try again");
                            Main.scanner.next();
                            Main.waitForUser();
                        }
                    }

                    this.accountCard.setCardPIN(newPIN);
                    Main.bankDataBase.changeCardPin(this.accountCard.cardNumber, newPIN);
                    System.out.println("Card pin changed successfully");
                }
                Main.waitForUser();
                accountDashboard();
                break;
            case 0:
                break;
            default: {
                System.out.println("\nWrong choice, please try again");
                Main.waitForUser();
                accountDashboard();
            }
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