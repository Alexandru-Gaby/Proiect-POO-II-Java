package Dao;

import DB.DBConnection;
import Model.CarSpecs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarSpecsDAO implements GenericDAO<CarSpecs, Integer> {
    private static CarSpecsDAO instance;
    private final Connection connection;

    private CarSpecsDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static synchronized CarSpecsDAO getInstance() {
        if (instance == null) {
            instance = new CarSpecsDAO();
        }
        return instance;
    }

    @Override
    public CarSpecs save(CarSpecs specs) {
        String sql = "INSERT INTO car_specs (body_type, fuel_type, engine_capacity, seat_count, gearbox, drivetrain) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, specs.getBodyType());
            stmt.setString(2, specs.getFuelType());
            stmt.setDouble(3, specs.getEnginecapacity());
            stmt.setInt(4, specs.getSeatCount());
            stmt.setString(5, specs.getGearbox());
            stmt.setString(6, specs.getDrivetrain());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating car specs failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    // Setează ID-ul în obiectul existent
                    specs.setId(newId);
                    return specs;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CarSpecs update(CarSpecs specs) {
        if (specs.getId() <= 0) {
            System.err.println("Cannot update CarSpecs: ID is missing");
            return null;
        }

        String sql = "UPDATE car_specs SET body_type = ?, fuel_type = ?, engine_capacity = ?, seat_count = ?, gearbox = ?, drivetrain = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specs.getBodyType());
            stmt.setString(2, specs.getFuelType());
            stmt.setDouble(3, specs.getEnginecapacity());
            stmt.setInt(4, specs.getSeatCount());
            stmt.setString(5, specs.getGearbox());
            stmt.setString(6, specs.getDrivetrain());
            stmt.setInt(7, specs.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return specs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM car_specs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<CarSpecs> findById(Integer id) {
        String sql = "SELECT * FROM car_specs WHERE id = ?";
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
                specs.setId(rs.getInt("id"));
                return Optional.of(specs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<CarSpecs> findAll() {
        List<CarSpecs> specsList = new ArrayList<>();
        String sql = "SELECT * FROM car_specs";

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
                specs.setId(rs.getInt("id"));
                specsList.add(specs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specsList;
    }
}