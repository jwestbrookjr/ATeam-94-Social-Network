////////////////////////////////////////////////////////////////////////////////
//
// Title:           (Main.java)
// Description:     (Contains Implementation for a Social Network GUI)
// Course:          (CS 400, Fall, 2019)
//
///////////////////////////////////////////////////////////////////////////////

package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main driver for the GUI application.
 * 
 * @author Sam Peaslee
 * @author Jon Westbrook
 *
 */
public class Main extends Application {
    
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 600;
    // Create a SocialNetwork Instance
    SocialNetwork socialNetwork = new SocialNetwork();

    // Will store the names of the friends of the central user.
    ObservableList<String> namesOfFriends;
    // Used to display the list of friends and makes them clickable.
    ListView<String> friendList = new ListView<String>();

    /**
     * Creates the user's GUI window.
     * 
     * @param centralUser - user of type GraphNode.
     * @param root        - base BorderPane
     * @param vbox        - base VBox.
     * @param hbox        - base HBox.
     */
    public void createUserDisplay(GraphNode centralUser, BorderPane root,
            VBox vbox, HBox hbox) {

        if (centralUser != null) {
            vbox.getChildren().clear();
            hbox.getChildren().clear();
            // Get list of friends for the central user.
            ArrayList<String> friends = centralUser.getFriends();
            // Get the user name of the centralUser.
            Text currentUser = new Text(centralUser.getUserName());
            currentUser.setFont(new Font("Arial", 14));
            // Add currentUser to the VBox.
            vbox.getChildren().add(currentUser);
            vbox.setSpacing(10);

            // Create a label.
            Label userLabel = new Label(
                    "" + centralUser.getUserName() + "'s friends:");
            userLabel.setFont(new Font("Arial", 14));
            // Add label to the HBox.
            hbox.getChildren().add(userLabel);

            // Set the the ObservableList to store the friends of the current
            // user.
            namesOfFriends = FXCollections.observableArrayList(friends);
            // Set the items of the ListView to the items in the ObservableList.
            friendList.setItems(namesOfFriends);
            // Add the ListView to the HBox.
            hbox.getChildren().add(friendList);
            // Set the maximum width and height of the ListView.
            friendList.setMaxSize(100, 100);
            // Makes the HBox's children line up in the center of the HBox.
            hbox.setAlignment(Pos.CENTER);
            // Add the HBox to the VBox.
            vbox.getChildren().add(hbox);
            // Set the VBox in the center of the BorderPane.
            root.setCenter(vbox);
        } else {
            // If the file does not specify a central user the below label will
            // be displayed.
            root.setCenter(new Label("File did not specify a central user."));
        }
    }

    /**
     * Starts creation of the GUI.
     * 
     * @param primaryStage - primary stage to set scenes upon.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        ///////////////////////////////////////////////////////////////////////
        Alert open = new Alert(AlertType.NONE, "Currently all the buttons are "
                + "functional. You will be prompted to save a file or not when "
                + "the GUI is exited.", ButtonType.OK);
        open.setTitle("A2 Message");
        open.showAndWait();
        ///////////////////////////////////////////////////////////////////////
        // INITIALIZING A BUNCH OF OBJECTS THAT ARE NEEDED TO CREATE THE GUI

        /*
         * On initial loading of the GUI the two lists below will be empty and
         * will not display the ObservableList that will hold all the users'
         * names in the current Social Network.
         */
        ObservableList<String> allUserNames = FXCollections
                .observableArrayList();
        // ListView is used to display user names in the GUI; also has built in
        // behavior that makes items of list clickable.
        ListView<String> allUserList = new ListView<String>();
        /// Sets the max width and height of the list.
        allUserList.setMaxSize(100, 150);
        
        /*
         * These Lists are used to display the names of every user currently in the 
         * graph. On the click of a name, selected user becomes the central user.
         */
        ObservableList<String> allUserNamesDisplayed = FXCollections
                .observableArrayList();
        ListView<String> allUsersDisplayedList = new ListView<String>();
        allUsersDisplayedList.setMaxSize(200, 125);
        allUsersDisplayedList.setItems(allUserNamesDisplayed);
        Text numOfUsersDisplayed = new Text("Number of Users: "+  allUserNamesDisplayed.size());
/////////////////////////////////////////////////////////////////////////////////
         
