package ServiceDB;

import Dao.ClientDAO;
import Model.*;
import java.util.List;


public class ClientService
{
    private final ClientDAO clientDAO;
    private final AuditService auditService;

    public ClientService() {
        this.clientDAO = ClientDAO.getInstance();
        this.auditService = AuditService.getInstance();
    }

    public Client addClient(String name, String address, String phone, String email) {
        Client client = new Client(name, address, phone, email);
        Client savedClient = clientDAO.save(client);

        if(savedClient != null)
        {
            auditService.logClientRegistration(name);
        }

        return savedClient;
    }

    public boolean removeClientByName(String name) {
        List<Client> clients = clientDAO.findByName(name);
        if (!clients.isEmpty()) {
            boolean removed = clientDAO.delete(clients.get(0).getId());
            if(removed){
                auditService.logClientRemoval(name);
            }

            return removed;
        }
        return false;
    }

    public List<Client> findClientByName(String name) {
        auditService.logSearchClient(name);
        return clientDAO.findByName(name);
    }

    public List<Client> getAllClients() {
        auditService.logViewAllClients();
        return clientDAO.findAll();
    }

    public List<Client> getVipClientsSortedByName(List<Reservation> allReservations) {
        auditService.logViewVIPClients();
        return clientDAO.findVIPClients();
    }

    public Client updateClient(Client client) {
        return clientDAO.update(client);
    }

    public void replaceClient(int clientId, VIPClient vipClient) {
        clientDAO.update(vipClient);
        auditService.logClientPromotion(vipClient.getName());
    }

    public Client getClientById(int id) {
        return clientDAO.findById(id).orElse(null);
    }
}