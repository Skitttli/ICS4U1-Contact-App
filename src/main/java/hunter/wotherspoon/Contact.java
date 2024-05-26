package hunter.wotherspoon;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Contact {
    private String firstName,lastName, address, birthday, company;
    private int profileColour;
    private ArrayList<String> phoneNumberList = new ArrayList<>();
    private ArrayList<String> emailList = new ArrayList<>();

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

    public void changeEverything(String firstName,String lastName,ArrayList<String> phoneNumbers,ArrayList<String> emails,String address,String birthday,String company){
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneList(phoneNumbers);
        setEmailList(emails);
        setAddress(address);
        setBirthday(birthday);
        setCompany(company);
    }
    public void setProfileColour(int profileColour){
        this.profileColour = profileColour;
    }
    public void setCompany(String company){
        this.company = company;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
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
        return company;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getAddress(){
        return address;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public ArrayList<String> getEmailList(){
        return emailList;
    }
    public ObservableValue<String> getFirstEmail(){
        if(emailList.size()==0){
            return new SimpleStringProperty("N/A");
        }else{
            return new SimpleStringProperty(emailList.get(0));
        }
    }
    public ArrayList<String> getPhoneList(){
        return phoneNumberList;
    }  
    public String getInitials(){
        if(lastName.equals("N/A")){
            return firstName.charAt(0)+"";
        } else if(firstName.equals("N/A")){
            return lastName.charAt(0)+"";
        }else{
            return firstName.charAt(0)+""+lastName.charAt(0)+" ";
        }
    }

    public String getFullName(){
        if(lastName.equals("N/A")){
            return firstName;
        } else if(firstName.equals("N/A")){
            return lastName;
        }else{
            return firstName+" "+lastName;
        }
    }


    public void removeCommas(){
        if(firstName.contains(",")){
            firstName = firstName.replaceAll(",","~~");
        }
        if(lastName.contains(",")){
            lastName = lastName.replaceAll(",","~~");
        }
        if(address.contains(",")){
            address = address.replaceAll(",","~~");
        }
        if(birthday.contains(",")){
            birthday = birthday.replaceAll(",","~~");
        }
        if(company.contains(",")){
            company = company.replaceAll(",","~~");
        }
        for(int i=0;i<emailList.size();i++){
            if(emailList.get(i).contains(",")){
                emailList.set(i,emailList.get(i).replaceAll(",","~~"));
            }
        }
        for(int i=0;i<phoneNumberList.size();i++){
            if(phoneNumberList.get(i).contains(",")){
                phoneNumberList.set(i,phoneNumberList.get(i).replaceAll(",","~~"));
            }
        }
    }

    public void addBackCommas(){
        if(firstName.contains("~~")){
            firstName = firstName.replaceAll("~~",",");
        }
        if(lastName.contains("~~")){
            lastName = lastName.replaceAll("~~",",");
        }
        if(address.contains("~~")){
            address = address.replaceAll("~~",",");
        }
        if(birthday.contains("~~")){
            birthday = birthday.replaceAll("~~",",");
        }
        if(company.contains("~~")){
            company = company.replaceAll("~~",",");
        }
    }
    
}
