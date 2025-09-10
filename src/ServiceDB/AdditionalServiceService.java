package ServiceDB;

import Dao.AdditionalServiceDAO;
import Model.*;

import java.util.List;

public class AdditionalServiceService {
    private final AdditionalServiceDAO additionalServiceDAO;
    private final AuditService auditService;

    public AdditionalServiceService() {
        this.additionalServiceDAO = AdditionalServiceDAO.getInstance();
        this.auditService = AuditService.getInstance();
    }

    public AdditionalService addInsuranceService() {
        InsuranceService service = new InsuranceService();
        AdditionalService savedService = additionalServiceDAO.save(service);
        if(savedService != null){
            auditService.logServiceAddition("INSURANCE_SERVICE");
        }

        return savedService;
    }

    public AdditionalService addRoadAssistance() {
        RoadAssistance service = new RoadAssistance();
        AdditionalService savedService = additionalServiceDAO.save(service);

        if (savedService != null) {
            auditService.logServiceAddition("ROAD_ASSISTANCE");
        }

        return savedService;
    }

    public List<AdditionalService> getAllServices() {
        return additionalServiceDAO.findAll();
    }

    public List<AdditionalService> getServicesByType(String type) {
        return additionalServiceDAO.findByType(type);
    }

    public AdditionalService getServiceById(int id) {
        return additionalServiceDAO.findById(id).orElse(null);
    }

    public AdditionalService updateService(AdditionalService service) {
        return additionalServiceDAO.update(service);
    }

    public boolean deleteService(int id) {
        return additionalServiceDAO.delete(id);
    }
}
