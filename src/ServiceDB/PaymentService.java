package ServiceDB;

import Model.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PaymentService {

    public double calculateTotalPrice(Car car, long days, Client client, List<Reservation> allReservations,
                                      List<AdditionalService> services) {
        double basePrice = car.getPricePerDay() * days;
        double servicesTotal = 0;

        // Calculează costul serviciilor suplimentare
        for (AdditionalService service : services) {
            servicesTotal += service.getDailyRate() * days;
        }

        double total = basePrice + servicesTotal;

        // Aplică reducerea pentru clienții VIP
        if (client instanceof VIPClient) {
            VIPClient vipClient = (VIPClient) client;
            double discount = total * (vipClient.getDiscountRate() / 100);
            total -= discount;
        }

        return total;
    }

    public double calculateBasePrice(Car car, long days) {
        return car.getPricePerDay() * days;
    }

    public double calculateServicesPrice(List<AdditionalService> services, long days) {
        return services.stream()
                .mapToDouble(service -> service.getDailyRate() * days)
                .sum();
    }
}