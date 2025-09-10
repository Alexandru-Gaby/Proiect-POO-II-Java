package Service;

import Model.*;
import Exception.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;


public class Service
{
    private ClientService clientService;
    private CarService carService;
    private ReservationService reservationService;
    private PaymentService paymentService;


    public Service(ClientService clientService, CarService carService, ReservationService reservationService, PaymentService paymentService)
    {
        this.clientService = clientService;
        this.carService = carService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
    }

    public Client registerClient(String name, String address, String phone, String email)
    {
        return clientService.addClient(name, address, phone, email);
    }

    public boolean removeClientByName(String name)
    {
        return clientService.removeClientByName(name);
    }

    public List<Car> getAvailableCars(LocalDateTime start, LocalDateTime end)
    {
        return carService.getAvailableCars(start,end, reservationService);
    }

    public List<Client> displayAllClients(){
        return clientService.getAllClients();
    }

    public Car addCar(String brand, String model, int year, double price, CarSpecs specs)
    {
       return carService.addCar(brand, model, year, price, specs);
    }


    public void reserveCarWithServices(Client client, int carId, LocalDateTime start, LocalDateTime end, List<AdditionalService> services)
    {
        Car car = carService.getCarById(carId);
        if (car == null) {
            System.out.println("Mașina nu a fost găsită.");
            return;
        }
        //daca masina este deja rezervata metoda createReservation arunca exceptia
        try {
//            reservationService.createReservation(client, car, start, end);
           Reservation reservation = reservationService.createReservationWithServices(client, car, start, end, services);
           long days = ChronoUnit.DAYS.between(start, end);

           List<Reservation> allReservations = new ArrayList<>(reservationService.getAllReservations());

            int count = 0;
            for (Reservation r : allReservations) {
                if (r.getClient().getId() == client.getId()) {
                    count++;
                }
            }

            if(count >= 3 && !(client instanceof VIPClient))
           {
               VIPClient vipclient = new VIPClient(
                       client.getId(),
                       client.getName(),
                       client.getAddress(),
                       client.getPhoneNumber(),
                       client.getEmail(),
                       15.0
               );
               clientService.replaceClient(client.getId(), vipclient);
               client = vipclient;
               System.out.println("Felicitări! Clientul a fost promovat la statutul de VIP.");

           }

           double total = paymentService.calculateTotalPrice(car, days, client, allReservations, services);
           reservation.setTotalAmount(total);

           reservation.showReservationDetails();


            System.out.println("Rezervarea a fost finalizata. Preț total: " + total + "RON");
        } catch (CarNotAvailableException e) {
            System.out.println("Eroare la rezervarea masinii: " + e.getMessage());
        }
    }

    public List<Reservation> getReservationsByClientName(String name)
    {
        return reservationService.getReservationsByClient(name);
    }

    public List<Client> getVipClientsSortedByName()
    {
        List<Reservation> allReservations = new ArrayList<>(reservationService.getAllReservations());

        return clientService.getVipClientsSortedByName(allReservations);
    }

    public List<Client>findClientsByName(String name)
    {
        return clientService.findClientByName(name);
    }

//    public double calculateTotalPrice(Model.Car car, long days, Model.Client client)
//    {
//        return paymentService.calculateTotalPrice(car, days, client);
//    }

    public Car getCarById(int carId)
    {
       return  carService.getCarById(carId);
    }

}




