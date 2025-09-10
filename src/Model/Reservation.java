package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reservation
{
    private static int nextId = 1;
    private int id;
    private Client client;
    private Car car;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private double totalAmount;
    private List<AdditionalService> additionalServices;

    public Reservation(Client client, Car car, LocalDateTime startDate, LocalDateTime endDate)
    {
        this.id = nextId++;
        this.client = client;
        this.car = car;
        this.startDateTime = startDate;
        this.endDateTime = endDate;
        this.additionalServices = new ArrayList<>();
    }

    // Constructor pentru baza de date
    public Reservation(int id, Client client, Car car, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.client = client;
        this.car = car;
        this.startDateTime = startDate;
        this.endDateTime = endDate;
        this.additionalServices = new ArrayList<>();
    }

    // Getteri
    public int getId() { return id; }
    public Client getClient() { return client; }
    public Car getCar() { return car; }
    public LocalDateTime getStartDate() { return startDateTime; }
    public LocalDateTime getEndDate() { return endDateTime; }


    public double getTotalAmount(){
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount){
        this.totalAmount = totalAmount;
    }

    public void addAdditionalService(AdditionalService service) {
        this.additionalServices.add(service);
    }

    public void showReservationDetails()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        System.out.println("Client: " + client.getName());
        System.out.println("Masina: " + car.getBrand() + " " + car.getModel());
        System.out.println("Perioada: " + startDateTime.format(formatter) + " - " + endDateTime.format(formatter));
    }


    public void setEndDate(LocalDate endDate) { this.endDateTime = endDateTime; }

    @Override
    public String toString()
    {
        return "Rezervare #" + id + " - " + client.getName() + " | " +
                car.getBrand() + " " + car.getModel() +
                " | De la: " + startDateTime + " până la: " + endDateTime;
    }

    //verificam daca 2 obiecte de tip reservatios sunt egale
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation other = (Reservation) o;
        return car.getId() == other.car.getId() && startDateTime.equals(other.startDateTime);
    }

    @Override
    public int hashCode()
    {
//  return car.getId() * 31 + startDate.hashCode();
    int result = client.getId();
    result = 31 * result + car.getId();
    result = 31 * result + startDateTime.hashCode();
    result = 31 * result + endDateTime.hashCode();
    return result;
    }

}
