package application;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
 
    // Create a SocialNetwork Instance
    SocialNetwork sn = new SocialNetwork();
 
    // Will store the names of the friends of the central user
    ObservableList<String> namesOfFriends; 
    // Used to display the list of friend and makes them clickable
    ListView<String> friendList  = new ListView<String>();

   
    public void createUserDisplay(GraphNode centralUser, BorderPane root, 
        VBox vbox, HBox hbox) {
        if(centralUser != null) {
            vbox.getChildren().clear();
            hbox.getChildren().clear();
            // Get list of friends for the central user   
            ArrayList<String> friends = centralUser.getFriends();
            //Get the user name of the centralUser 
            Text currentUser = new Text(centralUser.getUserName());
            // add currentUser to the vbox
            vbox.getChildren().add(currentUser);
            vbox.setSpacing(10);
            // Create a label
            Label label = new Label("" + centralUser.getUserName() + "'s Friends:");
            // Add label to the hbox
            hbox.getChildren().add(label);
            // Set the the ObservableList to store the friends of the current user
            namesOfFriends = FXCollections.observableArrayList(friends);
            // Set the items of the ListView to the items in the ObservableList
            friendList.setItems(namesOfFriends);
            // Add the ListView to the hbox
            hbox.getChildren().add(friendList);
            // Set the maximum width and hieght of the ListView
            friendList.setMaxSize(100, 100);
            // Makes the hbox's children line up in the center of the hbox
            hbox.setAlignment(Pos.CENTER);
            // Add the hbox to the vbox
            vbox.getChildren().add(hbox);
            // Set the vbox in the center of the BorderPane 
            root.setCenter(vbox);
        }else {
            // If the file does not specify a central user the below label will
            // be displayed 
            root.setCenter(new Label("File did not specify a central user"));
        }
    }
    
    

    @Override
    public void start(Stage primaryStage) throws Exception {
 //////////////////////////////////////////////////////////////////////////////
        //INITIALIZING A BUNCH OF OBJECTS THAT ARE NEEDED TO CREATE THE GUI
        
        /*
         * On initial loading of the GUI the two list below will be empty 
         * and will not be displayed
         * ObservableList that will hold all the users names in the current 
         * Social Network  
         */ 
        ObservableList<String> allUserNames = 
            FXCollections.observableArrayList();
        // ListView is used to display user names in the GUI, also has built in 
        // behavior that makes items of list clickable 
        ListView<String> allUserList = new ListView<String>();
        ///Sets the max width and height of the list
        allUserList.setMaxSize(100, 150);
        
        //Pane used to construct the layout of the GUI
        BorderPane root = new BorderPane();
        
        /*
         * Put a VBox in the center of the pane and create a HBox
         * Both boxes will be updated when createUserDisplay is called 
         * Once a file is loaded this VBox will hold the name of the current 
         * central user and its list of friends
         * Look at createUserDisplay to see the details about how these two 
         * boxes are used 
         */
        VBox vbox1 = new VBox();
        vbox1.setAlignment(Pos.CENTER);
        HBox hbox = new HBox();
        hbox.setSpacing(10);

///////////////////////////////////////////////////////////////////////////////
        // CREATING A BUTTON THAT ALLOWS THE GUI USER TO UPLOAD A FILE TO 
        // CREATE A SOCIAL NETWORK. AN ERROR WINDOW WILL POP UP IF THE FILE
        // IS NOT VALID
        // MULTIPLE FILES CAN BE UPLOADED
        
        /*
         * A FileChooser Instance can be used to allow the user of the GUI
         * to upload a file from their computer 
         * Clicking the button will open up File Explorer on Windows
         * (I'm guessing it will open up finder on Mac)
         * When a file is selected it will be loaded in and if the central user 
         * is specified in the file, the users info will be displayed
         * Otherwise current central user will stayed displayed  
         * Adding multiple files will just keep updating the Social Network 
         * with the contents of the file 
         * 
         * If the file is not in the proper format a pop up window will be 
         * displayed that says "Error Invaild File"
         * 
         * I used the below web page as a reference 
         * https://www.geeksforgeeks.org/javafx-filechooser-class/
         * 
         */

        VBox fileBox = new VBox();
        FileChooser fileChooser = new FileChooser();
        Button fileButton = new Button("Upload File");


        fileBox.getChildren().addAll(new Label("Upload File to"
            + " update Social Network"), fileButton);


        EventHandler<ActionEvent> updateSociallNetwork =
            new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {

                // Get the file selected by the GUI user
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    // Code in this if statement updates the SocialNetwork
                    // Store current central user 
                    GraphNode currentCentralU = sn.getCentralUser();
                    try {// If an error occurs when trying to create
                         // a new Social Network with the file the GUI
                         // user chose then do not update the social
                         // network and have a pop up window display
                        sn.createSocialNetWork(file);
                    } catch (Exception ex) {
                        // Displays an alert that tells the user their
                        // file was not valid
                        Alert a = new Alert(AlertType.NONE);
                        // Have to add a ButtonType.CANCEL or else
                        // you cannot exit the alert
                        a.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                        a.setTitle("Error");
                        a.setContentText("Invalid File: Social Network "
                            + "not" + " updated");
                        a.showAndWait();
                        // Exit method
                        return;
                    }
                    // If the central user is changed by the file then 
                    // what's displayed on the GUI will be updated
                    if (sn.getCentralUser() != currentCentralU) {
                        // Remove all names from the ObservableList if there
                       
                        // Clear the HBox and VBox that are currently displaying
                        // the central user
                        hbox.getChildren().clear();
                        vbox1.getChildren().clear();
                        // Updates what's being displayed on the GUI with the
                        // updated SocialNetwork created by the file the user 
                        // chose
                        createUserDisplay(sn.getCentralUser(), root, vbox1, hbox);
                    }
                    // Clear ObservableList then add all user names in 
                    // the social network
                    // Need to clear first so names don't show up twice
                    allUserNames.removeAll(sn.getGraph().getAllUsers());
                    allUserNames.addAll(sn.getGraph().getAllUsers());
                    allUserList.setItems(allUserNames);
                    root.setLeft(allUserList);

                }
            }
        };
        
        // When the button is pressed the method above will execute
        fileButton.setOnAction(updateSociallNetwork);
        fileBox.setAlignment(Pos.TOP_CENTER);
        root.setTop(fileBox);
        
