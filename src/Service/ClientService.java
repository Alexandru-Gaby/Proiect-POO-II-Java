package Service;

import Model.Client;
import Model.Reservation;
import Model.VIPClient;

import java.util.*;

public class ClientService
{
    private Map<Integer, Client> clients = new HashMap<>();
    int nextId = 1;

    public Client addClient(String name, String address, String phoneNumber, String email)
    {
       Client client = new Client(nextId++, name, address, phoneNumber, email);
       clients.put(client.getId(),client);

       return client;
    }

    public Client findClientById(int id)
    {
        return clients.get(id);
    }


    public void replaceClient(int id, Client newClient) {
        clients.put(id, newClient);
    }



    public List<Client> findClientByName(String name)
    {
        List<Client> results = new ArrayList<>();
        for(Client c : clients.values())
        {
            if(c.getName().toLowerCase().contains(name.toLowerCase()))
            {
                results.add(c);
            }
        }
        return results;
    }

    public boolean removeClientByName(String name)
    {
        List <Client> found = findClientByName(name);
        if(!found.isEmpty())
        {
            for (Client c: found)
            {
                clients.remove(c.getId());
            }
            return true;
        }

        return false;

    }

    public boolean removeClient(int id)
    {
        return clients.remove(id)!= null;
    }

    public boolean updateClientEmail(int id, String newEmail)
    {
        Client c1 = clients.get(id);
        if(c1 != null)
        {
            c1.setEmail(newEmail);
            return  true;
        }
      return false;
    }

    public boolean updateClientPhone(int id, String newphonenumber)
    {
        Client c1 = clients.get(id);
        if(c1 != null)
        {
            c1.setPhoneNumber(newphonenumber);
            return true;
        }
        return false;
    }


    public List<Client> getAllClients()
    {
        return new ArrayList<>(clients.values());
    }

    public List<Client> getVipClientsSortedByName(List<Reservation> reservations)
    {
        List<Client> vipClients = new ArrayList<>();

        for(Client client : clients.values())
        {
            int reservationCount = 0;
            for (Reservation r : reservations)
            {
                if (r.getClient().getId() == client.getId()) {
                    reservationCount++;
                }
            }

            if (reservationCount >= 3)
            {
              if(!(client instanceof VIPClient))
              {
                  VIPClient vip = new VIPClient(
                          client.getId(),
                          client.getName(),
                          client.getAddress(),
                          client.getPhoneNumber(),
                          client.getEmail(),
                          15
                  );
                  vipClients.add(vip);
              }
              else {
                  vipClients.add(client);
              }
            }
        }
        vipClients.sort(Comparator.comparing(Client::getName));

        return vipClients;
    }

}
