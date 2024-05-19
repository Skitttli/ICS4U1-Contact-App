package oldVersion.wotherspoon;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


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
        for(int c = 0 ;c < contacts.size();c++){
            curContact = contacts.get(c);
            csvWriter.write(curContact.getFirstName()+","+curContact.getLastName()+","+curContact.getPhoneNumber()+","+curContact.getEmail()+","+curContact.getAddress()+","+curContact.getBirthday()+","+curContact.getCompany());
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
            contacts.add(new Contact(curData[0], curData[1], curData[2], curData[3], curData[4], curData[5], curData[6]));
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

    public static void newContact(){
        Stage addStage = new Stage();

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();

        Label phoneNumberLabel = new Label("Phone Number");
        TextField phoneNumberField = new TextField();

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
            contacts.add(new Contact(
                firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                phoneNumberField.getLength() != 0? phoneNumberField.getText() :"N/A",
                emailField.getLength() != 0?emailField.getText():"N/A",
                addressField.getLength() != 0?addressField.getText():"N/A",
                birthdayField.getLength() != 0?birthdayField.getText():"N/A",
                companyField.getLength() != 0?companyField.getText():"N/A"
            ));
            addStage.close();
        });

        VBox addBox= new VBox();
        addBox.setSpacing(5);
        //addBox.getChildren().addAll(firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,phoneNumberField, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField,saveContactButton);
        addBox.getChildren().addAll(firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,phoneNumberField, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField,saveContactButton);
        addScene = new Scene(addBox,480,640);

        addStage.setScene(addScene);
        addStage.show();

    }

    //TODO Implement this
    public static void editContact(Contact currentContact){
        
    }

    //TODO Remove setEditable
    public static void viewContact(Contact currentContact){
        Stage viewStage = new Stage();

        Label fullNameLabel = new Label(currentContact.getFullName());
        fullNameLabel.setFont(new Font("Arial",24));
        fullNameLabel.setTextAlignment(TextAlignment.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField(currentContact.getFirstName());
        //!firstNameField.setEditable(false);
        //!firstNameField.setMouseTransparent(true);
        firstNameField.setFocusTraversable(false);

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField(currentContact.getLastName());
        //!lastNameField.setEditable(false);
        //!lastNameField.setMouseTransparent(true);
        lastNameField.setFocusTraversable(false);

        Label phoneNumberLabel = new Label("Phone Number");
        TextField phoneNumberField = new TextField(currentContact.getPhoneNumber());
        //!phoneNumberField.setEditable(false);
        //!phoneNumberField.setMouseTransparent(true);
        phoneNumberField.setFocusTraversable(false);



        Label emailLabel = new Label("Email");
        TextField emailField = new TextField(currentContact.getEmail());
        //!emailField.setEditable(false);
        //!emailField.setMouseTransparent(true);
        emailField.setFocusTraversable(false);

        Label addressLabel = new Label("Address");
        TextField addressField = new TextField(currentContact.getAddress());
        //!addressField.setEditable(false);
        //!addressField.setMouseTransparent(true);
        addressField.setFocusTraversable(false);

        Label birthdayLabel = new Label("Birthday");
        TextField birthdayField = new TextField(currentContact.getBirthday());
        //!birthdayField.setEditable(false);
        //!birthdayField.setMouseTransparent(true);
        birthdayField.setFocusTraversable(false);

        Label companyLabel = new Label("Company");
        TextField companyField = new TextField(currentContact.getCompany());
        //!companyField.setEditable(false);
        //!companyField.setMouseTransparent(true);
        companyField.setFocusTraversable(false);

        Button saveContactButton = new Button("Save Changes");
        saveContactButton.setOnAction(e->{

            currentContact.changeEverything(
                firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                phoneNumberField.getLength() != 0? phoneNumberField.getText() :"N/A",
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
            contacts.remove(contacts.indexOf(currentContact));
            viewStage.close();
        });

        VBox viewBox= new VBox();
        viewBox.setSpacing(5);
        viewBox.getChildren().addAll(fullNameLabel,firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,phoneNumberField, emailLabel, emailField,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField, saveContactButton,deleteContactButton);
        viewScene = new Scene(viewBox,480,640);

        viewStage.setScene(viewScene);
        viewStage.show();
    }  

}