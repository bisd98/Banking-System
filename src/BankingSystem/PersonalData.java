package BankingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;

public class PersonalData {
    String name;
    String surname;
    String personalID;
    LocalDate dateOfBirth;
    String email;
    int phoneNumber;

    static ArrayList<String> personalIDNumbers = new ArrayList<>();

    static boolean checkPersonalID(String enteredPersonalID){
        if (personalIDNumbers.isEmpty()){
            return true;
        }
        for (String personalID : personalIDNumbers){
            if (Objects.equals(enteredPersonalID, personalID)){
                return false;
            }
        }
        return true;
    }

    PersonalData(String name, String surname, String personalID,
                 LocalDate dateOfBirth, String email, int phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.personalID = personalID;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    static PersonalData personalDataForm() throws IOException {
        String userName, userSurname, userEmail;
        String userPersonalID;
        int userPhone;
        LocalDate userBirthDate;
        while (true) {
            try {
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.print("\nEnter your name: ");
                userName = Main.scanner.next();
                if (!userName.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException();
                }
                System.out.print("Enter your surname: ");
                userSurname = Main.scanner.next();
                if (!userSurname.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException();
                }
                System.out.print("Enter your Personal ID Number: ");
                userPersonalID = Main.scanner.next();
                if (userPersonalID.length() != 11) {
                    throw new IllegalArgumentException();
                } else if (!checkPersonalID(userPersonalID)) {
                    System.out.println("Personal ID number found in the database\n\n" +
                            "Personal data loaded...");
                    Main.waitForUser();
                    return Main.bankDataBase.selectPersonalData(userPersonalID);
                }
                System.out.print("Enter your date of birth (yyyy-MM-d): ");
                userBirthDate = LocalDate.parse(Main.scanner.next());
                System.out.print("Enter your email address: ");
                userEmail = Main.scanner.next();
                if (!userEmail.matches("^[\\w!#$%&'*+/=?`{|}~^-]" +
                        "+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                    throw new IllegalArgumentException();
                }
                System.out.print("Enter your phone number: ");
                userPhone = Main.scanner.nextInt();
                if (Long.toString(userPhone).length() != 9) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Invalid format, please try again");
                Main.waitForUser();
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
            }
        }
        personalIDNumbers.add(userPersonalID);
        Main.bankDataBase.insertPersonalData(userPersonalID, userName,
                userSurname, java.sql.Date.valueOf(userBirthDate), userEmail, userPhone);
        return new PersonalData(userName, userSurname, userPersonalID,
                userBirthDate, userEmail, userPhone);
    }

    boolean managePersonalData() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.print("\nName: " + this.name);
        System.out.println("Surname: " + this.surname);
        System.out.println("Personal ID Number: " + this.personalID);
        System.out.println("Date of birth: " + this.dateOfBirth);
        System.out.println("Email address: " + this.email);
        System.out.println("Phone number: " + this.phoneNumber);
        System.out.println(
                "\nSelect:\n" +
                        "1. Change your email address\n" +
                        "2. Change your phone number\n" +
                        "3. Back\n" +
                        "0. Exit\n");

        int choice;
        while (true) {
            try {
                System.out.print("choice: ");
                choice = Main.scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid format, please try again");
                Main.scanner.next();
                Main.waitForUser();
                managePersonalData();
            }
        }
        switch (choice) {
            case 1:
                String newEmail;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nYour email address: " + this.email);
                        System.out.print("\nEnter new email address: ");
                        newEmail = Main.scanner.next();
                        if (!newEmail.matches("^[\\w!#$%&'*+/=?`{|}~^-]" +
                                "+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                            throw new IllegalArgumentException();
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid format, please try again");
                        Main.waitForUser();
                    }
                }
                setEmail(newEmail);
                Main.bankDataBase.changeClientEmail(this.personalID, newEmail);
                System.out.println("\nEmail address has been successfully changed");
                Main.waitForUser();
                managePersonalData();
                break;
            case 2:
                int newPhone;
                while (true) {
                    try {
                        Main.clearScreen();
                        System.out.println(Main.logo);
                        System.out.println("\nYour phone number: " + this.phoneNumber);
                        System.out.print("\nEnter new phone number: ");
                        newPhone = Main.scanner.nextInt();
                        if (Integer.toString(newPhone).length() != 9) {
                            throw new IllegalArgumentException();
                        }
                        break;
                    }catch (IllegalArgumentException e) {
                        System.out.println("Invalid format, please try again");
                        Main.waitForUser();
                    }catch (InputMismatchException e){
                        System.out.println("Invalid format, please try again");
                        Main.scanner.next();
                        Main.waitForUser();
                    }
                }
                setPhoneNumber(newPhone);
                Main.bankDataBase.changeClientPhone(this.personalID, newPhone);
                System.out.println("\nPhone number has been successfully changed");
                Main.waitForUser();
                managePersonalData();
                break;
            case 3:
                break;
            case 0:
                System.exit(1);
            default: {
                System.out.println("Wrong choice, please try again");
                Main.waitForUser();
                managePersonalData();
            }
        }
        return true;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
