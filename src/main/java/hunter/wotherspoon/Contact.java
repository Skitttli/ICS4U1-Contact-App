package hunter.wotherspoon;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty birthday;
    private SimpleStringProperty company;
    private int profileColour;
    //private int numPhoneNumbers;
    private ArrayList<String> phoneNumberList = new ArrayList<>();
    private ArrayList<String> emailList = new ArrayList<>();

    //TODO Remove this
    public Contact(String firstName,String lastName,ArrayList<String> phoneNumbers,String email,String address,String birthday,String company,int profileColour){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmail(email);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
        setProfileColour(profileColour);
    }

    public Contact(String firstName,String lastName,ArrayList<String> phoneNumbers,ArrayList<String> emails,String address,String birthday,String company,int profileColour){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmailList(emails);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
        setProfileColour(profileColour);
    }
    public void setProfileColour(int profileColour){
        this.profileColour = profileColour;
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
    public void setEmailList(ArrayList<String> emailList){
        this.emailList = emailList;
    }
    public void setPhoneList(ArrayList<String> phoneList){
        this.phoneNumberList = phoneList;
    }


    public int getProfileColourInt(){
        return profileColour;
    }
    public String getProfileColourHex(){
        switch (profileColour) {
            case 0:
                 return "#aa47bd";                   
            case 1:
                return "#7b1fa2";                         
            case 2:
                return "#77919d";                         
            case 3: 
                return "#455a65";                        
            case 4: 
                return "#ec417a";                        
            case 5:  
                return "#c2185d";                       
            case 6: 
                return "#5d6ac0";                       
            case 7: 
                return "#0388d2";                        
            case 8:
                return "#01569c";                      
            case 9:
                return "#0098a9";                      
            case 10:
                return "#00897b";      
            case 11:
                return "#004d40";                      
            case 12:
                return "#6a9f39";                      
            case 13:
                return "#34691e";                      
            case 14:
                return "#8a6f63";                
            case 15:
                return "#5f4038";                      
            case 16:
                return "#7d57c1";                      
            case 17:
                return "#512da9";                     
            case 18:
                return "#ef6c00";                      
            case 19:
                return "#f84f20";      
            case 20:
            default:
                return "#bf350b";                     
        }   
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
    public ArrayList<String> getEmailList(){
        return emailList;
    }
    public ArrayList<String> getPhoneList(){
        return phoneNumberList;
    }  
    public String getInitials(){
        if(lastName.get().equals("N/A")){
            return firstName.get().charAt(0)+"";
        } else if(firstName.get().equals("N/A")){
            return lastName.get().charAt(0)+"";
        }else{
            return firstName.get().charAt(0)+""+lastName.get().charAt(0)+" ";
        }
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

    //TODO Remove this
    public void changeEverything(String firstName,String lastName,ArrayList<String> phoneNumbers,String email,String address,String birthday,String company){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmail(email);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
    }

    public void changeEverything(String firstName,String lastName,ArrayList<String> phoneNumbers,ArrayList<String> emails,String address,String birthday,String company){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmailList(emails);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
    }

}
