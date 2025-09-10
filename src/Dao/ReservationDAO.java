package Dao;

import DB.DBConnection;
import Model.Car;
import Model.Client;
import Model.Reservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAO implements GenericDAO<Reservation, Integer> {
    private static ReservationDAO instance;
    private final Connection connection;
    private final ClientDAO clientDAO;
    private final CarDAO carDAO;

    private ReservationDAO() {
        this.connection = DBConnection.getInstance().getConnection();
        this.clientDAO = ClientDAO.getInstance();
        this.carDAO = CarDAO.getInstance();
    }

    public static synchronized ReservationDAO getInstance() {
        if (instance == null) {
            instance = new ReservationDAO();
        }
        return instance;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations (client_id, car_id, start_date, end_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getClient().getId());
            stmt.setInt(2, reservation.getCar().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndDate()));
            stmt.setDouble(5, reservation.getTotalAmount());
            stmt.setString(6, "ACTIVE");

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return new Reservation(newId, reservation.getClient(), reservation.getCar(),
                            reservation.getStartDate(), reservation.getEndDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Reservation update(Reservation reservation) {
        String sql = "UPDATE reservations SET start_date = ?, end_date = ?, total_amount = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(reservation.getStartDate()));
            stmt.setTimestamp(2, Timestamp.valueOf(reservation.getEndDate()));
            stmt.setDouble(3, reservation.getTotalAmount());
            stmt.setString(4, "ACTIVE");
            stmt.setInt(5, reservation.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Reservation> findById(Integer id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Optional<Client> client = clientDAO.findById(rs.getInt("client_id"));
                Optional<Car> car = carDAO.findById(rs.getInt("car_id"));

                if (client.isPresent() && car.isPresent()) {
                    LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                    LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();

                    return Optional.of(new Reservation(
                            rs.getInt("id"),
                            client.get(),
                            car.get(),
                            startDate,
                            endDate
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE status = 'ACTIVE'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Optional<Client> client = clientDAO.findById(rs.getInt("client_id"));
                Optional<Car> car = carDAO.findById(rs.getInt("car_id"));

                if (client.isPresent() && car.isPresent()) {
                    LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                    LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();

                    reservations.add(new Reservation(
                            rs.getInt("id"),
                            client.get(),
                            car.get(),
                            startDate,
                            endDate
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Reservation> findByClientId(Integer clientId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE client_id = ? AND status = 'ACTIVE'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Optional<Client> client = clientDAO.findById(rs.getInt("client_id"));
                Optional<Car> car = carDAO.findById(rs.getInt("car_id"));

                if (client.isPresent() && car.isPresent()) {
                    LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                    LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();

                    reservations.add(new Reservation(
                            rs.getInt("id"),
                            client.get(),
                            car.get(),
                            startDate,
                            endDate
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}