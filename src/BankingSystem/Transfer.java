package BankingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Objects;

public class Transfer {
    String transferSender;
    String transferRecipient;
    String transferDescription;
    float transferAmount;
    LocalDate transferDate;

    Transfer(String transferSender, String transferRecipient, String transferDescription,
             float transferAmount, LocalDate transferDate) {
        this.transferSender = transferSender;
        this.transferRecipient = transferRecipient;
        this.transferDescription = transferDescription;
        this.transferAmount = transferAmount;
        this.transferDate = Objects.requireNonNullElseGet(transferDate, LocalDate::now);

    }

    static boolean createTransferMenu(Account senderAccount) throws IOException {
        String recipient;
        float amount;
        while (true) {
            try {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nAccount number: " + senderAccount.accountNumber
                        + "\nResources: " + Main.df.format(senderAccount.accountResources) + "$");
                System.out.print("\nRecipient's bank account number: ");
                recipient = Main.scanner.next();
                if (!recipient.matches("^PL[0-9]{26}$")) {
                    throw new IllegalArgumentException();
                }
                if (recipient.equals(senderAccount.accountNumber)){
                    System.out.println("You cannot send a transfer to the same account number");
                    return false;
                }
                System.out.print("\nTransfer amount: ");
                amount = Main.scanner.nextFloat();
                if (checkAccountResources(senderAccount, amount)) {
                    System.out.println("\nNot enough funds in the account");
                    return false;
                } else if (amount <= 0) {
                    System.out.println("\nInvalid amount of resources");
                    return false;
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid format, please try again");
                Main.waitForUser();
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        Account recipientAccount = checkRecipientAccount(recipient);
        if (Objects.isNull(recipientAccount)) {
            return false;
        } else {
            System.out.print("\nDescription of the transfer: ");
            String desc = "";
            Main.scanner.nextLine();
            desc += Main.scanner.nextLine();
            Transfer newTransfer = new Transfer(senderAccount.accountNumber,
                    recipientAccount.accountNumber, desc, amount, null);
            newTransfer.resourcesTransfer(senderAccount, recipientAccount);

            Main.bankDataBase.insertTransfer(newTransfer.transferSender, newTransfer.transferRecipient,
                    newTransfer.transferDescription, newTransfer.transferAmount,
                    java.sql.Date.valueOf(newTransfer.transferDate));

            senderAccount.accountTransfers.add(newTransfer);
            recipientAccount.accountTransfers.add(newTransfer);
            if (Objects.equals(recipientAccount.accountType, "Credit account")) {
                if (recipientAccount.accountResources == 0) {
                    Objects.requireNonNull(checkRecipient(recipientAccount)).clientBankAccounts.
                            remove(recipientAccount);
                } else if (recipientAccount.accountResources > 0) {
                    Transfer overpayReturn = new Transfer(recipientAccount.accountNumber,
                            senderAccount.accountNumber, "Overpay return",
                            recipientAccount.accountResources, null);
                    overpayReturn.resourcesTransfer(recipientAccount, senderAccount);

                    Main.bankDataBase.insertTransfer(overpayReturn.transferSender, overpayReturn.transferRecipient,
                            overpayReturn.transferDescription, overpayReturn.transferAmount,
                            java.sql.Date.valueOf(overpayReturn.transferDate));
                    //WPROWADZENIE ZWROTU NADPŁATY DO BAZY DANYCH

                    senderAccount.accountTransfers.add(overpayReturn);
                    Objects.requireNonNull(checkRecipient(recipientAccount)).clientBankAccounts.
                            remove(recipientAccount);
                } else {
                    System.out.println("\nRemaining to repay the credit: "
                            + -(recipientAccount.accountResources) + "$");
                }
            }
        }
        return true;
    }

    static boolean checkAccountResources(Account senderAccount, float amount) {
        return (senderAccount.accountResources < amount);
    }

    static Account checkRecipientAccount(String recipientAccountNumber) {
        int bicNumber = Integer.parseInt(recipientAccountNumber.substring(4, 12));
        for (Bank anyBank : Main.banks) {
            if (anyBank.bic == bicNumber) {
                for (Client anyClient : anyBank.clients) {
                    for (Account anyAccount : anyClient.clientBankAccounts) {
                        if (recipientAccountNumber.equals(anyAccount.accountNumber)) {
                            return anyAccount;
                        }
                    }
                }
            }
        }
        System.out.println("\nWrong bank account number");
        return null;
    }

    static Client checkRecipient(Account recipientAccount) {
        for (Bank anyBank : Main.banks) {
            for (Client anyClient : anyBank.clients) {
                for (Account anyAccount : anyClient.clientBankAccounts) {
                    if (anyAccount.equals(recipientAccount)) {
                        return anyClient;
                    }
                }
            }
        }
        System.out.println("\nWrong client");
        return null;
    }

    void resourcesTransfer(Account sender, Account recipient) {
        sender.accountResources -= this.transferAmount;
        recipient.accountResources += this.transferAmount;
        if (Objects.equals(recipient.accountType, "Credit account")) {
            recipient.accountBank.bankResources += this.transferAmount;
        }
    }
}
