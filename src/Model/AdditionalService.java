package Model;

public interface AdditionalService
{
    double calculateServiceCost(long days);
    String getServiceName();
    String getServiceDescription();
    double getDailyRate();
    Integer getId();
    void setId(Integer id);

    void setServiceName(String name);
    void setServiceDescription(String description);
    void setDailyRate(double dailyrate);
}
