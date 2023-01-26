package BankingSystem;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;

public class CsvCreator {

    private static String CSV_FOLDER;

    static void exportTransfersCSV(ArrayList<Transfer> transfers, String cardNumber) throws IOException {
        CSV_FOLDER = "Bank_statements";
        Path directory = Paths.get(CSV_FOLDER);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String CSV_FILE = "Transfers_" + cardNumber + ".csv";
        String[] HEADERS = new String[]{"TransferDate", "TransferAmount", "TransferSender", "TransferRecipient", "TransferDescription"};
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FOLDER, CSV_FILE));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))
        ) {
            for (Transfer transfer : transfers) {
                csvPrinter.printRecord(Arrays.asList(transfer.transferDate, transfer.transferAmount,
                        transfer.transferSender, transfer.transferRecipient, transfer.transferDescription));
                csvPrinter.flush();
            }
        }
    }

    static void exportClientsCSV(ArrayList<Client> clients, String bankName) throws IOException {
        CSV_FOLDER = "Clients_Data";
        Path directory = Paths.get(CSV_FOLDER);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String CSV_FILE = bankName + "_Clients_Data.csv";
        String[] HEADERS = new String[]{"PersonalID", "Name", "Surname", "DateOfBirth", "Email", "PhoneNumber"};
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FOLDER, CSV_FILE));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(HEADERS))
        ) {
            int counter = 0;
            for (Client client : clients) {
                csvPrinter.printRecord(Arrays.asList(client.clientPersonalData.personalID,
                        client.clientPersonalData.name, client.clientPersonalData.surname,
                        client.clientPersonalData.dateOfBirth, client.clientPersonalData.email,
                        client.clientPersonalData.phoneNumber));
                csvPrinter.flush();
                counter +=1;
            }
            System.out.println("\nClients exported: " + counter);
        }
    }


    static void importClientsCsv(Bank importBank, Bank destinationBank) throws IOException {
        String CSV_FILE = "Clients_Data/" + importBank.bankName + "_Clients_Data.csv";
        if(!Files.exists(Paths.get(CSV_FILE))) {
            System.out.println("No Clients exported from this bank");
            return;
        }
        try (
                CSVParser parser = CSVParser.parse(
                        Paths.get(CSV_FILE),
                        Charset.defaultCharset(),
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord()
                )
        ) {
            ArrayList<Client> nonActiveClients = new ArrayList<>();
            int counter = 0;
            for (CSVRecord record : parser) {
                String personalID = record.get("PersonalID");
                String name = record.get("Name");
                String surname = record.get("Surname");
                LocalDate dateOfBirth = LocalDate.parse(record.get("DateOfBirth"));
                String email = record.get("Email");
                int phoneNumber = Integer.parseInt(record.get("PhoneNumber"));
                PersonalData personalData = new PersonalData(name, surname, personalID, dateOfBirth, email, phoneNumber);
                Client importedClient = new Client(Client.clientIDGenerator(), personalData, 0, LocalDate.now());
                importedClient.clientBank = destinationBank;
                importedClient.clientPassword = null;
                destinationBank.clients.add(importedClient);
                nonActiveClients.add(importedClient);
                counter +=1;
            }
            nonActiveClientsCsv(nonActiveClients, destinationBank.bankName);
            System.out.println("\nClients imported: " + counter);
        }
    }

    static void nonActiveClientsCsv(ArrayList<Client> clients, String bankName) throws IOException {
        String CSV_FILE = "Clients_Data/Non-activated_" + bankName + "_Clients.csv";
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ClientID", "personalID", "name", "surname"))
        ) {
            for (Client client : clients) {
                csvPrinter.printRecord(
                        client.clientID,
                        client.clientPersonalData.personalID,
                        client.clientPersonalData.name,
                        client.clientPersonalData.surname
                );
            }
        }
    }
}
