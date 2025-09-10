package Model;

public class Car
{
    private int id;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private CarSpecs specs;


    public Car(String brand, String model, int year,
               double pricePerDay, CarSpecs specs)
    {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.specs = specs;
    }

    public Car(int id, String brand, String model, int year,
               double pricePerDay, CarSpecs specs)
    {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.specs = specs;
    }

    //SETTERI
    public void setId(int id) { this.id = id; }

    public void setBrand(String brand) { this.brand = brand; }

    public void setModel(String model) { this.model = model; }

    public void setYear(int year) { this.year = year; }

    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public void setSpecs(CarSpecs specs) { this.specs = specs; }


    //GETTERI
    public int getId() { return id; }

    public String getBrand() { return brand; }

    public String getModel() { return model; }

    public int getYear() { return year; }

    public double getPricePerDay() { return pricePerDay; }

    public CarSpecs getSpecs() { return specs; }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(id).append(", Brand: ").append(brand).append(", model: ").append(model).append(
                ", Year: ").append(year).append (", PricePerDay: ").append (pricePerDay).append(", RON").append(", Specs: " ).append(specs);
        return sb.toString();
    }
}
