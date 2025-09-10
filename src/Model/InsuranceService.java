package Model;

public class InsuranceService implements AdditionalService
{
    private Integer id;
    private double dailyRate = 15.0;
    private String name = "Insurance Service";
    private String description = "Asigurare completa care acopera toate daunele asupra vehiculului( inclusiv daune accidentale, furt, vandalism si calamitati naturale).";

    @Override
    public double calculateServiceCost(long days)
    {
        return dailyRate * days;
    }

    @Override
    public String getServiceName()
    {
        return name;
    }

    @Override
    public String getServiceDescription()
    {
        return  description;
    }

    @Override
    public double getDailyRate()
    {
        return dailyRate;
    }

    @Override
    public Integer getId()
    {
        return id;
    }

    @Override
    public void setId(Integer id)
    {
        this.id = id;
    }
    @Override
    public void setServiceName(String name) {
        this.name = name;
    }

    @Override
    public void setServiceDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDailyRate(double rate) {
        this.dailyRate = rate;
    }

}
