import java.io.IOException;
import java.util.InputMismatchException;

public class Card {
    String cardType;
    String cardNumber;
    int cardPIN;

    Card(String cardType, String cardNumber, int cardPIN) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
    }

    static Card creatingCardMenu(Bank clientBank) throws IOException {
        String cardType = null;
        int choice;
        while (true) {
            Main.clearScreen();
            System.out.println(Main.logo);
            System.out.println("\nSelect card type:" +
                    "\n1. Visa" +
                    "\n2. MasterCard\n");
            try {
                System.out.print("choice: ");
                choice = Main.scanner.nextInt();
                if (!(choice == 1 || choice == 2)) {
                    System.out.println("Wrong choice, please try again");
                    Main.waitForUser();
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        switch (choice) {
            case 1 -> cardType = "Visa";
            case 2 -> cardType = "MasterCard";
        }
        String cardNumber = Long.toString(cardNumberGenerator(clientBank, choice + 4));
        int cardPIN;
        while (true) {
            try {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nYour card number: " + cardNumber);
                System.out.print("\nSet card PIN: ");
                cardPIN = Main.scanner.nextInt();
                if (Integer.toString(cardPIN).length() != 4) {
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
        System.out.println("\nThe card has been successfully created");
        return new Card(cardType, cardNumber, cardPIN);
    }

    static Long cardNumberGenerator(Bank clientBank, int industryID) {
        int range = 999999999 - 100000000 + 1;
        int cardNumberGenerated;
        do {
            cardNumberGenerated = Main.rand.nextInt(range) + 100000000;
        } while (!cardCheckNumber(cardNumberGenerated, clientBank));
        clientBank.individualCardNumbers.add(cardNumberGenerated);
        String numberWithoutControlNr = industryID
                + Integer.toString(clientBank.bic).substring(0, 5)
                + (cardNumberGenerated);
        return Long.parseLong(numberWithoutControlNr
                + luhnAlgorithm(numberWithoutControlNr));
    }

    static String luhnAlgorithm(String cardNumber) {
        cardNumber += "0";
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        return Integer.toString(checkDigit);
    }

    static boolean cardCheckNumber(int clientGeneratedID, Bank clientBank) {
        if (clientBank.individualCardNumbers.isEmpty()) {
            return true;
        }
        for (int anyCardNumber : clientBank.individualCardNumbers) {
            if (clientGeneratedID == anyCardNumber) {
                return false;
            }
        }
        return true;
    }

    public void setCardPIN(int cardPIN) {
        this.cardPIN = cardPIN;
    }
}
