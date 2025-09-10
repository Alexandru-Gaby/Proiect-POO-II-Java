package Service;

import Model.AdditionalService;
import Model.Car;
import Model.Client;
import Model.Reservation;
import Model.VIPClient;

import java.util.List;

public class PaymentService
{

    public double calculateTotalPrice(Car car, long rentalDays, Client client, List<Reservation> reservations, List<AdditionalService> services)
    {
        double basePrice = car.getPricePerDay() * rentalDays;

        for(AdditionalService service : services)
        {
            basePrice += service.calculateServiceCost(rentalDays);
        }

        if (client instanceof VIPClient vip)
        {
            double discountRate = vip.getDiscountRate();
            return basePrice * (1 - discountRate / 100.0);
        }
        return basePrice;

    }

//    public double calculateTotalPrice(Model.Car car, long days, Model.Client client)
//    {
//        return car.getPricePerDay() * days;
//    }

    public void processPayment(double amount, String paymentMethod)
    {
        System.out.println("Plata in valoare de " + amount + " RON " + "a fost achitata cu " + paymentMethod);
    }


}
