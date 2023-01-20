public class Card {
    String cardType;
    String cardNumber;
    int cardPIN;

    Card(String cardType, String cardNumber, int cardPIN){
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
    }

    static Card creatingCardMenu(Bank clientBank){
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nSelect card type:" +
                "\n1. Visa" +
                "\n2. MasterCard\n");
        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();
        String cardType = null;
        switch (choice) {
            case 1 -> cardType = "Visa";
            case 2 -> cardType = "MasterCard";
        }
        Main.clearScreen();
        System.out.println(Main.logo);
        String cardNumber = Long.toString(cardNumberGenerator(clientBank,choice + 4));
        System.out.println("\nYour card number: " + cardNumber);
        System.out.print("\nSet card PIN: ");
        String cardPIN = Main.scanner.next();
        System.out.println("\nThe card has been successfully created");
        return new Card(cardType, cardNumber, Integer.parseInt(cardPIN));
    }
    static Long cardNumberGenerator(Bank clientBank, int industryID){
        int range = 999999999 - 100000000 + 1;
        int cardNumberGenerated;
        do {
            cardNumberGenerated = Main.rand.nextInt(range) + 100000000;
        } while (!cardCheckNumber(cardNumberGenerated, clientBank));
        clientBank.individualCardNumbers.add(cardNumberGenerated);
        String numberWithoutControlNr = Integer.toString(industryID)
                + Integer.toString(clientBank.bic).substring(0, 5)
                + Integer.toString(cardNumberGenerated);
        return Long.parseLong(numberWithoutControlNr
                + luhnAlgorithm(numberWithoutControlNr));
    }

    static String luhnAlgorithm(String cardNumber){
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
}
