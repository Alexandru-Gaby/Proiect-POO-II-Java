import Model.*;
import ServiceDB.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainDB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ClientService clientService = new ClientService();
        CarService carService = new CarService();
        ReservationService reservationService = new ReservationService();
        PaymentService paymentService = new PaymentService();

        Service service = new Service(clientService, carService, reservationService, paymentService);

        boolean running = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        while (running) {
            System.out.println("\n--- Meniu Principal ---");
            System.out.println("1. Adaugă client: ");
            System.out.println("2. Ștergere client: ");
            System.out.println("3. Adaugă mașină:");
            System.out.println("4. Afișează mașini disponibile: ");
            System.out.println("5. Afișează toate rezervările disponibile unui anumit client: ");
            System.out.println("6. Rezervă mașină: ");
            System.out.println("7. Afișează clienții VIP");
            System.out.println("8. Afișează toți clienții");
            System.out.println("9. Adaugă servicii suplimentare în sistem");
            System.out.println("10. Afișează toate mașinile");
            System.out.println("11. Actualizare client");
            System.out.println("12. Ieșire");

            System.out.println("Alege opțiunea: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
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

                    if (removed) {
                        System.out.println("Clientul a fost șters. ");
                    } else {
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
                    } catch (Exception e) {
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

                        if (raspuns.equalsIgnoreCase("da")) {
                            // Obține serviciile din baza de date
                            List<AdditionalService> allServices = service.getAllAdditionalServices();

                            if (allServices.isEmpty()) {
                                System.out.println("Nu există servicii suplimentare în sistem.");
                            } else {
                                System.out.println("Servicii disponibile:");
                                for (int i = 0; i < allServices.size(); i++) {
                                    AdditionalService srv = allServices.get(i);
                                    System.out.println((i + 1) + ". " + srv.getServiceDescription() +
                                            " - " + srv.getDailyRate() + " RON/zi");
                                }

                                System.out.print("Introduceți opțiunile separate prin spațiu (ex: 1 2): ");
                                String[] optiuni = scanner.nextLine().split(" ");

                                for (String opt : optiuni) {
                                    try {
                                        int index = Integer.parseInt(opt) - 1;
                                        if (index >= 0 && index < allServices.size()) {
                                            selectedServices.add(allServices.get(index));
                                        } else {
                                            System.out.println("Optiune invalida: " + opt);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Optiune invalida: " + opt);
                                    }
                                }
                            }
                        }

                        service.reserveCarWithServices(selectedClient, carId, start2, end2, selectedServices);
                        System.out.println("Rezervarea a fost finalizată cu succes!");

                    } catch (Exception e) {
                        System.out.println("Eroare la rezervare: " + e.getMessage());
                    }
                    break;

                case 7:
                    List<Client> vipClients = service.getVipClientsSortedByName();
                    if (vipClients.isEmpty()) {
                        System.out.println("Nu există clienți vip.");
                    } else {
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
                    System.out.println("Adăugare servicii suplimentare:");
                    System.out.println("1. Asigurare");
                    System.out.println("2. Asistență rutieră");
                    System.out.print("Alegeți opțiunea: ");
                    int serviceOption = scanner.nextInt();
                    scanner.nextLine();

                    switch (serviceOption) {
                        case 1:
                            AdditionalService insurance = service.addInsuranceService();
                            System.out.println("Serviciu de asigurare adăugat cu succes!");
                            break;
                        case 2:
                            AdditionalService roadAssist = service.addRoadAssistance();
                            System.out.println("Serviciu de asistență rutieră adăugat cu succes!");
                            break;
                        default:
                            System.out.println("Opțiune invalidă.");
                    }
                    break;

                case 10:
                    List<Car> allCars = service.getAllCars();
                    if (allCars.isEmpty()) {
                        System.out.println("Nu există mașini în sistem.");
                    } else {
                        System.out.println("Toate mașinile din sistem:");
                        for (Car car : allCars) {
                            System.out.println(car);
                        }
                    }
                    break;
                case 11:
                    try {
                        System.out.print("Introduceti numele clientului de actualizat: ");
                        String searchName = scanner.nextLine();
                        List<Client> clientsFound = service.findClientsByName(searchName);

                        if (clientsFound.isEmpty()) {
                            System.out.println("Nu a fost gasit niciun client cu numele specificat.");
                            break;
                        }

                        System.out.println("Clienti gasiti:");
                        for (int i = 0; i < clientsFound.size(); i++) {
                            System.out.println((i + 1) + ". " + clientsFound.get(i));
                        }

                        Client clientToUpdate;
                        if (clientsFound.size() == 1) {
                            clientToUpdate = clientsFound.get(0);
                        } else {
                            System.out.print("Selectati clientul (numarul): ");
                            int selection = scanner.nextInt();
                            scanner.nextLine();
                            if (selection < 1 || selection > clientsFound.size()) {
                                System.out.println("Selectie invalida!");
                                break;
                            }
                            clientToUpdate = clientsFound.get(selection - 1);
                        }

                        System.out.println("Client selectat pentru actualizare: " + clientToUpdate);
                        System.out.println("Introduceti noile date (pentru a pastra valorile actuale lasa spatiul necompletat):");


                        System.out.print("Nume nou (" + clientToUpdate.getName() + "): ");
                        String newName = scanner.nextLine().trim();
                        if (!newName.isEmpty()) {
                            clientToUpdate.setName(newName);
                        }

                        System.out.print("Adresa noua (" + clientToUpdate.getAddress() + "): ");
                        String newAddress = scanner.nextLine().trim();
                        if (!newAddress.isEmpty()) {
                            clientToUpdate.setAddress(newAddress);
                        }


                        System.out.print("Telefon nou (" + clientToUpdate.getPhoneNumber() + "): ");
                        String newPhone = scanner.nextLine().trim();
                        if (!newPhone.isEmpty()) {
                            clientToUpdate.setPhoneNumber(newPhone);
                        }

                        System.out.print("Email nou (" + clientToUpdate.getEmail() + "): ");
                        String newEmail = scanner.nextLine().trim();
                        if (!newEmail.isEmpty()) {
                            clientToUpdate.setEmail(newEmail);
                        }


                        if (!(clientToUpdate instanceof VIPClient))
                        {
                            System.out.print("Doriti sa promovati clientul la VIP? (da/nu): ");
                            String promoteResponse = scanner.nextLine().trim().toLowerCase();
                            if (promoteResponse.equals("da")) {
                                System.out.print("Introduceti rata de discount (0-100): ");
                                try {
                                    double discountRate = scanner.nextDouble();
                                    scanner.nextLine();
                                    if (discountRate >= 0 && discountRate <= 100) {
                                        VIPClient newVipClient = new VIPClient(
                                                clientToUpdate.getId(),
                                                clientToUpdate.getName(),
                                                clientToUpdate.getAddress(),
                                                clientToUpdate.getPhoneNumber(),
                                                clientToUpdate.getEmail(),
                                                discountRate
                                        );
                                        clientToUpdate = newVipClient;
                                        System.out.println("Clientul a fost promovat la VIP!");
                                    } else {
                                        System.out.println("Rata de discount trebuie sa fie intre 0 și 100%");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Format invalid pentru discount!");
                                    scanner.nextLine();
                                }
                            }
                        }

                        Client updatedClient = clientService.updateClient(clientToUpdate);
                        if (updatedClient != null) {
                            System.out.println("Clientul a fost actualizat cu succes!");
                            System.out.println("Date noi: " + updatedClient);
                        } else {
                            System.out.println("Eroare la actualizarea clientului.");
                        }

                    } catch (Exception e) {
                        System.out.println("Eroare la actualizarea clientului: " + e.getMessage());
                        scanner.nextLine();
                    }
                    break;
                case 12:
                    running = false;
                    System.out.println("Iesire din aplicație.");
                    break;

                default:
                    System.out.println("Opțiune invalidă. Vă rugăm să alegeți din meniu.");
                    break;
            }
        }
        scanner.close();
    }
}