        // Pane used to construct the layout of the GUI.
        BorderPane root = new BorderPane();
        // Sets padding of items around border of the GUI window: top, right,
        // bottom, left.
        root.setPadding(new Insets(20, 20, 60, 20));
        root.setBackground(new Background(
                new BackgroundFill(Color.DARKOLIVEGREEN, null, null)));

        // Adds an icon to the GUI.
 //       ObservableList<String> mutualFriendsDisplayed = FXCollections
 //               .observableArrayList();
        ListView<String> mutualFriendsDisplayedList = new ListView<String>();
        mutualFriendsDisplayedList.setMaxSize(200, 125);
 //       mutualFriendsDisplayedList.setItems(mutualFriendsDisplayed);
        
        Text mutualFriendsText = new Text("Mutual Friends");
        VBox mutualFriendsVBox = new VBox();
        mutualFriendsVBox.getChildren().addAll(mutualFriendsText, mutualFriendsDisplayedList);
        mutualFriendsVBox.setSpacing(10);
        VBox imageBox = new VBox();
        /*
        try {
            Image highFive = new Image("application/highfive.png");
            ImageView iconPlace = new ImageView(highFive);
            imageBox.getChildren().addAll(iconPlace);
        } catch (Exception e) {
            System.out.println(System.getProperty("user.dir"));
            System.out.println(e.getMessage());
        }
        */ 
       
        imageBox.getChildren().addAll(mutualFriendsVBox, numOfUsersDisplayed, allUsersDisplayedList);
        imageBox.setSpacing(10);
    //    mutualFriendsVBox.setVisible(false);
        root.setRight(imageBox);
        

        /*
         * Put a VBox in the center of the pane and create an HBox. Both boxes
         * will be updated when createUserDisplay is called. Once a file is
         * loaded this VBox will hold the name of the current central user and
         * his or her list of friends. See createUserDisplay for details about
         * how these two boxes are used.
         */
        VBox vbox1 = new VBox();
        vbox1.setAlignment(Pos.CENTER);
        HBox hbox = new HBox();
        hbox.setSpacing(10);

////////////////////////////////////////////////////////////////////////////////
        // Create a new log file every time the GUI is started.
        // When the GUI is exited, the user will be asked if they want to save a
        // file or not. This log file is the file they can save.
        File logFile = new File("log.txt");
        FileWriter logFW = new FileWriter(logFile);
////////////////////////////////////////////////////////////////////////////////        

        /*
         * A FileChooser instance can be used to allow the user of the GUI to
         * upload a file from their computer. Clicking the button will open up
         * File Explorer. When a file is selected it will be loaded in and if
         * the central user is specified in the file, the user's info will be
         * displayed. Otherwise, current central user will stayed displayed.
         * Adding multiple files will just keep updating the Social Network with
         * the contents of the file.
         * 
         * If the file is not in the proper format a pop up window will be
         * displayed that says "Error Invalid File".
         * 
         * The following webpage was consulted for information on the
         * FileChooser class.
         * https://www.geeksforgeeks.org/javafx-filechooser-class/
         * 
         */

        VBox fileBox = new VBox();
        fileBox.setSpacing(10);
        FileChooser fileChooser = new FileChooser();
        Button fileButton = new Button("Upload File");
        Label fileAdd = new Label("Select file to update the Social Network.");
        fileAdd.setFont(new Font("Arial", 14));

        fileBox.getChildren().addAll(fileAdd, fileButton);

