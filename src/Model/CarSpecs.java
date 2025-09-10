package Model;

public class CarSpecs
{
    private int id;

    private String bodyType;
    private String fuelType;
    private double engineCapacity;
    private int seatCount;
    private String gearbox;
    private String drivetrain;

    public CarSpecs(String bodyType,String fuelType, double engineCapacity,
                    int seatCount, String gearbox, String drivetrain)
    {
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.engineCapacity = engineCapacity;
        this.seatCount = seatCount;
        this.gearbox = gearbox;
        this.drivetrain = drivetrain;
    }

    public CarSpecs(int id, String bodyType,String fuelType, double engineCapacity,
                    int seatCount,String gearbox,String drivetrain)
    {
        this.id = id;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.engineCapacity = engineCapacity;
        this.seatCount = seatCount;
        this.gearbox = gearbox;
        this.drivetrain = drivetrain;
    }
    public void setId(int id){ this.id = id; }

    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public void setEngineCapacity(double engineCapacity) { this.engineCapacity = engineCapacity; }

    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public void setGearbox(String gearbox) { this.gearbox = gearbox; }

    public void setDrivetrain(String drivetrain) { this.drivetrain = drivetrain; }

    //Getteri
    public int getId() { return id; }

    public String getBodyType() { return bodyType; }

    public String getFuelType() { return fuelType; }

    public double getEnginecapacity() { return engineCapacity; }

    public int getSeatCount() { return seatCount; }

    public String getGearbox() { return gearbox; }

    public String getDrivetrain() { return drivetrain; }

    @Override
    public String toString() {
        return "Tip caroserie: " + bodyType +
                ", Combustibil: " + fuelType +
                ", Capacitate: " + engineCapacity + "L" +
                ", Locuri: " + seatCount +
                ", Transmisie: " + gearbox +
                ", Tracțiune: " + drivetrain;
    }


}
