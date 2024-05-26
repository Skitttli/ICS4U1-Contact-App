package hunter.wotherspoon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class App extends Application {

    private static Scene mainScene;
    private static Scene addScene;
    private static Scene viewScene;
    private static TableView<Contact> contactTable = new TableView<Contact>();
    private static TableColumn<Contact,String> nameCol = new TableColumn<>("Name");
    private static TableColumn<Contact,String> emailCol = new TableColumn<>("Email");
    private static TableColumn<Contact,String> profileCol = new TableColumn<>("");
    public static final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private static BufferedReader csvReader;
    private static BufferedWriter csvWriter;
    private static String filePath = "src/main/java/hunter/wotherspoon/ContactSave.csv";
    private double xOffset;
    private double yOffset;
    private static VBox mainVbox;


    
    public static void main(String[] args) {
        launch();
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        
        stage.setTitle("Contacts App");

        readData();
        initMainUI();
        mainScene.getStylesheets().add(this.getClass().getResource("mainstyle.css").toExternalForm());
        mainScene.setFill(Color.TRANSPARENT);
        stage.setScene(mainScene);
        stage.initStyle(StageStyle.TRANSPARENT);

        mainVbox.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
            e.consume();
        });

        stage.show();
    }

    @Override
    public void stop() throws IOException{
        writeData();
    }

    /**
     * Method to write all of the Contact info to the csv file.
     * <p> First it clears the entire csv file with a buffered writer not using append so that no duplicates
     * are stored from a previous save
     * <p> Then it loops through the entire Observable List of {@link #contacts} writing every different value with 
     * commas in between and the arraylists being split with colons. Finally writing a next line and continuing on to
     * the next contact
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the csv file is not found
     */
    public static void writeData() throws IOException,FileNotFoundException{
        Contact curContact;
        csvWriter = new BufferedWriter(new FileWriter(filePath, false));
        csvWriter = new BufferedWriter(new FileWriter(filePath, true));
        csvWriter.flush();
        for(int c = 0;c < contacts.size();c++){
            curContact = contacts.get(c);
            curContact.removeCommas();
            csvWriter.write(curContact.getFirstName()+","+curContact.getLastName()+",");
            ArrayList<String> curPhoneList = curContact.getPhoneList();
            for(String curPhoneNum:curPhoneList){
                csvWriter.write(curPhoneNum);
                if(curPhoneList.indexOf(curPhoneNum)!=curPhoneList.size()-1) csvWriter.write(":");
            }
            csvWriter.write(",");
            ArrayList<String> curEmailList = curContact.getEmailList();
            for(String curEmail:curEmailList){
                csvWriter.write(curEmail);
                if(curEmailList.indexOf(curEmail)!=curEmailList.size()-1) csvWriter.write(":");
            }
            csvWriter.write(","+curContact.getAddress()+","+curContact.getBirthday()+","+curContact.getCompany()+","+curContact.getProfileColourInt());
            if(c!=contacts.size()-1) csvWriter.write("\n");
            }
            csvWriter.close();
    }

    /**
     * Method to read Contact info from the csv file and create new contacts for each of them.
     * <p> Reads each new line as a contact, and splits the line by commas. For phone numbers and emails where there can be multiple
     * it splits those sections by colons. It then creates a new contact with all the split data and then adds back the commas if any
     * were found
     * <p> Reads until it finds a line that is empty
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the csv file is not found
     */
    public static void readData() throws IOException,FileNotFoundException{
        contactTable.getItems().clear();
        csvReader = new BufferedReader(new FileReader(filePath));
        String curLine;
        while((curLine=csvReader.readLine())!=null){
            String[] curData = curLine.split(",");
            ArrayList<String> curPhonesList = new ArrayList<>(Arrays.asList(curData[2].split(":")));
            ArrayList<String> curEmailsList = new ArrayList<>(Arrays.asList(curData[3].split(":")));
            Contact curContact = new Contact(curData[0], curData[1], curPhonesList, curEmailsList, curData[4], curData[5], curData[6],Integer.parseInt(curData[7]));
            curContact.addBackCommas();
            contacts.add(curContact);
        }
    }

    /**
     * Initializes Main UI
     * <p>Creates the cell factories for each column, as well as telling the add contact and the exit button what to do.
     */
    @SuppressWarnings("unchecked")
    public void initMainUI(){

        contactTable.setPlaceholder(new Label("Add Contacts For Table To Appear"));

        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("fullName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(emailCell -> {return emailCell.getValue().getFirstEmail();});
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());

        profileCol.setMinWidth(50);
        profileCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("initials"));
        profileCol.setCellFactory(e-> new TableCell<Contact, String>(){
            @Override
            public void updateItem(String item, boolean empty) {
                // Always invoke super constructor.
                super.updateItem(item, empty);

                if (item == null || empty) {
                    getStyleClass().clear();
                    setStyle(null);
                    contactTable.refresh();
                } else {
                    setText(item);
                    this.setStyle("-fx-background-color: "+contacts.get(this.getIndex()).getProfileColourHex()+"; -fx-text-fill: white; -fx-font-size: 16px; -fx-alignment: CENTER;");
                }
            }
        });

        contactTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        contactTable.setOnMousePressed(e->{
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {  
                    viewContact(contactTable.getSelectionModel().getSelectedItem());
                }
        });

        contactTable.getColumns().addAll(profileCol,nameCol,emailCol);
        contactTable.setItems(contacts);

        Button addContactButton = new Button("+");
        addContactButton.getStyleClass().add("add-button");

        addContactButton.setOnAction(e ->{
            newContact();
        });

        Button exitButton = new Button();
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e->{
            Platform.exit();
        });

        mainVbox = new VBox();
        mainVbox.setSpacing(5);
        mainVbox.getChildren().addAll(exitButton,contactTable,addContactButton);
        mainVbox.getStyleClass().clear();
        mainVbox.getStyleClass().add("background-pane");

        mainVbox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            event.consume();
        });

        mainScene = new Scene(mainVbox, 480, 480);
    }

    /**
     * Initializes the UI for a new contact and handles everything within that page
     * <p> First it creates all the fields that are needed and their labels, including multiple phone numbers and emails.
     * It then tells the add new contact button to add the new contact to the list when it is created. It also handles input validation when adding a new contact
     * All of it is surrounded by a ScrollPane to ensure that it can be scrolled through and too many emails don't break it.
     */
    public void newContact(){
        Stage addStage = new Stage();

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField();
        firstNameField.setFocusTraversable(false);

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField();
        lastNameField.setFocusTraversable(false);

        Label phoneNumberLabel = new Label("Phone Numbers");
        VBox phoneBox = new VBox();
        HBox resizeNumPhoneBox = new HBox();

        Button addPhone = new Button("Add Phone Number");
        Button deletePhone = new Button("Delete Phone Number");

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

         deletePhone.setOnAction(e->{
             phoneNumberList.remove(phoneNumberList.size()-1);
             phoneBox.getChildren().clear();
             for(TextField curField:phoneNumberList){
                 curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)+1));
                 phoneBox.getChildren().add(curField);
             }
         });
        resizeNumPhoneBox.getChildren().addAll(addPhone,deletePhone);


        Label emailLabel = new Label("Emails");
        VBox emailBox = new VBox();
        HBox resizeNumEmailBox = new HBox();

        Button addEmail = new Button("Add Email");
        Button deleteEmail = new Button("Delete Email");

        ArrayList<TextField> emailList = new ArrayList<>();

        addEmail.setOnAction(e->{
            TextField emailField = new TextField();
            emailList.add(emailField);
            emailBox.getChildren().clear();
            for(TextField curField:emailList){
                curField.setPromptText("Email #"+(emailList.indexOf(curField)+1));
                emailBox.getChildren().add(curField);
            }
        });

         deleteEmail.setOnAction(e->{
             emailList.remove(emailList.size()-1);
             emailBox.getChildren().clear();
             for(TextField curField:emailList){
                 curField.setPromptText("Email #"+(emailList.indexOf(curField)+1));
                 emailBox.getChildren().add(curField);
             }
         });
        resizeNumEmailBox.getChildren().addAll(addEmail,deleteEmail);


        Label addressLabel = new Label("Address");
        TextField addressField = new TextField();
        addressField.setFocusTraversable(false);

        Label birthdayLabel = new Label("Birthday");
        TextField birthdayField = new TextField();
        birthdayField.setFocusTraversable(false);

        Label companyLabel = new Label("Company");
        TextField companyField = new TextField();
        companyField.setFocusTraversable(false);

        Label invalidLabel = new Label("This input is invalid, please try again\nNo Section may contain ~~\nEvery email must contain a @ and a . and cannot contain a comma a colon or a space\nEvery phone number must only contain digits 0-9");
        invalidLabel.setVisible(false);

        Button saveContactButton = new Button("Save Contact");
        saveContactButton.setOnAction(e->{
            ArrayList<String> stringPhoneNums = new ArrayList<>();
            for(TextField curField:phoneNumberList){
                if(curField.getLength()!=0){
                    stringPhoneNums.add(curField.getText());
                }
            }

            ArrayList<String> stringEmails = new ArrayList<>();
            for(TextField curField:emailList){
                if(curField.getLength()!=0){
                    stringEmails.add(curField.getText());
                }
            }

            if(checkInvalid(firstNameField.getText(), lastNameField.getText(), stringPhoneNums, stringEmails, addressField.getText(), birthdayField.getText(), companyField.getText())){
                invalidLabel.setVisible(true);
            }else{
                contacts.add(new Contact(
                    firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                    lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                    stringPhoneNums,
                    stringEmails,
                    addressField.getLength() != 0?addressField.getText():"N/A",
                    birthdayField.getLength() != 0?birthdayField.getText():"N/A",
                    companyField.getLength() != 0?companyField.getText():"N/A",
                    new Random().nextInt(21)
                ));
                contactTable.refresh();
                addStage.close();
            }
        });

        Button exitButton = new Button();
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e->{
            addStage.close();
        });

        VBox addBox= new VBox();
        addBox.setSpacing(5);
        addBox.getChildren().addAll(exitButton,firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,resizeNumPhoneBox,phoneBox, emailLabel, resizeNumEmailBox,emailBox,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField,saveContactButton, invalidLabel);

        ScrollPane scrollPane = new ScrollPane(addBox);
        scrollPane.setFitToWidth(true);

        scrollPane.getStyleClass().add("background-pane");

        addScene = new Scene(scrollPane,480,640);

        addScene.getStylesheets().add(getClass().getResource("addstyle.css").toExternalForm());
        addScene.setFill(Color.TRANSPARENT);
        addStage.setScene(addScene);
        addStage.initStyle(StageStyle.TRANSPARENT);

        scrollPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            event.consume();
        });

        scrollPane.setOnMouseDragged(event -> {
            addStage.setX(event.getScreenX() - xOffset);
            addStage.setY(event.getScreenY() - yOffset);
            event.consume();
        });

        addStage.show();

    }

    /**
     * Initializes the UI for to view and edit a contact and handles everything within that page
     * <p> First it creates all the fields that are needed and their labels, including multiple phone numbers and emails.
     * It adds all existing data into these fields to be shown.
     * It then tells the save changes button to add the changes to the existing contact. Then refreshes the table.
     * Handles the delete contact button which removes the contact from the table. Then refreshes the table.
     * It also handles input validation event when editing fields that have already been created
     * All of it is surrounded by a ScrollPane to ensure that it can be scrolled through and too many emails don't break it.
     * 
     * @param curContact The current contact that should be viewed
     */
    public void viewContact(Contact curContact){
        Stage viewStage = new Stage();

        viewStage.setTitle(curContact.getFullName());

        Label fullNameLabel = new Label(curContact.getFullName());
        fullNameLabel.setFont(new Font("Arial",24));
        fullNameLabel.setTextAlignment(TextAlignment.CENTER);

        Label firstNameLabel = new Label("First Name");
        TextField firstNameField = new TextField(curContact.getFirstName());
        firstNameField.setFocusTraversable(false);

        Label lastNameLabel = new Label("Last Name");
        TextField lastNameField = new TextField(curContact.getLastName());
        lastNameField.setFocusTraversable(false);

        Label phoneNumberLabel = new Label("Phone Numbers");


        VBox phoneBox = new VBox();
        HBox resizeNumPhoneBox = new HBox();

        Button addPhone = new Button("Add Phone Number");
        Button deletePhone = new Button("Delete Phone Number");

        ArrayList<TextField> phoneNumberList = new ArrayList<>();
        
        for(String curPhoneNum:curContact.getPhoneList()){
            TextField curField = new TextField(curPhoneNum);
            phoneNumberList.add(curField);
            curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)));
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

         deletePhone.setOnAction(e->{
             phoneNumberList.remove(phoneNumberList.size()-1);
             phoneBox.getChildren().clear();
             for(TextField curField:phoneNumberList){
                 curField.setPromptText("Phone Number #"+(phoneNumberList.indexOf(curField)+1));
                 phoneBox.getChildren().add(curField);
             }
         });
        resizeNumPhoneBox.getChildren().addAll(addPhone,deletePhone);

        Label emailLabel = new Label("Emails");
        VBox emailBox = new VBox();
        HBox resizeNumEmailBox = new HBox();

        Button addEmail = new Button("Add Email");
        Button deleteEmail = new Button("Delete Email");

        ArrayList<TextField> emailList = new ArrayList<>();

        for(String curEmail:curContact.getEmailList()){
            TextField curField = new TextField(curEmail);
            emailList.add(curField);
            curField.setPromptText("Email #"+(emailList.indexOf(curField)));
            curField.setFocusTraversable(false);

            emailBox.getChildren().add(curField);
        }

        addEmail.setOnAction(e->{
            TextField emailField = new TextField();
            emailList.add(emailField);
            emailBox.getChildren().clear();
            for(TextField curField:emailList){
                curField.setPromptText("Email #"+(emailList.indexOf(curField)+1));
                emailBox.getChildren().add(curField);
            }
        });

         deleteEmail.setOnAction(e->{
             emailList.remove(emailList.size()-1);
             emailBox.getChildren().clear();
             for(TextField curField:emailList){
                 curField.setPromptText("Email #"+(emailList.indexOf(curField)+1));
                 emailBox.getChildren().add(curField);
             }
         });
        resizeNumEmailBox.getChildren().addAll(addEmail,deleteEmail);

        Label addressLabel = new Label("Address");
        TextField addressField = new TextField(curContact.getAddress());
        addressField.setFocusTraversable(false);

        Label birthdayLabel = new Label("Birthday");
        TextField birthdayField = new TextField(curContact.getBirthday());
        birthdayField.setFocusTraversable(false);

        Label companyLabel = new Label("Company");
        TextField companyField = new TextField(curContact.getCompany());
        companyField.setFocusTraversable(false);

        Label invalidLabel = new Label("This input is invalid, please try again\nNo Section may contain ~~\nEvery email must contain a @ and a . and cannot contain a comma a colon or a space\nEvery phone number must only contain digits 0-9");
        invalidLabel.setVisible(false);

        Button saveContactButton = new Button("Save Changes");
        saveContactButton.setOnAction(e->{
            ArrayList<String> stringPhoneNums = new ArrayList<>();
            for(TextField curField:phoneNumberList){
                if(curField.getLength()!=0){
                    stringPhoneNums.add(curField.getText());
                }
            }

            ArrayList<String> stringEmails = new ArrayList<>();
            for(TextField curField:emailList){
                if(curField.getLength()!=0){
                    stringEmails.add(curField.getText());
                }
            }

            if(checkInvalid(firstNameField.getText(), lastNameField.getText(), stringPhoneNums, stringEmails, addressField.getText(), birthdayField.getText(), companyField.getText())){
                invalidLabel.setVisible(true);
            }else{
                curContact.changeEverything(
                    firstNameField.getLength() != 0?firstNameField.getText():"N/A",
                    lastNameField.getLength() != 0? lastNameField.getText():"N/A",
                    stringPhoneNums,
                    stringEmails,
                    addressField.getLength() != 0?addressField.getText():"N/A",
                    birthdayField.getLength() != 0?birthdayField.getText():"N/A",
                    companyField.getLength() != 0?companyField.getText():"N/A"
                );
                contactTable.refresh();
                viewStage.close();
            }
        });

        Button deleteContactButton = new Button("Delete Contact");
        deleteContactButton.setOnAction(e->{
            contacts.remove(contacts.indexOf(curContact));
            viewStage.close();
        });

        Button exitButton = new Button();
        exitButton.getStyleClass().add("exit-button");
        exitButton.setOnAction(e->{
            viewStage.close();
        });

        VBox viewBox= new VBox();
        viewBox.setSpacing(5);
        viewBox.getChildren().addAll(exitButton,fullNameLabel,firstNameLabel,firstNameField,lastNameLabel,lastNameField,phoneNumberLabel,resizeNumPhoneBox,phoneBox, emailLabel, resizeNumEmailBox,emailBox,addressLabel,addressField,birthdayLabel,birthdayField,companyLabel,companyField, saveContactButton,deleteContactButton,invalidLabel);
        
        ScrollPane scrollPane = new ScrollPane(viewBox);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("background-pane");

        viewScene = new Scene(scrollPane,480,640);

        viewScene.getStylesheets().add(getClass().getResource("addstyle.css").toExternalForm());
        viewScene.setFill(Color.TRANSPARENT);
        viewStage.setScene(viewScene);
        viewStage.initStyle(StageStyle.TRANSPARENT);

        scrollPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            event.consume();
        });

        scrollPane.setOnMouseDragged(e -> {
            viewStage.setX(e.getScreenX() - xOffset);
            viewStage.setY(e.getScreenY() - yOffset);
            e.consume();
        });

        viewStage.show();
    }  

    /**
     * Checks if the data for the contact entered is invalid and has characters that are on the no no list.
     * Uses RegEx to check if there are matches or if there are characters contained
     * 
     * @return If invalid characters are in the strings, return true. Otherwise return false
     */
    public boolean checkInvalid(String firstName,String lastName,ArrayList<String> phoneNumberList,ArrayList<String> emailList,String address,String birthday,String company){
        if(firstName.contains("~~")||lastName.contains("~~")||address.contains("~~")||birthday.contains("~~")||company.contains("~~")){
            return true;
        }
        for(int i=0;i<emailList.size();i++){
            if(emailList.get(i).matches(".*[, :].*")||!(emailList.get(i).contains("@")&&emailList.get(i).contains("."))){
                return true;
            }
        }
        for(int i=0;i<phoneNumberList.size();i++){
            if(phoneNumberList.get(i).matches(".*\\D.*")&&!phoneNumberList.get(i).equals("N/A")){
                return true;
            }
        }
        return false;
    }
}