        EventHandler<ActionEvent> updateSociallNetwork = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {

                // Get the file selected by the GUI user.
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    // Updates the SocialNetwork to store current central user.
                   
                    try {// If an error occurs when trying to create
                            // a new Social Network with the file the GUI
                            // user chose then do not update the social
                            // network and have a pop up window display.
                        SocialNetwork newSN = new SocialNetwork();
                        newSN.createSocialNetWork(file);
                        socialNetwork.createSocialNetWork(file);
                        socialNetwork.updateLogFile(file,logFW );
                    } catch (Exception ex) {
                        // Displays an alert that tells the user their
                        // file was not valid.
                        Alert a = new Alert(AlertType.NONE);
                        // Adds a button to allow user to close alert.
                        a.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        a.setTitle("Error");
                        a.setContentText(
                                "Invalid file: Social Network not updated.");
                        a.showAndWait();
                        // Exit method.
                        return;
                    }
                    // If the central user is changed by the file then
                    // what is displayed on the GUI will be updated.
                    if (socialNetwork.getCentralUser() != null) {
                        // Clear the HBox and VBox that are currently displaying
                        // the central user.
                        hbox.getChildren().clear();
                        vbox1.getChildren().clear();
                        // Updates what is being displayed on the GUI with the
                        // updated SocialNetwork created by the file the user
                        // chooses.
                        createUserDisplay(socialNetwork.getCentralUser(), root,
                                vbox1, hbox);

                    } else {
                        // If no central users set, display a list of all users.
                        // Clear ObservableList then add all user names in
                        // the social network. Clear all names first so they
                        // don't show up twice.
                        allUserNames.removeAll(
                                socialNetwork.getGraph().getAllUsers());
                        allUserNames
                                .addAll(socialNetwork.getGraph().getAllUsers());
                        allUserList.setItems(allUserNames);
                        // No longer needed root.setCenter(allUserList);
                        
                    }
                    allUserNamesDisplayed
                            .addAll(socialNetwork.getGraph().getAllUsers());
                    allUsersDisplayedList.setItems(allUserNamesDisplayed);
                    numOfUsersDisplayed.setText("Number of Users: "+  allUserNamesDisplayed.size());
                    root.setRight(imageBox);
                }
            }
        };


        // When the button is pressed the method above will execute.
        fileButton.setOnAction(updateSociallNetwork);
        fileBox.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(fileBox);
////////////////////////////////////////////////////////////////////////////////
        /*
         * Attempt to display mutaul friends
         */
        