////////////////////////////////////////////////////////////////////////////////
        // GIVING THE USER OPTIONS TO REMOVE AND ADD PEOPLE TO THE SOCIAL
        // NETWORK USING TextFields AND Buttons
        
        /*
         * vbox2 holds the TextFields,Buttons and Labels that allow the Social 
         * Network to be updated via the GUI. vbox2 is displayed on the right 
         * side of the GUI
         * Right now a user can be added and removed
         * A new central user can be chosen as well
         * *******************************************************************
         * STILL NEED TO MAKE IT SO WE CAN:
         * 1) ADD A FRIENDSHIP BETWEEN TWO USERS  
         * 2) REMOVE A FRIENDSHIP BETWEEN TWO USERS 
         */
        
        // Adding TextFields, Buttons, labels to a VBox
        VBox vbox2 = new VBox();      
        Button addUserBTN = new Button("Add User");
        TextField addUser = new TextField();
        Button removeUserBTN = new Button("Remove User");
        TextField removeUser = new TextField();
        Button setCentralUser = new Button("Set Central User");
        TextField setU = new TextField();
        vbox2.getChildren().addAll(new Label("Add User:"), addUser,
            addUserBTN, new Label("Remove User:"),
            removeUser, removeUserBTN,setU,new Label("View User"), setCentralUser);
        root.setRight(vbox2);
     
        /*
         * When the addUserBTN is pressed a user is added to the SocialNetwork
         * If the user is already in the Social Network, nothing is added
         */
        addUserBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 
                String[] words = {"a", addUser.getText()};
                try {
                    // Look at updateSocialNetwork for the details of this
                    // method 
                    sn.updateSocialNetwork("a", words);
                    if (!allUserNames.contains(addUser.getText())) {
                        allUserNames.add(addUser.getText());
                    }

                } catch (Exception e) {

                }
            }
        });

        /*
         * When the removeUserBTN is pressed a user is removed if present in 
         * the social network, also removes all edges(friendships) of the user
         */
        removeUserBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
                String[] words = {"r", removeUser.getText()};
                try {
                    // Look at updateSocialNetwork for the details of this
                    // method 
                    sn.updateSocialNetwork("r", words);
                    if (allUserNames.contains(removeUser.getText())) {             
                        allUserNames.remove(removeUser.getText());
                        if (namesOfFriends.contains(removeUser.getText())) {
                            namesOfFriends.remove(removeUser.getText());
                        }
                        System.out.println("Remove:  " + sn.getCentralUser().getUserName());
                        if(sn.getCentralUser().getUserName().
                            equals(removeUser.getText())) {
                            root.setCenter(new Label("Removed Central User From"
                                + "Social Network"));
                        }
                        
                    }

                } catch (Exception e) {

                }
            }
        });
        
        /*
         * When the setCentral button is pressed if the name in the
         * TextField setU is in the graph the user's info is displayed
         * Otherwise "User not found in Social Network" is displayed
         */
        setCentralUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
                GraphNode newCentralU = sn.getGraph().search(setU.getText());
                if(newCentralU != null) {
                    sn.setCentralUser(newCentralU);
                    createUserDisplay(sn.getCentralUser(), root, vbox1, hbox);
                }else {
                    root.setCenter(new Label("User not found in Social Network"));
                }
            }
        });
///////////////////////////////////////////////////////////////////////////////
        // CODE TO UPDATE GUI WHEN NAMES IN THE TWO ListView INSTANCES ARE
        // CLICKED ON
        
        /*
         * This updates what user is displayed when a friend is clicked in 
         * the currents users list of friends
         */

        friendList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                // This gets the index that the clicked name is stored
                // in the list
                // If you click on the list where no name is a negative number
                // is returned
                int indexOfFriend = friendList.getSelectionModel().getSelectedIndex();
                //
                if (indexOfFriend < 0) {
                    // Did not click on a name, DO NOT change what's displayed
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    sn.setCentralUser(sn.getGraph().search(namesOfFriends.get(indexOfFriend)));
                    createUserDisplay(sn.getCentralUser(), root, vbox1, hbox);
                }

            }

        });

        /*
         * This updates what user is displayed when a named is click in the list 
         * that contains all users 
         */
        allUserList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                //Same logic as EventHandler for friendList above
                int indexOfFriend = allUserList.getSelectionModel().getSelectedIndex();
                if (indexOfFriend < 0) {        
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    sn.setCentralUser(sn.getGraph().search(allUserNames.get(indexOfFriend)));
                    createUserDisplay(sn.getCentralUser(), root, vbox1, hbox);
                }

            }

        });
        
////////////////////////////////////////////////////////////////////////////////
        Scene mainScene = new Scene(root, 500, 500);
        primaryStage.setTitle("Social Network");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}