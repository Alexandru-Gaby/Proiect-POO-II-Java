package ServiceDB;

import Dao.*;
import Model.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class CarService {
    private final CarDAO carDAO;
    private final AuditService auditService;

    public CarService() {
        this.carDAO = CarDAO.getInstance();
        this.auditService = AuditService.getInstance();
    }

    public Car addCar(String brand, String model, int year, double pricePerDay, CarSpecs specs) {
        Car car = new Car(brand, model, year, pricePerDay, specs);
        Car savedCar = carDAO.save(car);

        if(savedCar != null){
            auditService.logCarAddition(brand, model);
        }

        return savedCar;
    }

    public List<Car> getAllCars() {
        auditService.logViewAllCars();
        return carDAO.findAll();
    }

    public Car getCarById(int id) {
        return carDAO.findById(id).orElse(null);
    }

    public List<Car> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate, ReservationService reservationService) {
        auditService.logViewAvailableCars();
        Timestamp start = Timestamp.valueOf(startDate);
        Timestamp end = Timestamp.valueOf(endDate);
        return carDAO.findAvailableCars(start, end);
    }

    public Car updateCar(Car car) {
        return carDAO.update(car);
    }

    public boolean deleteCar(int id) {
        boolean deleted = carDAO.delete(id);
        if(deleted){
            auditService.logCarRemoval(id);
        }
        return deleted;
    }

}