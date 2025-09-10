package Model;

public class RoadAssistance implements AdditionalService
{
    private Integer id;
    private double dailyRate = 10.0;
    private String name = "Road Assistance";
    private String description = "Serviciu de asistenta rutiera 24/7 care include: tractare in caz de defectiuni, pornire motor in caz de baterie descărcata,deblocare vehicul în caz de pierdere chei si suport tehnic telefonic permanenent.";

    @Override
    public double calculateServiceCost(long days)
    {
        return dailyRate * days;
    }

    @Override
    public String getServiceDescription()
    {
        return description;
    }

    @Override
    public double getDailyRate() {
        return dailyRate;
    }

    @Override
    public String getServiceName(){ return name; }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setServiceName(String name){
        this.name = name;
    }

    @Override
    public void setServiceDescription(String description)
    {
        this.description = description;
    }

    @Override
    public void setDailyRate(double rate)
    {
        this.dailyRate = rate;
    }
}
