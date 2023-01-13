import java.io.IOException;
import java.time.LocalDate;

public class PersonalData {
    String name;
    String surname;
    int personalID;
    LocalDate dateOfBirth;
    String email;
    int phoneNumber;

    static PersonalData personalDataForm(){
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nEnter your name: ");
        String userName = Main.scanner.next();
        System.out.println("Enter your surname: ");
        String userSurname = Main.scanner.next();
        System.out.println("Enter your Personal ID Number: ");
        int userPersonalID = Main.scanner.nextInt();
        System.out.println("Enter your date of birth (yyyy-MM-d): ");
        LocalDate userBirthDate = LocalDate.parse(Main.scanner.next());
        System.out.println("Enter your email address: ");
        String userEmail = Main.scanner.next();
        System.out.println("Enter your phone number: ");
        int userPhone = Main.scanner.nextInt();
        return new PersonalData(userName, userSurname, userPersonalID,
                userBirthDate, userEmail, userPhone);
    }

    boolean managePersonalData() throws IOException {
        Main.clearScreen();
        System.out.println(Main.logo);
        System.out.println("\nName: " + this.name);
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
                System.out.println("\nEnter new email address: ");
                setEmail(Main.scanner.next());
                System.out.println("\nEmail address has been successfully changed");
                Main.waitForUser();
                managePersonalData();
            case 2:
                Main.clearScreen();
                System.out.println(Main.logo);
                System.out.println("\nYour phone number: " + this.email);
                System.out.println("\nEnter new phone number: ");
                setPhoneNumber(Main.scanner.nextInt());
                System.out.println("\nPhone number has been successfully changed");
                Main.waitForUser();
                managePersonalData();
            case 3:
                return true;
            case 0:
                System.exit(1);
        }
        return true;
    }
    PersonalData(String name, String surname, int personalID,
                 LocalDate dateOfBirth, String email, int phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.personalID = personalID;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPersonalID() {
        return personalID;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
