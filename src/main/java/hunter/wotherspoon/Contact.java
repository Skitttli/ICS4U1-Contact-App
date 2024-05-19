package hunter.wotherspoon;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class Contact {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty initials;
    private SimpleStringProperty address;
    private SimpleStringProperty birthday;
    private SimpleStringProperty company;
    private Color profileColour;
    //private int numPhoneNumbers;
    private ArrayList<String> phoneNumberList = new ArrayList<>();

    public Contact(String firstName,String lastName,ArrayList<String> phoneNumbers,String email,String address,String birthday,String company){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmail(email);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
    }

    public void setCompany(String company){
        this.company = new SimpleStringProperty(company);
    }
    public void setBirthday(String birthday){
        this.birthday = new SimpleStringProperty(birthday);
    }
    public void setAddress(String address){
        this.address = new SimpleStringProperty(address);
    }
    public void setFirstName(String firstName){
        this.firstName = new SimpleStringProperty(firstName);
    }
    public void setLastName(String lastName){
        this.lastName = new SimpleStringProperty(lastName);
    }
    public void setEmail(String email){
        this.email=new SimpleStringProperty(email);
    }
    public void setPhoneList(ArrayList<String> phoneList){
        this.phoneNumberList = phoneList;
    }
    public void setInitials(String firstName,String lastName){
        char firstLetter = firstName.charAt(0);
        char lastLetter = lastName.charAt(0);
        initials = new SimpleStringProperty(firstLetter+lastLetter+"");
    }
    public void setProfileColour(){
        Random rand = new Random();
        switch (rand.nextInt(21)) {
            case 0:
                profileColour = Color.valueOf("#aa47bd");   
                break;
            case 1:
                profileColour = Color.valueOf("#7b1fa2");         
                break;
            case 2:
                profileColour = Color.valueOf("#77919d");         
                break;
            case 3: 
                profileColour = Color.valueOf("#455a65");        
                break;
            case 4: 
                profileColour = Color.valueOf("#ec417a");        
                break;
            case 5:  
                profileColour = Color.valueOf("#c2185d");       
                break;
            case 6: 
                profileColour = Color.valueOf("#5d6ac0");        
                break;
            case 7: 
                profileColour = Color.valueOf("#0388d2");        
                break;
            case 8:
                profileColour = Color.valueOf("#01569c");      
                break;
            case 9:
                profileColour = Color.valueOf("#0098a9");      
                break;
            case 10:
                profileColour = Color.valueOf("#00897b");      
                break;
            case 11:
                profileColour = Color.valueOf("#004d40");      
                break;
            case 12:
                profileColour = Color.valueOf("#6a9f39");      
                break;
            case 13:
                profileColour = Color.valueOf("#34691e");      
                break;
            case 14:
                profileColour = Color.valueOf("#8a6f63");
                break;
            case 15:
                profileColour = Color.valueOf("#5f4038");      
                break;
            case 16:
                profileColour = Color.valueOf("#7d57c1");      
                break;
            case 17:
                profileColour = Color.valueOf("#512da9");      
                break;
            case 18:
                profileColour = Color.valueOf("#ef6c00");      
                break;
            case 19:
                profileColour = Color.valueOf("#f84f20");      
                break;
            case 20:
                profileColour = Color.valueOf("#bf350b");      
                break;
        }

        
    }

    public ArrayList<String> getPhoneList(){
        return phoneNumberList;
    }  
    public String getCompany(){
        return company.get();
    }
    public String getBirthday(){
        return birthday.get();
    }
    public String getAddress(){
        return address.get();
    }
    public String getFirstName(){
        return firstName.get();
    }
    public String getLastName(){
        return lastName.get();
    }

    public String getEmail(){
        return email.get();
    }
    public String getInitials(){
        return initials.get();
    }
    @SuppressWarnings("exports")
    public Color getProfileColour(){
        return profileColour;
    }
    public String getFullName(){
        if(lastName.get().equals("N/A")){
            return firstName.get();
        } else if(firstName.get().equals("N/A")){
            return lastName.get();
        }else{
            return firstName.get()+" "+lastName.get();
        }
    }

    public void changeEverything(String firstName,String lastName,ArrayList<String> phoneNumbers,String email,String address,String birthday,String company){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmail(email);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
    }

}
