package hunter.wotherspoon;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class App extends Application {

    private static Scene mainScene;
    private static Scene addScene;
    private static Scene viewScene;
    private static TableView<Contact> contactTable = new TableView<Contact>();
    private static TableColumn<Contact,String> nameCol = new TableColumn<>("Name");
    private static TableColumn<Contact,String> emailCol = new TableColumn<>("Email");
    public static final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private static BufferedReader csvReader;
    private static BufferedWriter csvWriter;
    private static String filePath = "src/main/java/hunter/wotherspoon/ContactSave.csv";


    
    public static void main(String[] args) {
        launch();
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {

        
        stage.setTitle("Contacts App");

        readData();
        initMainUI();
        stage.setScene(mainScene);
        stage.show();
    }

    @Override
    public void stop() throws IOException{
        writeData();
    }

    public static void writeData() throws IOException{
        Contact curContact;
        csvWriter = new BufferedWriter(new FileWriter(filePath, false));
        csvWriter = new BufferedWriter(new FileWriter(filePath, true));
        csvWriter.flush();
        for(int c = 0;c < contacts.size();c++){
            curContact = contacts.get(c);
            csvWriter.write(curContact.getFirstName()+","+curContact.getLastName()+",");
            ArrayList<String> curPhoneList = curContact.getPhoneList();
            for(String curPhoneNum:curPhoneList){
                csvWriter.write(curPhoneNum);
                if(curPhoneList.indexOf(curPhoneNum)!=curPhoneList.size()-1) csvWriter.write(":");
            }
            csvWriter.write(","+curContact.getEmail()+","+curContact.getAddress()+","+curContact.getBirthday()+","+curContact.getCompany());
            if(c!=contacts.size()-1) csvWriter.write("\n");
            }
            csvWriter.close();
    }

    public static void readData() throws IOException{
        contactTable.getItems().clear();
        csvReader = new BufferedReader(new FileReader(filePath));
        String curLine;
        while((curLine=csvReader.readLine())!=null){
            String[] curData = curLine.split(",");
            ArrayList<String> curPhonesList =new ArrayList<>(Arrays.asList(curData[2].split(":")));
            contacts.add(new Contact(curData[0], curData[1], curPhonesList, curData[3], curData[4], curData[5], curData[6]));
        }
    }


    @SuppressWarnings("unchecked")
    public static void initMainUI(){

        contactTable.setPlaceholder(new Label("Add Contacts For Table To Appear"));

        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("fullName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());

        contactTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        contactTable.setOnMousePressed(e->{
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {  
                    viewContact(contactTable.getSelectionModel().getSelectedItem());
                }
        });

        contactTable.getColumns().addAll(nameCol,emailCol);
        contactTable.setItems(contacts);

        Button addContactButton = new Button("+");
        addContactButton.setPrefSize(100, 20);

        addContactButton.setOnAction(e ->{
            newContact();
        });

        VBox mainVbox = new VBox();
        mainVbox.setSpacing(5);
        mainVbox.getChildren().addAll(contactTable,addContactButton);
        mainScene = new Scene(mainVbox, 400, 480);
    }

    //TODO Merge newContact with editContact
    public static void newContact(){
        Stage addStage = new Stage();

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();



        Label phoneNumberLabel = new Label("Phone Numbers");

        //TODO Make this sections its own method
        VBox phoneBox = new VBox();
        HBox resizeNumPhoneBox = new HBox();

        Button addPhone = new Button("Add Phone Number");
        Button deletePhone = new Button("Delete Phone Number");


        //TODO Make this not scuffed
        ArrayList<TextField> phoneNumberList = new ArrayList<>();

        addPhone.setOnAction(e->{
            TextField phoneField = new TextField();
            phoneNumberList.add(phoneField);
            phoneBox.getChildren().clear();
            for(TextField curField:phoneNumberList){
                curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)+1));
                phoneBox.getChildren().add(curField);
            }
        });

        //TODO Check work
         deletePhone.setOnAction(e->{
             phoneNumberList.remove(phoneNumberList.size()-1);

         });
        resizeNumPhoneBox.getChildren().addAll(addPhone,deletePhone);



        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();

        Label addressLabel = new Label("Address");
        TextField addressField = new TextField();

        Label birthdayLabel = new Label("Birthday");
        TextField birthdayField = new TextField();

        Label companyLabel = new Label("Company");
        TextField companyField = new TextField();

        Button saveContactButton = new Button("Save Contact");
        saveContactButton.setOnAction(e->{
            //TODO Make this not convert from textfield arraylist to string arraylist right here
            ArrayList<String> stringPhoneNums = new ArrayList<>();
            for(TextField curField:phoneNumberList){
                if(curField.getLength()!=0){
                    stringPhoneNums.add(curField.getText());
                }
            }

            contacts.add(new Contact(
                firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                stringPhoneNums,
                emailField.getLength() != 0?emailField.getText():"N/A",
                addressField.getLength() != 0?addressField.getText():"N/A",
                birthdayField.getLength() != 0?birthdayField.getText():"N/A",
                companyField.getLength() != 0?companyField.getText():"N/A"
            ));
            addStage.close();
        });

        //TODO Make this work
        Button deleteContactButton = new Button("Delete Contact");
        deleteContactButton.setOnAction(e->{
            addStage.close();
        });

        VBox addBox= new VBox();
        addBox.setSpacing(5);
        //addBox.getChildren().addAll(firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,phoneNumberField, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField,saveContactButton);
        addBox.getChildren().addAll(firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,resizeNumPhoneBox,phoneBox, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField,saveContactButton);
        addScene = new Scene(addBox,480,640);

        addStage.setScene(addScene);
        addStage.show();

    }

    //TODO Implement this
    public static void editContact(Contact curContact){
        
    }

    //TODO Remove setEditable Comments
    //TODO Move this to editContact, make a specific viewContact zone
    public static void viewContact(Contact curContact){
        Stage viewStage = new Stage();

        Label fullNameLabel = new Label(curContact.getFullName());
        fullNameLabel.setFont(new Font("Arial",24));
        fullNameLabel.setTextAlignment(TextAlignment.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField(curContact.getFirstName());
        //!firstNameField.setEditable(false);
        //!firstNameField.setMouseTransparent(true);
        firstNameField.setFocusTraversable(false);

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField(curContact.getLastName());
        //!lastNameField.setEditable(false);
        //!lastNameField.setMouseTransparent(true);
        lastNameField.setFocusTraversable(false);

        Label phoneNumberLabel = new Label("Phone Numbers");


        //TODO Make this not scuffed

        VBox phoneBox = new VBox();
        HBox resizeNumPhoneBox = new HBox();

        Button addPhone = new Button("Add Phone Number");
        Button deletePhone = new Button("Delete Phone Number");

        ArrayList<TextField> phoneNumberList = new ArrayList<>();
        
        for(String curPhoneNum:curContact.getPhoneList()){
            TextField curField = new TextField(curPhoneNum);
            phoneNumberList.add(curField);
            curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)));
            //!curField.setEditable(false);
            //!curField.setMouseTransparent(true);
            curField.setFocusTraversable(false);

            phoneBox.getChildren().add(curField);
        }

        addPhone.setOnAction(e->{
            TextField phoneField = new TextField();
            phoneNumberList.add(phoneField);
            phoneBox.getChildren().clear();
            for(TextField curField:phoneNumberList){
                curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)));
                phoneBox.getChildren().add(curField);
                
            }
        });

        //TODO Check work
         deletePhone.setOnAction(e->{
             phoneNumberList.remove(phoneNumberList.size()-1);
         });

        resizeNumPhoneBox.getChildren().addAll(addPhone,deletePhone);


        Label emailLabel = new Label("Email");
        TextField emailField = new TextField(curContact.getEmail());
        //!emailField.setEditable(false);
        //!emailField.setMouseTransparent(true);
        emailField.setFocusTraversable(false);

        Label addressLabel = new Label("Address");
        TextField addressField = new TextField(curContact.getAddress());
        //!addressField.setEditable(false);
        //!addressField.setMouseTransparent(true);
        addressField.setFocusTraversable(false);

        Label birthdayLabel = new Label("Birthday");
        TextField birthdayField = new TextField(curContact.getBirthday());
        //!birthdayField.setEditable(false);
        //!birthdayField.setMouseTransparent(true);
        birthdayField.setFocusTraversable(false);

        Label companyLabel = new Label("Company");
        TextField companyField = new TextField(curContact.getCompany());
        //!companyField.setEditable(false);
        //!companyField.setMouseTransparent(true);
        companyField.setFocusTraversable(false);

        Button saveContactButton = new Button("Save Changes");
        saveContactButton.setOnAction(e->{

            //TODO Make this not convert from textfield arraylist to string arraylist right here
            ArrayList<String> stringPhoneNums = new ArrayList<>();
            for(TextField curField:phoneNumberList){
                if(curField.getLength()!=0){
                    stringPhoneNums.add(curField.getText());
                }
            }

            curContact.changeEverything(
                firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                stringPhoneNums,
                emailField.getLength() != 0?emailField.getText():"N/A",
                addressField.getLength() != 0?addressField.getText():"N/A",
                birthdayField.getLength() != 0?birthdayField.getText():"N/A",
                companyField.getLength() != 0?companyField.getText():"N/A"
            );

            contactTable.refresh();
            viewStage.close();
        });

        Button deleteContactButton = new Button("Delete Contact");
        deleteContactButton.setOnAction(e->{
            contacts.remove(contacts.indexOf(curContact));
            viewStage.close();
        });

        VBox viewBox= new VBox();
        viewBox.setSpacing(5);
        viewBox.getChildren().addAll(fullNameLabel,firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,resizeNumPhoneBox,phoneBox, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField, saveContactButton,deleteContactButton);
        viewScene = new Scene(viewBox,480,640);

        viewStage.setScene(viewScene);
        viewStage.show();
    }  

}