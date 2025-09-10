package Model;

import java.util.List;

public class VIPClient extends Client
{

    private double discountRate = 15.0;


    public VIPClient(int id, String name,String address, String phoneNumber, String email,double discountRate)
    {
        super(id, name, address, phoneNumber, email);
        this.discountRate = discountRate;
    }

    public boolean hasLoyaltyDiscount(List<Reservation> reservations)
    {
        int count = 0;
        for(Reservation r : reservations)
        {
            if(r.getClient().getId() == this.getId())
            {
                count++;
            }
        }
        return count >= 3;
    }

    public double getDiscountRate()
    {
        return discountRate;
    }

}