////////////////////////////////////////////////////////////////////////////////
        // GIVING THE USER OPTIONS TO REMOVE AND ADD PEOPLE TO THE SOCIAL
        // NETWORK USING TextFields AND Buttons

        /*
         * vbox2 holds the TextFields, Buttons, and Labels that allow the Social
         * Network to be updated via the GUI. vbox2 is displayed on the left
         * side of the GUI. A user can be added and removed and a new central
         * user can be chosen as well.
         */

        // Adding TextFields, Buttons, labels to a VBox.
        VBox vbox2 = new VBox();
        vbox2.setPadding(new Insets(10));
        vbox2.setSpacing(10);

        Button addUserBTN = new Button("Add User");
        Label addUserLabel = new Label("Add user:");
        addUserLabel.setFont(new Font("Arial", 14));
        TextField addUser = new TextField();
        // Clears the TextField when a user clicks on it with a
        // mouse.
        addUser.setMaxWidth(100);

        Button removeUserBTN = new Button("Remove User");
        Label removeUserLabel = new Label("Remove user:");
        removeUserLabel.setFont(new Font("Arial", 14));
        TextField removeUser = new TextField();

        removeUser.setMaxWidth(100);

        Button setCentralUser = new Button("Set Central User");
        Label setCentralLabel = new Label("View user:");
        setCentralLabel.setFont(new Font("Arial", 14));
        TextField setUser = new TextField();

        setUser.setMaxWidth(100);

        Button addFriendship = new Button("Add Friendship");
        Label addFriendLabel = new Label("Add friendship:");
        addFriendLabel.setFont(new Font("Arial", 14));
        TextField addFriend1 = new TextField();

        TextField addFriend2 = new TextField();

        addFriend1.setMaxWidth(100);
        addFriend2.setMaxWidth(100);
        HBox friendBox = new HBox();
        friendBox.setSpacing(5);
        friendBox.getChildren().addAll(addFriend1, addFriend2);

        Button removeFriend = new Button("Remove Friendship");
        Label removeLabel = new Label("Remove friendship:");
        removeLabel.setFont(new Font("Arial", 14));
        TextField removeFriend1 = new TextField();
        TextField removeFriend2 = new TextField();

        removeFriend1.setMaxWidth(100);
        removeFriend2.setMaxWidth(100);
        HBox removeBox = new HBox();
        removeBox.setSpacing(5);
        removeBox.getChildren().addAll(removeFriend1, removeFriend2);
       
        VBox addUserVBox = new VBox();
        addUserVBox.setSpacing(10);
        addUserVBox.getChildren().addAll(addUserLabel, addUser, addUserBTN);
        VBox removeUserVBox = new VBox();
        removeUserVBox.setSpacing(10);
        removeUserVBox.getChildren().addAll(removeUserLabel, removeUser, removeUserBTN);

        HBox addRemoveBox = new HBox();
        addRemoveBox.setSpacing(10);
        addRemoveBox.getChildren().addAll(addUserVBox,removeUserVBox);
        
        Button mutualFriend = new Button("List Mutual Friends");
        Label mutualLabel = new Label("List Mutual Friends");
        mutualLabel.setFont(new Font("Arial", 14));
        TextField mutualFriend1 = new TextField();
        TextField mutualFriend2 = new TextField();
        mutualFriend1.setMaxWidth(100);
        mutualFriend2.setMaxWidth(100);
        HBox mutualFriendBox = new HBox();
        mutualFriendBox.setSpacing(5);
        mutualFriendBox.getChildren().addAll(mutualFriend1, mutualFriend2);
        
        vbox2.getChildren().addAll(addRemoveBox,
                setCentralLabel,setUser, setCentralUser, addFriendLabel, friendBox,
                addFriendship, removeLabel, removeBox, removeFriend, mutualLabel,
                mutualFriendBox, mutualFriend);
        root.setLeft(vbox2);

        // A pop up window to let the user of the GUI know if their attempt
        // to update the Social Network worked or not.
        Alert update = new Alert(AlertType.NONE);
        // Creates a button type OK to allow user to exit alert dialogue.
        update.getDialogPane().getButtonTypes().add(ButtonType.OK);

        /**
         * Event class which adds a user to the social network based on a GUI
         * action.
         * 
         * @author Sam Peaslee
         * @author Jon Westbrook
         *
         */
        class RegisterAction implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                if (!addUser.getText().isBlank()) {
                    
                    String[] words = { "a", addUser.getText().trim().toLowerCase() };             
                    try {
                        int numberOfUsers = socialNetwork.getGraph().order();
                        // If user is already in the social network, do not add
                        // user; alert GUI user and exit method.
                        if (socialNetwork.getGraph()
                                .search(addUser.getText().trim().toLowerCase()) != null) {
                            update.setTitle("Duplicate User");
                            update.setContentText("User " + addUser.getText().trim().toLowerCase()
                                    + " already in the Social Network.");
                            update.showAndWait();
                            addUser.clear();
                            return;
                        }

                        socialNetwork.updateSocialNetwork("a", words);
                        // If the number of users in the social network
                        // increases, the new user was added; alert the GUI
                        // user.
                        if (numberOfUsers == socialNetwork.getGraph().order()
                                - 1) {
                            update.setTitle("User Added");
                            update.setContentText("User " + addUser.getText().trim().toLowerCase()
                                    + " added to Social Network.");
                            update.showAndWait();
                            //Add command to the log file 
                            socialNetwork.updateLogFile(words, logFW);
                            
                        }
                        // Update the ObservableList containing all users.
                        if (!allUserNames.contains(addUser.getText().trim().toLowerCase())) {
                            allUserNames.add(addUser.getText().trim().toLowerCase());
                            allUserNamesDisplayed.add(addUser.getText().trim().toLowerCase());
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    numOfUsersDisplayed.setText("Number of Users: "+  allUserNamesDisplayed.size());
                }
                addUser.clear();
            }
        }

        /*
         * When the addUserBTN is pressed or enter is pressed in the addUser
         * TextField, calls an event handler.
         */
        addUserBTN.setOnAction(new RegisterAction());
        addUser.setOnAction(new RegisterAction());

        /*
         * When the removeUserBTN is pressed a user is removed if present in the
         * social network; also removes all edges (friendships) of the user.
         */
        class RemoveAction implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                if (!removeUser.getText().isBlank()) {
                    String[] words = { "r", removeUser.getText().trim().toLowerCase() };
                    try {
                        // Number of people in the social network.
                        int numberOfUsers = socialNetwork.getGraph().order();
                        // Look at updateSocialNetwork for the details of this
                        // method.
                        socialNetwork.updateSocialNetwork("r", words);
                        // If number of users decreases by one then the user
                        // is found and removed from the social network.
                        // GUI user is alerted.
                        if (numberOfUsers - 1 == socialNetwork.getGraph()
                                .order()) {
                            update.setTitle("User Removed");
                            update.setContentText(removeUser.getText().trim().toLowerCase()
                                    + " removed from the Social Network.");
                            update.showAndWait();
                            //Add command to the log file 
                            socialNetwork.updateLogFile(words, logFW);
                        } else {
                            // User not found, no one was removed from the
                            // social network.
                            update.setTitle("User Not Removed");
                            update.setContentText(removeUser.getText().trim().toLowerCase()
                                    + " was not found in the Social Network.");
                            update.showAndWait();
                        }
                        // Updating the ObservableList allUserNames
                        // in the GUI if necessary.
                        if (allUserNames.contains(removeUser.getText().trim().toLowerCase())) {
                            allUserNames.remove(removeUser.getText().trim().toLowerCase());
                            allUserNamesDisplayed.remove(removeUser.getText().trim().toLowerCase());
                        }
                        // If the current list of friends being displayed
                        // contains the user that was just removed, update
                        // the list.
                        if (namesOfFriends.contains(removeUser.getText().trim().toLowerCase())) {
                            namesOfFriends.remove(removeUser.getText().trim().toLowerCase());
                        }
                        // If current central user is deleted, replace the user
                        // info being displayed with "Removed Central User".
                        if (socialNetwork.getCentralUser().getUserName()
                                .equals(removeUser.getText().trim().toLowerCase())) {
                            root.setCenter(new Label("Removed central user."));
                        }
                    } catch (Exception e) {
                    }
                    numOfUsersDisplayed.setText("Number of Users: "+  allUserNamesDisplayed.size()); 
                }
                removeUser.clear();
            }
            
        }

        // Removes a user when GUI user clicks button or presses enter in text
        // field.
        removeUserBTN.setOnAction(new RemoveAction());
        removeUser.setOnAction(new RemoveAction());

        /*
         * When the addFriendship button is pressed a friendship is added if it
         * does not already exist and the input is valid.
         */
        class AddFriendAction implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                if (!addFriend1.getText().isBlank()
                        && !addFriend2.getText().isBlank()) {
                    String[] words = { "a", addFriend1.getText().trim().toLowerCase(),
                            addFriend2.getText().trim().toLowerCase() };
                    // If same user is entered twice, no friendship is added
                    // and an alert shows up.
                    if (addFriend1.getText().trim().toLowerCase().equals(addFriend2.getText().trim().toLowerCase())) {
                        update.setTitle("Error!");
                        update.setContentText(
                                "You entered the same user twice.");
                        update.showAndWait();
                        addFriend1.clear();
                        addFriend2.clear();
                        return;
                    }
                    try {
                        GraphNode friend1 = socialNetwork.getGraph()
                                .search(addFriend1.getText().trim().toLowerCase());
                        if (friend1 != null) {
                            // If friendship already exists between the two
                            // users do not create a new friendship; GUI user is
                            // alerted.
                            if (friend1.getFriends()
                                    .contains(addFriend2.getText().trim().toLowerCase())) {
                                update.setTitle("Friendship not added: ");
                                update.setContentText(addFriend1.getText().trim().toLowerCase()
                                        + " and " + addFriend2.getText().trim().toLowerCase()
                                        + " are already friends.");
                                update.showAndWait();
                                addFriend1.clear();
                                addFriend2.clear();
                                return;
                            }
                        }
                        // Initial number of edges in the graph.
                        int numberOfFriendships = socialNetwork.getGraph()
                                .size();
                        socialNetwork.updateSocialNetwork("a", words);
                        // If the number of edges is increased a new friendship
                        // was created, GUI user is alerted.
                        if (numberOfFriendships + 1 == socialNetwork.getGraph()
                                .size()) {
                            update.setTitle("Friendship added: ");
                            update.setContentText(addFriend1.getText().trim().toLowerCase() + " and "
                                    + addFriend2.getText().trim().toLowerCase()
                                    + " are now friends.");
                            update.showAndWait();
                            //Add command to the log file 
                            socialNetwork.updateLogFile(words, logFW);
                        }

                        // Updating the ObservableList allUserNames
                        // in the GUI if necessary.
                        if (!allUserNames.contains(addFriend1.getText().trim().toLowerCase())) {
                            allUserNames.add(addFriend1.getText().trim().toLowerCase());
                            allUserNamesDisplayed.add(addFriend1.getText().trim().toLowerCase());
                        }
                        if (!allUserNames.contains(addFriend2.getText().trim().toLowerCase())) {
                            allUserNames.add(addFriend2.getText().trim().toLowerCase());
                            allUserNamesDisplayed.add(addFriend2.getText().trim().toLowerCase());
                        }
                        // If either user in the new friendship is currently
                        // the central user (user being displayed)
                        // update the list of friends being displayed.
                        if (socialNetwork.getCentralUser() != null) {
                            if (socialNetwork.getCentralUser().getUserName()
                                    .equals(addFriend2.getText().trim().toLowerCase())) {
                                if (!namesOfFriends
                                        .contains(addFriend1.getText().trim().toLowerCase())) {
                                    namesOfFriends.add(addFriend1.getText().trim().toLowerCase());
                                }
                            }
                            if (socialNetwork.getCentralUser().getUserName()
                                    .equals(addFriend1.getText().trim().toLowerCase())) {
                                if (!namesOfFriends
                                        .contains(addFriend2.getText().trim().toLowerCase())) {
                                    namesOfFriends.add(addFriend2.getText().trim().toLowerCase());
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    numOfUsersDisplayed.setText("Number of Users: "+  allUserNamesDisplayed.size());
                }
                addFriend1.clear();
                addFriend2.clear();
            }
        }

        // Adds a friendship between two users if the button is pressed or enter
        // is pressed in the text field.
        addFriendship.setOnAction(new AddFriendAction());
        addFriend2.setOnAction(new AddFriendAction());

        /*
         * When the setCentral button is pressed, if the name in the TextField
         * setU is in the graph, the user's info is displayed. Otherwise
         * "User not found in Social Network" is displayed.
         */
        class SetCentralUser implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                GraphNode newCentralU = socialNetwork.getGraph()
                        .search(setUser.getText().trim().toLowerCase());
                if (newCentralU != null) {
                    socialNetwork.setCentralUser(newCentralU);
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
                    String[] words = {"s",setUser.getText().trim().toLowerCase()};                 
                   try {
                       //Add command to the log file 
                       socialNetwork.updateLogFile(words, logFW);
                   }catch(Exception e){
                       
                   }
                } else {
                    update.setTitle("User Not Found");
                    update.setContentText(setUser.getText().trim().toLowerCase()
                            + " not found in the Social Network.");
                    update.showAndWait();
                }
                setUser.clear();
            }
        }

        // Sets central user if button is pressed or enter is pressed in text
        // field.
        setCentralUser.setOnAction(new SetCentralUser());
        setUser.setOnAction(new SetCentralUser());

        /**
         * Class which implements the functionality to remove friendships.
         * 
         * @author Sam Peaslee
         * @author Jon Westbrook
         *
         */
        class RemoveFriendship implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                if (!removeFriend1.getText().isBlank()
                        && !removeFriend2.getText().isBlank()) {
                    String[] words = { "r", removeFriend1.getText().trim().toLowerCase(),
                            removeFriend2.getText().trim().toLowerCase() };
                    // If same user is entered twice, no friendship is removed
                    // and an alert shows up.
                    if (removeFriend1.getText().trim().toLowerCase()
                            .equals(removeFriend2.getText().trim().toLowerCase())) {
                        update.setTitle("Error!");
                        update.setContentText(
                                "You entered the same user twice.");
                        update.showAndWait();
                        removeFriend1.clear();
                        removeFriend2.clear();
                        return;
                    }

                    // Initial number of edges in the graph.
                    int numberOfFriendships = socialNetwork.getGraph().size();

                    try {
                        GraphNode friendA = socialNetwork.getGraph()
                                .search(removeFriend1.getText().trim().toLowerCase());
                        if (friendA != null) {
                            // If friendship exists between the two entered
                            // names, need to remove the friendship.
                            if (friendA.getFriends()
                                    .contains(removeFriend2.getText().trim().toLowerCase())) {
                                socialNetwork.updateSocialNetwork("r", words);
                                update.setTitle("Friendship removed: ");
                                update.setContentText(removeFriend1.getText().trim().toLowerCase()
                                        + " and " + removeFriend2.getText().trim().toLowerCase()
                                        + " are no longer friends.");
                                update.showAndWait();
                                //Add command to the log file 
                                socialNetwork.updateLogFile(words, logFW);
                            }
                        }
                        // If the number of edges is the same, a friendship
                        // was not removed, GUI user is alerted.
                        if (numberOfFriendships == socialNetwork.getGraph()
                                .size()) {
                            update.setTitle("Friendship not removed: ");
                            update.setContentText(removeFriend1.getText().trim().toLowerCase()
                                    + " and " + removeFriend2.getText().trim().toLowerCase()
                                    + " friendship does not exist.");
                            update.showAndWait();
                            removeFriend1.clear();
                            removeFriend2.clear();
                            return;
                        }

                        // Updating the ObservableList allUserNames
                        // in the GUI if necessary.
                        if (!allUserNames.contains(removeFriend1.getText().trim().toLowerCase())) {
                            allUserNames.add(removeFriend1.getText().trim().toLowerCase());
                        }
                        if (!allUserNames.contains(removeFriend2.getText().trim().toLowerCase())) {
                            allUserNames.add(removeFriend2.getText().trim().toLowerCase());
                        }
                        // If either user in the friendship removed is currently
                        // the central user (user being displayed),
                        // updates the list of friends being displayed.
                        if (socialNetwork.getCentralUser() != null) {
                            if (socialNetwork.getCentralUser().getUserName()
                                    .equals(removeFriend2.getText().trim().toLowerCase())) {
                                if (namesOfFriends
                                        .contains(removeFriend1.getText().trim().toLowerCase())) {
                                    namesOfFriends
                                            .remove(removeFriend1.getText().trim().toLowerCase());
                                }
                            }
                            if (socialNetwork.getCentralUser().getUserName()
                                    .equals(removeFriend1.getText().trim().toLowerCase())) {
                                if (namesOfFriends
                                        .contains(removeFriend2.getText().trim().toLowerCase())) {
                                    namesOfFriends
                                            .remove(removeFriend2.getText().trim().toLowerCase());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert a = new Alert(AlertType.ERROR);
                        a.show();
                    }
                }
                removeFriend1.clear();
                removeFriend2.clear();
            }
        }

        // Removes friendship if button is pressed or enter is pressed in second
        // text field.
        removeFriend.setOnAction(new RemoveFriendship());
        removeFriend2.setOnAction(new RemoveFriendship());
///////////////////////////////////////////////////////////////////////////////
        /**
         * Class which implements the functionality to display mutual friendships.
         * 
         * @author Grant Hellenbrand
         * @author Alexander Bush
         *
         */
        class MutualFriendship implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent event) {
                if (!mutualFriend1.getText().isBlank()
                        && !mutualFriend2.getText().isBlank()) {
                    // If same user is entered twice, no mutual friends are displayed
                    // and an alert shows up.
                    if (mutualFriend1.getText().trim().toLowerCase()
                            .equals(mutualFriend2.getText().trim().toLowerCase())) {
                        update.setTitle("Error!");
                        update.setContentText(
                                "You entered the same user twice.");
                        update.showAndWait();
                        mutualFriend1.clear();
                        mutualFriend2.clear();
                        return;
                    }
                    
                    // Initial number of edges in the graph.
                    int numberOfFriendships = socialNetwork.getGraph().size();
                    
                    try {
                        GraphNode friendA = socialNetwork.getGraph()
                                .search(mutualFriend1.getText().trim().toLowerCase());
                        GraphNode friendB = socialNetwork.getGraph()
                                .search(mutualFriend2.getText().trim().toLowerCase());
                        if (friendA != null) {
                            // If friendship exists between the two entered
                            // names, need to remove the friendship.
                            if (friendA.getFriends()
                                    .contains(mutualFriend2.getText().trim().toLowerCase())) {
                                
                            }
                            ArrayList<String> mutualUsers = new ArrayList(friendA.getFriends());
                            mutualUsers.retainAll(friendB.getFriends());
                            
                            ObservableList<String> mutualUsersList = FXCollections
                                    .observableArrayList();
                            
                            mutualUsersList.addAll(mutualUsers);
                            
                            mutualFriendsDisplayedList.setItems(mutualUsersList);
                            
                            mutualFriendsText.setText("Mutual Friends between " 
                                    + mutualFriend1.getText().trim().toLowerCase() + " and " 
                                    + mutualFriend2.getText().trim().toLowerCase()
                                    + "\nNumber of Mutual Friends: " + mutualUsers.size());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert a = new Alert(AlertType.ERROR);
                        a.show();
                    }
                }
                mutualFriend1.clear();
                mutualFriend2.clear();
            }
        }

        // Displays Mutual friendships if button is pressed or enter is pressed in second
        // text field.
        mutualFriend.setOnAction(new MutualFriendship());
        mutualFriend2.setOnAction(new MutualFriendship());


        
