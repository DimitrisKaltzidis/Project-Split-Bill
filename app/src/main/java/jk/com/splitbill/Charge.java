package jk.com.splitbill;

import java.util.ArrayList;

/**
 * Created by Jim on 13/2/2015.
 */
public class Charge {

    private String description;
    private double amount;
    private ArrayList<Integer> splitBetween = new ArrayList<Integer>();
    private double amountPerUser;

    public Charge(String description, ArrayList<Integer> splitBetween, double amount) {
        this.description = description;
        this.splitBetween = splitBetween;
        this.amount = amount;
        amountPerUser = amount / (double) splitBetween.size();
        ArrayList<User> temp = Methods.getUsers();
        for(int i =0;i<splitBetween.size();i++){
            temp.get(splitBetween.get(i)).setAmount(temp.get(splitBetween.get(i)).getAmount() + amountPerUser);
            System.out.println( temp.get(splitBetween.get(i)).getAmount()+" "+temp.get(splitBetween.get(i)).getUserName());
        }

        Methods.setUsers(temp);
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

    public ArrayList<Integer> getSplitBetween() {
        return splitBetween;
    }

    public void setSplitBetween(ArrayList<Integer> splitBetween) {
        this.splitBetween = splitBetween;
    }

    public void deleteChargeFromUsersAmount(){


    }

    public double getAmountPerUser() {
        return amountPerUser;
    }

    public void setAmountPerUser(double amountPerUser) {
        this.amountPerUser = amountPerUser;
    }

    public ArrayList<User> getSplitBetweenUsersAsObjects(){
        ArrayList<User> temp = Methods.getUsers();
        ArrayList<User> toBeReturned = new ArrayList<User>();
        for(int i =0;i<splitBetween.size();i++){
            toBeReturned.add(temp.get(splitBetween.get(i)));
        }
        for (int i =0 ;i<toBeReturned.size();i++){
            System.out.println(toBeReturned.get(i).getUserName());
        }
        return toBeReturned;
    }
}
