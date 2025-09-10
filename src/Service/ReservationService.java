package Service;

import Model.AdditionalService;
import Model.Car;
import Model.Client;
import Model.Reservation;
import Exception.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ReservationService
{
    private Set<Reservation> reservations = new HashSet<>();


    public boolean isCarAvailable(Car car, LocalDateTime start, LocalDateTime end)
    {
        for (Reservation r : reservations) {
            if (r.getCar().getId() == car.getId()) {
                if (!(end.isBefore(r.getStartDate()) || start.isAfter(r.getEndDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void createReservation(Client client, Car car, LocalDateTime startDate, LocalDateTime endDate)
            throws CarNotAvailableException
    {
        if (!isCarAvailable(car, startDate,endDate))
        {
            throw new CarNotAvailableException("Mașina este deja rezervată în perioada selectată.");
        }

        Reservation reservation = new Reservation(client, car, startDate, endDate);
        reservations.add(reservation);
    }

    public List<Reservation> getReservationsByClient(String clientName)
    {
       List <Reservation> result = new ArrayList<>();
       for(Reservation r : reservations)
       {
           if(r.getClient().getName().equalsIgnoreCase(clientName))
           {
               result.add(r);
           }
       }
       return result;
    }

    public void cancelReservation(Reservation reservation)
    {
        reservations.remove(reservation);
    }

    public Set<Reservation> getAllReservations()
    {
        return reservations;
    }

    public Reservation createReservationWithServices(Client client,Car car,LocalDateTime startDate,LocalDateTime endDate, List<AdditionalService>additionalServices)
        throws CarNotAvailableException {

            if(!isCarAvailable(car, startDate, endDate)){
                throw new CarNotAvailableException("Masina nu este disponibila pentru inchiriere.");
            }

            Reservation reservation = new Reservation(client,car,startDate,endDate);

            for(AdditionalService service: additionalServices)
            {
                reservation.addAdditionalService(service);
            }
            reservations.add(reservation);

            return reservation;
    }


}
