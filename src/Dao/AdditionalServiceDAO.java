package Dao;

import DB.DBConnection;
import Model.AdditionalService;
import Model.InsuranceService;
import Model.RoadAssistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdditionalServiceDAO implements GenericDAO<AdditionalService, Integer>
{
    private static AdditionalServiceDAO instance;
    private final Connection connection;

    private AdditionalServiceDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static synchronized AdditionalServiceDAO getInstance() {
        if (instance == null) {
            instance = new AdditionalServiceDAO();
        }
        return instance;
    }

    @Override
    public AdditionalService save(AdditionalService service) {
        String sql = "INSERT INTO additional_services (name, description, daily_rate, service_type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, service.getServiceName());
            stmt.setString(2, service.getServiceDescription());
            stmt.setDouble(3, service.getDailyRate());
            stmt.setString(4, service.getClass().getSimpleName().toUpperCase());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating additional service failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return service;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AdditionalService update(AdditionalService service) {
        String sql = "UPDATE additional_services SET name = ?, description = ?, daily_rate = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, service.getServiceName());
            stmt.setString(2, service.getServiceDescription());
            stmt.setDouble(3, service.getDailyRate());
            stmt.setInt(4, service.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return service;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM additional_services WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<AdditionalService> findById(Integer id) {
        String sql = "SELECT * FROM additional_services WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(createServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<AdditionalService> findAll() {
        List<AdditionalService> services = new ArrayList<>();
        String sql = "SELECT * FROM additional_services";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(createServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    private AdditionalService createServiceFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String serviceType = rs.getString("service_type");
        String name = rs.getString("name");
        String description = rs.getString("description");
        double dailyRate = rs.getDouble("daily_rate");

        AdditionalService service;
        switch (serviceType.toUpperCase()) {
            case "INSURANCESERVICE":
                service =  new InsuranceService();
                break;
            case "ROADASSISTANCE":
                service = new RoadAssistance();
                break;
            default:
                service = new InsuranceService();
                break;
        }
        service.setId(id);
        service.setServiceName(name);
        service.setServiceDescription(description);
        service.setDailyRate(dailyRate);
        return service;
    }

    public List<AdditionalService> findByType(String serviceType) {
        List<AdditionalService> services = new ArrayList<>();
        String sql = "SELECT * FROM additional_services WHERE service_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serviceType.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                services.add(createServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}