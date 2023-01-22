import java.io.IOException;
import java.time.LocalDate;

public class PersonalData {
    String name;
    String surname;
    Long personalID;
    LocalDate dateOfBirth;
    String email;
    Long phoneNumber;

    PersonalData(String name, String surname, Long personalID,
                 LocalDate dateOfBirth, String email, Long phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.personalID = personalID;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    static PersonalData personalDataForm() {
        System.out.print("\nEnter your name: ");
        String userName = Main.scanner.next();
        System.out.print("Enter your surname: ");
        String userSurname = Main.scanner.next();
        System.out.print("Enter your Personal ID Number: ");
        Long userPersonalID = Main.scanner.nextLong();
        System.out.print("Enter your date of birth (yyyy-MM-d): ");
        LocalDate userBirthDate = LocalDate.parse(Main.scanner.next());
        System.out.print("Enter your email address: ");
        String userEmail = Main.scanner.next();
        System.out.print("Enter your phone number: ");
        Long userPhone = Main.scanner.nextLong();
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

        System.out.print("choice: ");
        int choice = Main.scanner.nextInt();

        switch (choice) {
            case 1:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nYour email address: " + this.email);
                System.out.print("\nEnter new email address: ");
                setEmail(Main.scanner.next());
                System.out.println("\nEmail address has been successfully changed");
                Main.waitForUser();
                managePersonalData();
                break;
            case 2:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nYour phone number: " + this.phoneNumber);
                System.out.print("\nEnter new phone number: ");
                setPhoneNumber(Main.scanner.nextLong());
                System.out.println("\nPhone number has been successfully changed");
                Main.waitForUser();
                managePersonalData();
                break;
            case 3:
                return true;
            case 0:
                System.exit(1);
        }
        return true;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
