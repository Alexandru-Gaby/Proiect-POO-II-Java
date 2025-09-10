package Dao;

import DB.DBConnection;
import Model.Client;
import Model.VIPClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO implements GenericDAO<Client, Integer> {
    private static ClientDAO instance;
    private final Connection connection;

    private ClientDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static synchronized ClientDAO getInstance() {
        if (instance == null) {
            instance = new ClientDAO();
        }
        return instance;
    }

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO clients (name, address, phone_number, email, is_vip, discount_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhoneNumber());
            stmt.setString(4, client.getEmail());
            stmt.setBoolean(5, client instanceof VIPClient);
            stmt.setDouble(6, client instanceof VIPClient ? ((VIPClient) client).getDiscountRate() : 0.0);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    if (client instanceof VIPClient) {
                        return new VIPClient(newId, client.getName(), client.getAddress(),
                                client.getPhoneNumber(), client.getEmail(),
                                ((VIPClient) client).getDiscountRate());
                    } else {
                        return new Client(newId, client.getName(), client.getAddress(),
                                client.getPhoneNumber(), client.getEmail());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, address = ?, phone_number = ?, email = ?, is_vip = ?, discount_rate = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhoneNumber());
            stmt.setString(4, client.getEmail());
            stmt.setBoolean(5, client instanceof VIPClient);
            stmt.setDouble(6, client instanceof VIPClient ? ((VIPClient) client).getDiscountRate() : 0.0);
            stmt.setInt(7, client.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Client> findById(Integer id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getBoolean("is_vip")) {
                    return Optional.of(new VIPClient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            rs.getDouble("discount_rate")
                    ));
                } else {
                    return Optional.of(new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getBoolean("is_vip")) {
                    clients.add(new VIPClient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            rs.getDouble("discount_rate")
                    ));
                } else {
                    clients.add(new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public List<Client> findByName(String name) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE LOWER(name) LIKE LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getBoolean("is_vip")) {
                    clients.add(new VIPClient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            rs.getDouble("discount_rate")
                    ));
                } else {
                    clients.add(new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public List<Client> findVIPClients() {
        List<Client> vipClients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE is_vip = true ORDER BY name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vipClients.add(new VIPClient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getDouble("discount_rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vipClients;
    }
}