package Dao;

import DB.DBConnection;
import Model.AdditionalService;
import Model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceDAO {
    private static ReservationServiceDAO instance;
    private final Connection connection;
    private final AdditionalServiceDAO additionalServiceDAO;

    private ReservationServiceDAO() {
        this.connection = DBConnection.getInstance().getConnection();
        this.additionalServiceDAO = AdditionalServiceDAO.getInstance();
    }

    public static synchronized ReservationServiceDAO getInstance() {
        if (instance == null) {
            instance = new ReservationServiceDAO();
        }
        return instance;
    }

    public boolean addServiceToReservation(int reservationId, int serviceId, int nr_days, double unitPrice) {
        String sql = "INSERT INTO reservation_services (reservation_id, service_id, nr_days, unit_price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.setInt(2, serviceId);
            stmt.setInt(3, nr_days);
            stmt.setDouble(4, unitPrice);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeServiceFromReservation(int reservationId, int serviceId) {
        String sql = "DELETE FROM reservation_services WHERE reservation_id = ? AND service_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.setInt(2, serviceId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AdditionalService> getServicesForReservation(int reservationId) {
        List<AdditionalService> services = new ArrayList<>();
        String sql = """
            SELECT adt.* FROM additional_services adt
            INNER JOIN reservation_services rs ON adt.id = rs.service_id
            WHERE rs.reservation_id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                additionalServiceDAO.findById(rs.getInt("id"))
                        .ifPresent(services::add);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public double getTotalServiceCostForReservation(int reservationId) {
        String sql = "SELECT SUM(nr_days * unit_price) as total FROM reservation_services WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}