package BankingSystem;

import java.sql.*;
import java.util.ArrayList;

public class BankDB {
    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    static final String DB_URL = "jdbc:sqlite:BankingSystem.db";
    static Connection conn;
    static Statement stat;

    BankDB() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("No driver for JDBC");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Database connection problem");
            e.printStackTrace();
        }
        createTables();
    }

    public boolean createTables() {
        String createBankTable =
                "CREATE TABLE IF NOT EXISTS `Bank` ("
                        + "`OwnerID` integer PRIMARY KEY,"
                        + "`BankName`varchar(255),"
                        + "`BankCreationDate`date,"
                        + "`Resources`real,"
                        + "`OwnerPassword`varchar(255),"
                        + "`BIC`integer,"
                        + "`BankInterestRate`real,"
                        + "`BankCreditInterestRate`real);";
        String createIndividualClientNumbersTable =
                "CREATE TABLE IF NOT EXISTS `IndividualClientNumbers`("
                        + "`IndividualClientNumber`long PRIMARY KEY,"
                        + "`OwnerID`integer,"
                        + "FOREIGN KEY (OwnerID) REFERENCES Bank(OwnerID));";
        String createIndividualCardNumbersTable =
                "CREATE TABLE IF NOT EXISTS `IndividualCardNumbers`("
                        + "`IndividualCardNumber`integer PRIMARY KEY,"
                        + "`OwnerID`integer,"
                        + "FOREIGN KEY (OwnerID) REFERENCES Bank(OwnerID));";
        String createClientTable =
                "CREATE TABLE IF NOT EXISTS `Client`("
                        + "`ClientID`integer PRIMARY KEY,"
                        + "`PersonalID`varchar(255),"
                        + "`ClientPassword`varchar(255),"
                        + "`ClientJoinDate`date,"
                        + "`OwnerID`integer,"
                        + "FOREIGN KEY (PersonalID) REFERENCES PersonalData(PersonalID),"
                        + "FOREIGN KEY (OwnerID) REFERENCES Bank(OwnerID));";
        String createPersonalDataTable =
                "CREATE TABLE IF NOT EXISTS `PersonalData`("
                        + "`PersonalID`varchar(255) PRIMARY KEY,"
                        + "`Name`varchar(255),"
                        + "`Surname`varchar(255),"
                        + "`DateOfBirth`date,"
                        + "`Email`varchar(255),"
                        + "`PhoneNumber`integer);";
        String createAccountTable =
                "CREATE TABLE IF NOT EXISTS `Account`("
                        + "`AccountNumber`varchar(255) PRIMARY KEY,"
                        + "`ClientID`integer,"
                        + "`OwnerID`integer,"
                        + "`AccountType`varchar(255),"
                        + "`AccountResources`real,"
                        + "`AccountCreationDate`date,"
                        + "`CardNumber`varchar(255) NULL,"
                        + "FOREIGN KEY (ClientID) REFERENCES Client(ClientID),"
                        + "FOREIGN KEY (OwnerID) REFERENCES Bank(OwnerID),"
                        + "FOREIGN KEY (CardNumber) REFERENCES Card(CardNumber));";
        String createCardTable =
                "CREATE TABLE IF NOT EXISTS Card("
                        + "CardNumber varchar(255) PRIMARY KEY,"
                        + "CardType varchar(255),"
                        + "CardPIN integer);";
        String createTransferTable =
                "CREATE TABLE IF NOT EXISTS Transfer("
                        + "TransferID integer PRIMARY KEY AUTOINCREMENT,"
                        + "TransferSender varchar(255),"
                        + "TransferRecipient varchar(255),"
                        + "TransferDescription varchar(255),"
                        + "TransferAmount real,"
                        + "TransferDate date);";

        try {
            stat.execute(createBankTable);
            stat.execute(createIndividualClientNumbersTable);
            stat.execute(createIndividualCardNumbersTable);
            stat.execute(createClientTable);
            stat.execute(createPersonalDataTable);
            stat.execute(createAccountTable);
            stat.execute(createCardTable);
            stat.execute(createTransferTable);

        } catch (SQLException e) {
            System.err.println("Error when creating tables");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void insertBank(int ownerID, String bankName, Date bankCreationDate, float resources,
                    String ownerPassword, int bic, float bankInterestRate, float bankCreditInterestRate) {
        String sql = "INSERT INTO Bank VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ownerID);
            statement.setString(2, bankName);
            statement.setDate(3, bankCreationDate);
            statement.setFloat(4, resources);
            statement.setString(5, ownerPassword);
            statement.setInt(6, bic);
            statement.setFloat(7, bankInterestRate);
            statement.setFloat(8, bankCreditInterestRate);

            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void insertClient(int clientId, String personalID, String clientPassword, Date clientJoinDate, int ownerID) {
        String sql = "INSERT INTO Client VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setString(2, personalID);
            statement.setString(3, clientPassword);
            statement.setDate(4, clientJoinDate);
            statement.setInt(5, ownerID);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }


     void insertAccount(String accountNumber, int clientId, int ownerID, String accountType,
                        float accountResources, Date accountCreationDate, String cardId) {
        String sql = "INSERT INTO Account VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setInt(2, clientId);
            statement.setInt(3, ownerID);
            statement.setString(4, accountType);
            statement.setFloat(5, accountResources);
            statement.setDate(6, accountCreationDate);
            statement.setObject(7, cardId, Types.INTEGER);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void insertPersonalData(String personalID, String name, String surname,
                            Date dateOfBirth, String email, int phoneNumber) {
        String sql = "INSERT INTO PersonalData VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, personalID);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setDate(4, dateOfBirth);
            statement.setString(5, email);
            statement.setInt(6, phoneNumber);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }


    void insertTransfer(String transferSender, String transferRecipient, String transferDescription, float transferAmount, Date transferDate) {
        String sql = "INSERT INTO Transfer (TransferSender, TransferRecipient, TransferDescription, TransferAmount, TransferDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, transferSender);
            statement.setString(2, transferRecipient);
            statement.setString(3, transferDescription);
            statement.setFloat(4, transferAmount);
            statement.setDate(5, transferDate);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void insertCard(String cardNumber, String cardType, int cardPin) {
        String sql = "INSERT INTO Card (CardNumber, CardType, CardPIN) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            statement.setString(2, cardType);
            statement.setInt(3, cardPin);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void insertIndividualClientNumber(long individualClientNumber, int ownerID) {
        String sql = "INSERT INTO IndividualClientNumbers VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, individualClientNumber);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void insertIndividualCardNumber(int individualCardNumber, int ownerID) {
        String sql = "INSERT INTO IndividualCardNumbers VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, individualCardNumber);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error");
        }
    }

    void addCardToAccount(String accountNumber, String cardNumber){
        String sql = "UPDATE Account SET CardNumber = ? WHERE AccountNumber = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Bank> selectBanks() {
        ArrayList<Bank> loadedBanks = new ArrayList<>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM Bank");
            while(result.next()) {
                Bank loadedBank = new Bank(result.getString("BankName"),
                        result.getFloat("Resources"),
                        result.getFloat("BankInterestRate"),
                        result.getFloat("BankCreditInterestRate"),
                        result.getDate("BankCreationDate").toLocalDate(),
                        result.getInt("BIC"));
                loadedBank.ownerID = result.getInt("OwnerID");
                loadedBank.ownerPassword = result.getString("OwnerPassword");
                loadedBank.clients = selectClients(loadedBank.ownerID, loadedBank);
                loadedBank.individualClientNumbers = selectIndividualClientNumbers(loadedBank.ownerID);
                loadedBank.individualCardNumbers = selectIndividualCardNumbers(loadedBank.ownerID);
                loadedBanks.add(loadedBank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedBanks;
    }

    ArrayList<Long> selectIndividualClientNumbers (int ownerID){
        ArrayList<Long> loadedIndividualClientNumbers = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM IndividualClientNumbers WHERE OwnerID = ?");
            statement.setInt(1, ownerID);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                loadedIndividualClientNumbers.add(result.getLong("IndividualClientNumber"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedIndividualClientNumbers;
    }

    ArrayList<Integer> selectIndividualCardNumbers (int ownerID){
        ArrayList<Integer> loadedIndividualCardNumbers = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM IndividualCardNumbers WHERE OwnerID = ?");
            statement.setInt(1, ownerID);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                loadedIndividualCardNumbers.add(result.getInt("IndividualClientNumber"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedIndividualCardNumbers;
    }

    ArrayList<Client> selectClients(int ownerID, Bank clientBank) {
        ArrayList<Client> loadedClients = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Client WHERE OwnerID = ?");
            statement.setInt(1, ownerID);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Client loadedClient = new Client(
                        result.getInt("ClientID"),
                        selectPersonalData(result.getString("PersonalID")),
                        0,
                        result.getDate("ClientJoinDate").toLocalDate());
                loadedClient.clientPassword = result.getString("ClientPassword");
                loadedClient.clientBank = clientBank;
                loadedClient.clientBankAccounts = selectAccounts(loadedClient.clientID, clientBank);
                loadedClients.add(loadedClient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedClients;
    }

    PersonalData selectPersonalData(String personalID){
        PersonalData loadedPersonalData;
        try {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM PersonalData WHERE PersonalID = ?");
                statement.setString(1, personalID);
                ResultSet result = statement.executeQuery();
                loadedPersonalData = new PersonalData(
                        result.getString("Name"),
                        result.getString("Surname"),
                        result.getString("PersonalID"),
                        result.getDate("DateOfBirth").toLocalDate(),
                        result.getString("Email"),
                        result.getInt("PhoneNumber"));
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        PersonalData.personalIDNumbers.add(loadedPersonalData.personalID);
        return loadedPersonalData;
    }

    ArrayList<Account> selectAccounts(int clientID, Bank clientBank) {
        ArrayList<Account> loadedAccounts = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Account WHERE ClientID = ?");
            statement.setInt(1, clientID);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Account loadedAccount = new Account(
                        result.getString("AccountNumber"),
                        result.getString("AccountType"),
                        result.getFloat("AccountResources"),
                        clientBank,
                        result.getDate("AccountCreationDate").toLocalDate()
                );
                loadedAccount.accountTransfers = selectTransfers(loadedAccount.accountNumber);
                loadedAccount.accountCard = selectCard(result.getString("CardNumber"));
                loadedAccounts.add(loadedAccount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedAccounts;
    }

    ArrayList<Transfer> selectTransfers(String accountNumber){
        ArrayList<Transfer> loadedTransfers = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Transfer " +
                    "WHERE TransferSender = ? OR TransferRecipient = ? ORDER BY TransferDate DESC");
            statement.setString(1, accountNumber);
            statement.setString(2, accountNumber);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                Transfer loadedTransfer = new Transfer(
                        result.getString("TransferSender"),
                        result.getString("TransferRecipient"),
                        result.getString("TransferDescription"),
                        result.getFloat("TransferAmount"),
                        result.getDate("TransferDate").toLocalDate()
                );
                loadedTransfers.add(loadedTransfer);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedTransfers;
    }

    Card selectCard(String cardNumber){
        Card loadedCard;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Card WHERE CardNumber = ?");
            statement.setString(1, cardNumber);
            ResultSet result = statement.executeQuery();
            loadedCard = new Card(
                    result.getString("CardType"),
                    result.getString("CardNumber"),
                    result.getInt("CardPIN")
            );
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return loadedCard;
    }

    void changeBankName(int ownerID, String newName){
        String sql = "UPDATE Bank SET BankName = ? WHERE OwnerID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeBankInterestRate(int ownerID, float newInterestRate){
        String sql = "UPDATE Bank SET BankInterestRate = ? WHERE OwnerID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setFloat(1, newInterestRate);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeBankCreditInterestRate(int ownerID, float newCreditInterestRate){
        String sql = "UPDATE Bank SET BankCreditInterestRate = ? WHERE OwnerID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setFloat(1, newCreditInterestRate);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeOwnerPassword(int ownerID, String newPassword){
        String sql = "UPDATE Bank SET OwnerPassword = ? WHERE OwnerID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setInt(2, ownerID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeClientEmail(String personalID, String newEmail){
        String sql = "UPDATE PersonalData SET Email = ? WHERE PersonalID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newEmail);
            statement.setString(2, personalID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeClientPhone(String personalID, int newPhone){
        String sql = "UPDATE PersonalData SET PhoneNumber = ? WHERE PersonalID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, newPhone);
            statement.setString(2, personalID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeCardPin(String cardNumber, int newPin){
        String sql = "UPDATE Card SET CardPIN = ? WHERE CardNumber = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, newPin);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
