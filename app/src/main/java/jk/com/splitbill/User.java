package jk.com.splitbill;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jim on 6/2/2015.
 */
public class User {

    private String userName, description;
    private int color;
    private double amount;
    private ArrayList<Charge> userCharges = new ArrayList<Charge>();

    public User(String userName, String description, int color, double amount) {
        this.userName = userName;
        this.description = description;
        this.color = color;
        this.amount = amount;
        Log.e("ADDED NEW USER","NAME: "+userName+" DESCRIPTION: "+description+" COLOR: "+color+" AMOUNT: "+amount);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public ArrayList<Charge> getUserCharges() {
        return userCharges;
    }

    public void setUserCharges(ArrayList<Charge> userCharges) {
        this.userCharges = userCharges;
    }

    public void addCharge(Charge newCharge){
        userCharges.add(newCharge);
    }

    public void removeCharge(Charge chargeToBeRemoved){
        System.out.println("Method Run");
        if((searchChargePosition(chargeToBeRemoved))!=-1) {
            amount = amount - chargeToBeRemoved.getAmountPerUser();
            userCharges.remove(searchChargePosition(chargeToBeRemoved));
            Log.e("REMOVED "+chargeToBeRemoved.getAmountPerUser(),"FROM USER "+this.userName);
        }
    }

    public int searchChargePosition(Charge chargeToBeRemoved){
        int toBeReturned = -1;
        for (int i=0;i<userCharges.size();i++){
            if(userCharges.get(i).getDescription().equals(chargeToBeRemoved.getDescription())){
                toBeReturned=i;
                break;
            }
        }
       return toBeReturned;
    }
}
