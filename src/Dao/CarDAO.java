package Dao;

import DB.DBConnection;
import Model.Car;
import Model.CarSpecs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDAO implements GenericDAO<Car, Integer> {
    private static CarDAO instance;
    private final Connection connection;
    private final CarSpecsDAO carSpecsDAO;

    private CarDAO() {
        this.connection = DBConnection.getInstance().getConnection();
        this.carSpecsDAO = CarSpecsDAO.getInstance();
    }

    public static synchronized CarDAO getInstance() {
        if (instance == null) {
            instance = new CarDAO();
        }
        return instance;
    }

    @Override
    public Car save(Car car) {
        try {
            connection.setAutoCommit(false); // Începe tranzacția

            // Salvează specificațiile mai întâi
            CarSpecs savedSpecs = carSpecsDAO.save(car.getSpecs());
            if (savedSpecs == null) {
                connection.rollback();
                return null;
            }

            // Salvează mașina cu referința către specificații
            String sql = "INSERT INTO cars (brand, model, year, price_per_day, car_specs_id) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, car.getBrand());
                stmt.setString(2, car.getModel());
                stmt.setInt(3, car.getYear());
                stmt.setDouble(4, car.getPricePerDay());
                stmt.setInt(5, savedSpecs.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new SQLException("Creating car failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        connection.commit(); // Confirmă tranzacția

                        return new Car(newId, car.getBrand(), car.getModel(),
                                car.getYear(), car.getPricePerDay(), savedSpecs);
                    }
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Resetează auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Car update(Car car) {
        String sql = "UPDATE cars SET brand = ?, model = ?, year = ?, price_per_day = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setDouble(4, car.getPricePerDay());
            stmt.setInt(5, car.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                if (car.getSpecs() != null) {
                    carSpecsDAO.update(car.getSpecs());
                }
                return car;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        try {
            connection.setAutoCommit(false);

            // Găsește car_specs_id înainte de ștergere
            String findSpecsId = "SELECT car_specs_id FROM cars WHERE id = ?";
            Integer specsId = null;
            try (PreparedStatement stmt = connection.prepareStatement(findSpecsId)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    specsId = rs.getInt("car_specs_id");
                }
            }

            // Șterge mașina
            String deleteCar = "DELETE FROM cars WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteCar)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0 && specsId != null) {
                    // Șterge și specificațiile
                    carSpecsDAO.delete(specsId);
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Car> findById(Integer id) {
        String sql = "SELECT c.*, cs.* FROM cars c LEFT JOIN car_specs cs ON c.car_specs_id = cs.id WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CarSpecs specs = new CarSpecs(
                        rs.getString("body_type"),
                        rs.getString("fuel_type"),
                        rs.getDouble("engine_capacity"),
                        rs.getInt("seat_count"),
                        rs.getString("gearbox"),
                        rs.getString("drivetrain")
                );
                specs.setId(rs.getInt("car_specs_id"));

                return Optional.of(new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price_per_day"),
                        specs
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.*, cs.* FROM cars c LEFT JOIN car_specs cs ON c.car_specs_id = cs.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CarSpecs specs = new CarSpecs(
                        rs.getString("body_type"),
                        rs.getString("fuel_type"),
                        rs.getDouble("engine_capacity"),
                        rs.getInt("seat_count"),
                        rs.getString("gearbox"),
                        rs.getString("drivetrain")
                );
                specs.setId(rs.getInt("car_specs_id"));

                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price_per_day"),
                        specs
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> findAvailableCars(Timestamp startDate, Timestamp endDate) {
        String sql = """
            SELECT c.*, cs.* FROM cars c 
            LEFT JOIN car_specs cs ON c.car_specs_id = cs.id
            WHERE c.id NOT IN (
                SELECT DISTINCT r.car_id FROM reservations r 
                WHERE r.status = 'ACTIVE' AND NOT (? >= r.end_date OR ? <= r.start_date)
            )
        """;

        List<Car> availableCars = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, endDate);
            stmt.setTimestamp(2, startDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CarSpecs specs = new CarSpecs(
                        rs.getString("body_type"),
                        rs.getString("fuel_type"),
                        rs.getDouble("engine_capacity"),
                        rs.getInt("seat_count"),
                        rs.getString("gearbox"),
                        rs.getString("drivetrain")
                );
                specs.setId(rs.getInt("car_specs_id"));

                availableCars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price_per_day"),
                        specs
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
    }
}