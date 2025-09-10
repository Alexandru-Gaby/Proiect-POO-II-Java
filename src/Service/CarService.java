package Service;

import Model.Car;
import Model.CarSpecs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CarService
{
    private int nextCarId = 1;
    private List<Car> cars;


    public CarService(List<Car> cars)
    { this.cars = cars; }

    public Car addCar(String brand, String model, int year, double pricePerDay, CarSpecs specs)
    {
        Car car = new Car(nextCarId++, brand, model, year, pricePerDay, specs);
        cars.add(car);

        return car;
    }

    public void removeCar(int carId)
    { cars.removeIf(car -> car.getId() == carId); }

    public Car getCarById(int carId)
    {
      for(Car car : cars)
      {
          if(car.getId() == carId)
          {
              return car;
          }
      }
        return null;
    }

    public List<Car> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate, ReservationService reservationService)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        List<Car> availableCars = new ArrayList<>();
        for(Car car : cars)
        {
            boolean isAvailable = true;

            if(reservationService.isCarAvailable(car,startDate,endDate)) {
                availableCars.add(car);}
        }
        System.out.println("Masini disponibile in perioada: " + startDate.format(formatter) + " - " + endDate.format(formatter) + ": " + availableCars.size());
        return availableCars;
    }
}
