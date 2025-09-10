import Model.*;
import Service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        ClientService clientService = new ClientService();
        CarService carService = new CarService(new ArrayList<>());
        ReservationService reservationService = new ReservationService();
        PaymentService paymentService = new PaymentService();
        Service service = new Service(clientService,carService,reservationService,paymentService);

        boolean running = true;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


        while(running)
        {
            System.out.println("\n--- Meniu Principal ---");
            System.out.println("1. Adaugă client: ");
            System.out.println("2. Ștergere client: ");
            System.out.println("3. Adaugă mașină:");
            System.out.println("4. Afișează mașini disponibile: ");
            System.out.println("5. Afișează toate rezervările disponibile unui anumit client: ");
            System.out.println("6. Rezervă mașină: ");
            System.out.println("7. Afișează clienții VIP");
            System.out.println("8. Afișează toți clienții");
            System.out.println("9. Ieșire");

            System.out.println("Alege opțiunea: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option)
            {
                case 1:
                    System.out.print("Nume: ");
                    String name = scanner.nextLine();
                    System.out.print("Adresă: ");
                    String address = scanner.nextLine();
                    System.out.print("Număr telefon: ");
                    String phone = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    service.registerClient(name, address, phone, email);
                    System.out.println("Client adăugat cu succes! ");
                    break;

                case 2:
                    System.out.print("Numele clientului de șters: ");
                    String nametodelete = scanner.nextLine();
                    boolean removed = service.removeClientByName(nametodelete);

                    if(removed)
                    {
                        System.out.println("Clientul a fost șters. ");
                    }
                    else
                    {
                        System.out.println("Clientul nu a fost găsit. ");
                    }
                    break;

                case 3:

                    System.out.print("Marcă: ");
                    String brand = scanner.nextLine();

                    System.out.print("Model: ");
                    String model = scanner.nextLine();

                    System.out.print("An fabricație: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Preț pe zi (RON): ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();


                    // Specificații
                    System.out.print("Tip caroserie (ex: Sedan, SUV): ");
                    String bodyType = scanner.nextLine();

                    System.out.print("Tip combustibil (ex: Benzină, Diesel, Electric): ");
                    String fuelType = scanner.nextLine();

                    System.out.print("Capacitate cilindrică(cm3)/Autonomie(km): ");
                    double engineCapacity = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Număr locuri: ");
                    int seats = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Transmisie (Manuală/Automată): ");
                    String gearbox = scanner.nextLine();

                    System.out.print("Tracțiune (ex: față, spate, integrală): ");
                    String drivetrain = scanner.nextLine();

                    CarSpecs specs = new CarSpecs(bodyType, fuelType, engineCapacity, seats, gearbox, drivetrain);


                    Car newCar = service.addCar(brand, model, year, price, specs);

                    // System.out.println("Mașina " + newCar.getBrand() + " " + newCar.getModel() +  " cu ID: " +
                    //        newCar.getId() + ", a fost adăugată în parc cu succes!");
                    System.out.println("Mașina a fost adăugată în parc cu succes!\n" + newCar.toString());
                    break;

                case 4:
                    try {
                        System.out.print("Introduceți data de început (dd-MM-yyyy HH:mm): ");
                        LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), formatter);

                        System.out.print("Introduceți data de sfârșit (dd-MM-yyyy HH:mm): ");
                        LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatter);

                        List<Car> availableCars = service.getAvailableCars(start, end);
                        if (availableCars.isEmpty()) {
                            System.out.println("Nu există mașini disponibile în perioada selectată!");
                        } else {
                            System.out.println("Masini disponibile: ");
                            for (Car car : availableCars) {
                                System.out.println(car);
                            }
                        }
                    }catch (Exception e){
                        System.out.println("Format invalid! Folositi: dd-MM-yyyy HH:mm");
                    }
                        break;


                case 5:
                    System.out.print("Introduceti numele clientului: ");
                    String nameSearch = scanner.nextLine();
                    List<Reservation> reservations = service.getReservationsByClientName(nameSearch);
                    if (reservations.isEmpty()) {
                        System.out.println("Nicio rezervare găsită pentru acest client.");
                    } else {
                        for (Reservation r : reservations) {
                            System.out.println(r);
                        }
                    }
                    break;


                case 6:
                    try {
                        System.out.print("Nume client: ");
                        String clientName = scanner.nextLine();
                        List<Client> foundClients = service.findClientsByName(clientName);

                        if (foundClients.isEmpty()) {
                            System.out.println("Clientul nu a fost găsit.");
                            break;
                        }

                        Client selectedClient = foundClients.get(0);

                        System.out.print("Data început (dd-MM-yyyy HH:mm): ");
                        LocalDateTime start2 = LocalDateTime.parse(scanner.nextLine(), formatter);

                        System.out.print("Data sfârșit (dd-MM-yyyy HH:mm): ");
                        LocalDateTime end2 = LocalDateTime.parse(scanner.nextLine(), formatter);

                        List<Car> availableCars2 = service.getAvailableCars(start2, end2);
                        if (availableCars2.isEmpty()) {
                            System.out.println("Nu există mașini disponibile pentru rezervare în perioada selectată.");
                            break;
                        }

                        System.out.println("Mașini disponibile:");
                        for (Car car : availableCars2) {
                            System.out.println(car);
                        }


                        System.out.print("Introduceți ID-ul mașinii pe care doriți să o rezervați: ");
                        int carId = scanner.nextInt();
                        scanner.nextLine();

                        List<AdditionalService> selectedServices = new ArrayList<>();
                        System.out.println("Doriti servicii suplimentare? (da/nu): ");
                        String raspuns = scanner.nextLine().trim().toLowerCase();

                        if (raspuns.equalsIgnoreCase("da"))
                        {
                            InsuranceService insuranceService = new InsuranceService();
                            RoadAssistance roadAssistance = new RoadAssistance();
                            System.out.println("Servicii disponibile:");
                            System.out.println("1. " + insuranceService.getServiceDescription() + " - " + insuranceService.getDailyRate() + " RON/zi");
                            System.out.println("2. " + roadAssistance.getServiceDescription() + " - " + roadAssistance.getDailyRate() + " RON/zi");
                            System.out.print("Introduceți opțiunile separate prin spațiu (ex: 1 2): ");
                            String[] optiuni = scanner.nextLine().split(" ");

                            for (String opt : optiuni) {
                                switch (opt) {
                                    case "1":
                                        selectedServices.add(new InsuranceService());
                                        break;
                                    case "2":
                                        selectedServices.add(new RoadAssistance());
                                        break;
                                    default:
                                        System.out.println("Optiune invalida: " + opt);
                                }
                            }
                        }

                        service.reserveCarWithServices(selectedClient, carId, start2, end2, selectedServices);
                        System.out.println("Rezervarea a fost finalizată cu succes!");

                    }catch (Exception e){
                        System.out.println("Eroare la rezervare: " + e.getMessage());
                    }
                        break;


                case 7:
                    List<Client> vipClients = service.getVipClientsSortedByName();
                    if(vipClients.isEmpty())
                    {
                        System.out.println("Nu există clienți vip.");
                    }else {
                        System.out.println("Clienți VIP sortați după nume:");

                        for (Client c : vipClients) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 8:
                    List<Client> clients = service.displayAllClients();
                    if (clients.isEmpty()) {
                        System.out.println("Nu există clienți înregistrați.");
                    } else {
                        System.out.println("Clienți înregistrați:");
                        for (Client client : clients) {
                            System.out.println(client);
                        }
                    }
                    break;

                case 9:
                    running = false;
                    System.out.println("Ieșire din aplicație.");
                    break;

                default:
                    System.out.println("Opțiune invalidă. Vă rugăm să alegeți din meniu.");
                    break;

            }

        }
        scanner.close();

    }
}
