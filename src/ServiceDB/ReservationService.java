package ServiceDB;

import Dao.ReservationDAO;
import Dao.ReservationServiceDAO;
import Model.*;
import Exception.CarNotAvailableException;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationServiceDAO reservationServiceDAO;
    private final AuditService auditService;

    public ReservationService() {
        this.reservationDAO = ReservationDAO.getInstance();
        this.reservationServiceDAO = ReservationServiceDAO.getInstance();
        this.auditService = AuditService.getInstance();
    }

    public Reservation createReservation(Client client, Car car, LocalDateTime startDate, LocalDateTime endDate)
            throws CarNotAvailableException {

        if (!isCarAvailable(car.getId(), startDate, endDate)) {
            throw new CarNotAvailableException("Mașina nu este disponibilă în perioada selectată");
        }

        Reservation reservation = new Reservation(client, car, startDate, endDate);
        Reservation savedReservation = reservationDAO.save(reservation);
        if(savedReservation != null){
            auditService.logReservationCreation(savedReservation.getId());
        }

        return savedReservation;
    }

    public Reservation createReservationWithServices(Client client, Car car, LocalDateTime startDate,
                                                     LocalDateTime endDate, List<AdditionalService> services)
            throws CarNotAvailableException
    {

        Reservation reservation = createReservation(client, car, startDate, endDate);


        for (AdditionalService service : services) {
            reservationServiceDAO.addServiceToReservation(
                    reservation.getId(),
                    service.getId(),
                    1,
                    service.getDailyRate()
            );
        }

        return reservation;
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public List<Reservation> getReservationsByClient(String clientName) {
        // Găsesc clientul și apoi rezervările lui
        auditService.logViewClientReservations(clientName);
        List<Reservation> allReservations = reservationDAO.findAll();
        return allReservations.stream()
                .filter(r -> r.getClient().getName().toLowerCase().contains(clientName.toLowerCase()))
                .toList();
    }

    public List<Reservation> getReservationsByClientId(int clientId) {
        return reservationDAO.findByClientId(clientId);
    }

    public Reservation updateReservation(Reservation reservation) {
        return reservationDAO.update(reservation);
    }

    public boolean cancelReservation(int reservationId) {
        boolean cancelled = reservationDAO.delete(reservationId);
        if(cancelled){
            auditService.logReservationCancellation(reservationId);
        }

        return cancelled;
    }

    public Reservation getReservationById(int id) {
        return reservationDAO.findById(id).orElse(null);
    }

    private boolean isCarAvailable(int carId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Reservation> existingReservations = reservationDAO.findAll();

        for (Reservation reservation : existingReservations) {
            if (reservation.getCar().getId() == carId) {
                // Verificam dacă există suprapunere de intervale
                if (!(endDate.isBefore(reservation.getStartDate()) ||
                        startDate.isAfter(reservation.getEndDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<AdditionalService> getServicesForReservation(int reservationId) {
        return reservationServiceDAO.getServicesForReservation(reservationId);
    }

    public double getServicesCostForReservation(int reservationId) {
        return reservationServiceDAO.getTotalServiceCostForReservation(reservationId);
    }
}