///////////////////////////////////////////////////////////////////////////////
        // CODE TO UPDATE GUI WHEN NAMES IN THE TWO ListView INSTANCES ARE
        // CLICKED ON

        /*
         * This updates what user is displayed when a friend is clicked in the
         * currents users list of friends
         */

        friendList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                // This gets the index where the clicked name is stored
                // in the list. Clicking on the list where no name is stored
                // results in a negative number being returned.
                int indexOfFriend = friendList.getSelectionModel()
                        .getSelectedIndex();
                //
                if (indexOfFriend < 0) {
                    // Did not click on a name, DO NOT change what's displayed.
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    String[] words = {"s",namesOfFriends.get(indexOfFriend)};                 
                    try {
                        //Add command to the log file 
                        socialNetwork.updateLogFile(words, logFW);
                    }catch(Exception e){
                        
                    }
                    socialNetwork.setCentralUser(socialNetwork.getGraph()
                            .search(namesOfFriends.get(indexOfFriend)));
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
                    
          
                }
            }
        });

        /*
         * This updates what user is displayed when a name is clicked in the
         * list that contains all users
         */
        allUserList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                // Same logic as EventHandler for friendList above.
                int indexOfFriend = allUserList.getSelectionModel()
                        .getSelectedIndex();
                if (indexOfFriend < 0) {
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    socialNetwork.setCentralUser(socialNetwork.getGraph()
                            .search(allUserNames.get(indexOfFriend)));
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
                }
            }
        });
        
        /*
         * This updates what user is displayed when a name is clicked in the
         * list that contains all users
         */
        allUsersDisplayedList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                // Same logic as EventHandler for friendList above.
                int indexOfFriend = allUsersDisplayedList.getSelectionModel()
                        .getSelectedIndex();
                if (indexOfFriend < 0) {
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    socialNetwork.setCentralUser(socialNetwork.getGraph()
                            .search(allUserNamesDisplayed.get(indexOfFriend)));
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
                }
            }
        });
        

