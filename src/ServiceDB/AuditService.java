package ServiceDB;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private final String CSV_FILE_PATH = "audit.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {
        initializeCSVFile();
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    private void initializeCSVFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            java.io.File file = new java.io.File(CSV_FILE_PATH);
            if (file.length() == 0) {
                writer.write("nume_actiune,timestamp\n");
            }
        } catch (IOException e) {
            System.err.println("Error initializing audit CSV file: " + e.getMessage());
        }
    }

    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(formatter);

        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.write(String.format("%s,%s\n", actionName, timestamp));
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to audit log: " + e.getMessage());
        }
    }


    public void logClientRegistration(String clientName) {
        logAction("REGISTER_CLIENT_" + clientName);
    }

    public void logClientRemoval(String clientName) {
        logAction("REMOVE_CLIENT_" + clientName);
    }

    public void logCarAddition(String carBrand, String carModel) {
        logAction("ADD_CAR_" + carBrand + "_" + carModel);
    }

    public void logCarRemoval(int carId) {
        logAction("REMOVE_CAR_ID_" + carId);
    }

    public void logReservationCreation(int reservationId) {
        logAction("CREATE_RESERVATION_ID_" + reservationId);
    }

    public void logReservationCancellation(int reservationId) {
        logAction("CANCEL_RESERVATION_ID_" + reservationId);
    }

    public void logClientPromotion(String clientName) {
        logAction("PROMOTE_CLIENT_TO_VIP_" + clientName);
    }

    public void logServiceAddition(String serviceName) {
        logAction("ADD_SERVICE_" + serviceName);
    }

    public void logViewAllClients() {
        logAction("VIEW_ALL_CLIENTS");
    }

    public void logViewVIPClients() {
        logAction("VIEW_VIP_CLIENTS");
    }

    public void logViewAvailableCars() {
        logAction("VIEW_AVAILABLE_CARS");
    }

    public void logViewAllCars() {
        logAction("VIEW_ALL_CARS");
    }

    public void logViewClientReservations(String clientName) {
        logAction("VIEW_CLIENT_RESERVATIONS_" + clientName);
    }

    public void logSearchClient(String clientName) {
        logAction("SEARCH_CLIENT_" + clientName);
    }
}