////////////////////////////////////////////////////////////////////////////////
        // More Buttons and Alerts.
        ButtonType save = new ButtonType("Save File and Exit");
        ButtonType noSave = new ButtonType("Exit Without Save");
        Alert alertOnExit = new Alert(AlertType.NONE, "Do you want to save the"
                + " log file to your computer?\nRIGHT NOW IF YOU CHOSE TO SAVE A FILE"
                + " IT WILL JUST BE A BLANK TEXT FILE", save, noSave);
        alertOnExit.setTitle("Exiting Social Network");
        Alert alertOnSave = new Alert(AlertType.NONE,
                "File saved!\nThanks for" + " using the Social Network.",
                ButtonType.OK);
        alertOnSave.setTitle("Goodbye.");
        Alert alertOnNoSave = new Alert(AlertType.NONE,
                "Thanks for" + " using the Social Network.", ButtonType.OK);
        alertOnNoSave.setTitle("Goodbye.");

        /**
         * Event handler that executes when GUI is closed.
         * 
         * @author Sam Peaslee
         */
        EventHandler<WindowEvent> promptOnClose = new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                try {
                    logFW.close();
                } catch (IOException e) {
                }
                // Store what button was clicked in the alertOnExit alert.
                Optional<ButtonType> result = alertOnExit.showAndWait();

                // If the save button was clicked
                if (result.orElse(save) == save) {
                    // FileChooser to prompt user to save a file on their
                    // computer
                    File fileToSave = fileChooser.showSaveDialog(primaryStage);
                    // If the GUI users chooses a file.
                    if (fileToSave != null) {
                        try {
                            // Copy contents of the log file to the new file
                            // saved.
                            Files.copy(logFile.toPath(), fileToSave.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                            alertOnSave.showAndWait();
                        } catch (Exception e) {
                            // Some IOException; file not saved.
                            alertOnSave.setContentText("File Not Saved");
                            alertOnSave.showAndWait();
                        }
                    } else {
                        // GUI user decided not to save a file, hit cancel in
                        // file explorer.
                        alertOnSave.setContentText("File Not Saved");
                        alertOnSave.showAndWait();
                    }
                }
                if (result.orElse(noSave) == noSave) {
                    alertOnNoSave.showAndWait();
                }
            }
        };
        primaryStage.setOnCloseRequest(promptOnClose);

////////////////////////////////////////////////////////////////////////////////
        Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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

    
    private Label updateUserLabel(int size) {
        Label newLabel = new Label("Number of Users: " + size);
        return newLabel;
    }
}