package application;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class Main extends Application {
    // Create a SocialNetwork Instance
    SocialNetwork socialNetwork = new SocialNetwork();

    // Will store the names of the friends of the central user.
    ObservableList<String> namesOfFriends;
    // Used to display the list of friend and makes them clickable.
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
     * Method that starts creation of the GUI.
     * 
     * @param primaryStage - primary stage to set scenes upon.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //////////////////////////////////////////////////////////////////////////////
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

        // Pane used to construct the layout of the GUI.
        BorderPane root = new BorderPane();
        // Sets padding of items around border of the GUI window: top, right,
        // bottom, left.
        root.setPadding(new Insets(20, 20, 60, 20));
        root.setBackground(new Background(
                new BackgroundFill(Color.DARKOLIVEGREEN, null, null)));

        // Adds an icon to the GUI.
        try {
            Image highFive = new Image(new FileInputStream("application/highfive.png"));
            ImageView iconPlace = new ImageView(highFive);

            VBox imageBox = new VBox();
            imageBox.getChildren().add(iconPlace);
            root.setRight(imageBox);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

///////////////////////////////////////////////////////////////////////////////
        // CREATING A BUTTON THAT ALLOWS THE GUI USER TO UPLOAD A FILE TO
        // CREATE A SOCIAL NETWORK. AN ERROR WINDOW WILL POP UP IF THE FILE
        // IS NOT VALID
        // MULTIPLE FILES CAN BE UPLOADED

        /*
         * A FileChooser Instance can be used to allow the user of the GUI to
         * upload a file from their computer Clicking the button will open up
         * File Explorer on Windows (I'm guessing it will open up finder on Mac)
         * When a file is selected it will be loaded in and if the central user
         * is specified in the file, the users info will be displayed Otherwise
         * current central user will stayed displayed Adding multiple files will
         * just keep updating the Social Network with the contents of the file
         * 
         * If the file is not in the proper format a pop up window will be
         * displayed that says "Error Invaild File"
         * 
         * I used the below web page as a reference
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
                    // Code in this if statement updates the SocialNetwork
                    // Store current central user
                    GraphNode currentCentralU = socialNetwork.getCentralUser();
                    try {// If an error occurs when trying to create
                            // a new Social Network with the file the GUI
                            // user chose then do not update the social
                            // network and have a pop up window display
                        SocialNetwork newSN = new SocialNetwork();
                        newSN.createSocialNetWork(file);
                        socialNetwork.createSocialNetWork(file);
                    } catch (Exception ex) {
                        // Displays an alert that tells the user their
                        // file was not valid
                        Alert a = new Alert(AlertType.NONE);
                        // Have to add a ButtonType.CANCEL or else
                        // you cannot exit the alert
                        a.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        a.setTitle("Error");
                        a.setContentText("Invalid file: Social Network " + "not"
                                + " updated");
                        a.showAndWait();
                        // Exit method
                        return;
                    }
                    // If the central user is changed by the file then
                    // what's displayed on the GUI will be updated
                    if (socialNetwork.getCentralUser() != currentCentralU) {
                        // Remove all names from the ObservableList if there

                        // Clear the HBox and VBox that are currently displaying
                        // the central user
                        hbox.getChildren().clear();
                        vbox1.getChildren().clear();
                        // Updates what's being displayed on the GUI with the
                        // updated SocialNetwork created by the file the user
                        // chose
                        createUserDisplay(socialNetwork.getCentralUser(), root,
                                vbox1, hbox);
                    }
                }
            }
        };

        // When the button is pressed the method above will execute
        fileButton.setOnAction(updateSociallNetwork);
        fileBox.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(fileBox);

////////////////////////////////////////////////////////////////////////////////
        // GIVING THE USER OPTIONS TO REMOVE AND ADD PEOPLE TO THE SOCIAL
        // NETWORK USING TextFields AND Buttons

        /*
         * vbox2 holds the TextFields,Buttons and Labels that allow the Social
         * Network to be updated via the GUI. vbox2 is displayed on the right
         * side of the GUI Right now a user can be added and removed A new
         * central user can be chosen as well
         * *******************************************************************
         * STILL NEED TO MAKE IT SO WE CAN: 1) REMOVE A FRIENDSHIP BETWEEN TWO
         * USERS
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
        addUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addUser.clear();
            }
        });
        addUser.setMaxWidth(100);

        Button removeUserBTN = new Button("Remove User");
        Label removeUserLabel = new Label("Remove user:");
        removeUserLabel.setFont(new Font("Arial", 14));
        TextField removeUser = new TextField();
        removeUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeUser.clear();
            }
        });
        removeUser.setMaxWidth(100);

        Button setCentralUser = new Button("Set Central User");
        Label setCentralLabel = new Label("View user:");
        setCentralLabel.setFont(new Font("Arial", 14));
        TextField setUser = new TextField();
        setUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setUser.clear();
            }
        });
        setUser.setMaxWidth(100);

        Button addFriendship = new Button("Add Friendship");
        Label addFriendLabel = new Label("Add friendship:");
        addFriendLabel.setFont(new Font("Arial", 14));
        TextField addFriend1 = new TextField();
        addFriend1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addFriend1.clear();
            }
        });
        TextField addFriend2 = new TextField();
        addFriend2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addFriend2.clear();
            }
        });
        addFriend1.setMaxWidth(100);
        addFriend2.setMaxWidth(100);
        HBox friendBox = new HBox();
        friendBox.setSpacing(5);
        friendBox.getChildren().addAll(addFriend1, addFriend2);
        
        
        Button viewAll = new Button("View All Users");

        vbox2.getChildren().addAll(addUserLabel, addUser, addUserBTN,
                removeUserLabel, removeUser, removeUserBTN, setCentralLabel,
                setUser, setCentralUser, addFriendLabel, friendBox,
                addFriendship, viewAll);
        root.setLeft(vbox2);

        // A pop up window to let the user of the GUI know if their attempt
        // to update the Social Network worked or not
        Alert update = new Alert(AlertType.NONE);
        // Creates a button type OK to allow user to exit alert dialogue.
        update.getDialogPane().getButtonTypes().add(ButtonType.OK);

        /*
         * When the addUserBTN is pressed a user is added to the SocialNetwork
         * If the user is already in the Social Network, nothing is added
         */
        addUserBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!addUser.getText().isBlank()) {
                    String[] words = { "a", addUser.getText() };
                    try {
                        int numberOfUsers = socialNetwork.getGraph().order();
                        // If user is already in the social network do not add
                        // user, alert gui user and exit method
                        if (socialNetwork.getGraph()
                                .search(addUser.getText()) != null) {
                            update.setTitle("Duplicate User");
                            update.setContentText("User " + addUser.getText()
                                    + " already in the Social Network");
                            update.showAndWait();
                            return;
                        }

                        // Look at updateSocialNetwork for the details of this
                        // method
                        socialNetwork.updateSocialNetwork("a", words);
                        // If the number of users in the social network
                        // increases
                        // the user was added, alert the gui user
                        if (numberOfUsers == socialNetwork.getGraph().order()
                                - 1) {
                            update.setTitle("User Added");
                            update.setContentText("User " + addUser.getText()
                                    + " added to " + "Social Network");
                            update.showAndWait();
                        }
                        // Update the ObservableList containing all users
                        if (!allUserNames.contains(addUser.getText())) {
                            allUserNames.add(addUser.getText());
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });

        /*
         * When the removeUserBTN is pressed a user is removed if present in the
         * social network, also removes all edges(friendships) of the user
         */
        removeUserBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!removeUser.getText().isBlank()) {
                    String[] words = { "r", removeUser.getText() };
                    try {
                        // Number of people in the social network
                        int numberOfUsers = socialNetwork.getGraph().order();
                        // Look at updateSocialNetwork for the details of this
                        // method
                        socialNetwork.updateSocialNetwork("r", words);
                        // If number of users decreases by one then the user
                        // was found and removed from the social network
                        // GUI user is alerted
                        if (numberOfUsers - 1 == socialNetwork.getGraph()
                                .order()) {
                            update.setTitle("User Removed");
                            update.setContentText(removeUser.getText()
                                    + " removed from the Social Network");
                            update.showAndWait();

                        } else {
                            // User not found, no one was removed from the
                            // social network
                            update.setTitle("User Not Removed");
                            update.setContentText(removeUser.getText()
                                    + " was not found in the Social Network.");
                            update.showAndWait();
                        }
                        // Updating the ObservableList allUserNames
                        // in the GUI if necessary
                        if (allUserNames.contains(removeUser.getText())) {
                            allUserNames.remove(removeUser.getText());
                        }
                        // If the current list of friend being displayed
                        // contains the user that was just removed update
                        // the list
                        if (namesOfFriends.contains(removeUser.getText())) {
                            namesOfFriends.remove(removeUser.getText());
                        }
                        // If current central user is deleted replace the user
                        // info being displayed with "Removed Central User"
                        if (socialNetwork.getCentralUser().getUserName()
                                .equals(removeUser.getText())) {
                            root.setCenter(new Label("Removed central user."));
                        }

                    } catch (Exception e) {

                    }
                }
            }
        });

        /*
         * When the addFriendship button is pressed a friendship is added if it
         * does not already exist and the input is valid
         */
        addFriendship.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!addFriend1.getText().isBlank()
                        && !addFriend2.getText().isBlank()) {
                    String[] words = { "a", addFriend1.getText(),
                            addFriend2.getText() };
                    // If same user is entered twice, no friendship is added
                    // and an alert shows up
                    if (addFriend1.getText().equals(addFriend2.getText())) {
                        update.setTitle("Error!");
                        update.setContentText(
                                "You entered the same user twice.");
                        update.showAndWait();
                        return;
                    }

                    try {
                        GraphNode friend1 = socialNetwork.getGraph()
                                .search(addFriend1.getText());
                        if (friend1 != null) {
                            // If friendship already exists between the two
                            // users
                            // do not create a new friendship, gui user is
                            // alerted
                            if (friend1.getFriends()
                                    .contains(addFriend2.getText())) {
                                update.setTitle("Friendship not added: ");
                                update.setContentText(addFriend1.getText()
                                        + " and " + addFriend2.getText()
                                        + " are already friends.");
                                update.showAndWait();
                                return;
                            }
                        }
                        // Initial number of edges in the graph
                        int numberOfFriendships = socialNetwork.getGraph()
                                .size();
                        // Look at updateSocialNetwork for the details of this
                        // method
                        socialNetwork.updateSocialNetwork("a", words);
                        // If the number of edges is increased a new friendship
                        // was created, gui user is alerted
                        if (numberOfFriendships + 1 == socialNetwork.getGraph()
                                .size()) {
                            update.setTitle("Friendship added: ");
                            update.setContentText(addFriend1.getText() + " and "
                                    + addFriend2.getText()
                                    + " are now friends.");
                            update.showAndWait();
                        }

                        // Updating the ObservableList allUserNames
                        // in the GUI if necessary
                        if (!allUserNames.contains(addFriend1.getText())) {
                            allUserNames.add(addFriend1.getText());
                        }
                        if (!allUserNames.contains(addFriend2.getText())) {
                            allUserNames.add(addFriend2.getText());
                        }
                        // If either user in the new friendship is currently
                        // the central user(user being displayed) need to update
                        // their list of friends being displayed
                        if (socialNetwork.getCentralUser().getUserName()
                                .equals(addFriend2.getText())) {
                            if (!namesOfFriends
                                    .contains(addFriend1.getText())) {
                                namesOfFriends.add(addFriend1.getText());
                            }
                        }
                        if (socialNetwork.getCentralUser().getUserName()
                                .equals(addFriend1.getText())) {
                            if (!namesOfFriends
                                    .contains(addFriend2.getText())) {

                                namesOfFriends.add(addFriend2.getText());
                            }
                        }

                    } catch (Exception e) {

                    }

                }
            }
        });

        /*
         * When the setCentral button is pressed if the name in the TextField
         * setU is in the graph the user's info is displayed Otherwise
         * "User not found in Social Network" is displayed
         */
        setCentralUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GraphNode newCentralU = socialNetwork.getGraph()
                        .search(setUser.getText());
                if (newCentralU != null) {
                    socialNetwork.setCentralUser(newCentralU);
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
                } else {
                    update.setTitle("User Not Found");
                    update.setContentText(setUser.getText()
                            + " not found in the Social Network.");
                    update.showAndWait();
                }
            }
        });
///////////////////////////////////////////////////////////////////////////////
        // Adding functionality to the button viewAll 
        // When clicked an alert pops us with a list of all users 
        Alert viewAllUsers = new Alert(AlertType.NONE);
        // Creates a button type OK to allow user to exit alert dialogue.
        viewAllUsers.getDialogPane().getButtonTypes().add(ButtonType.OK);
        viewAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                // Clear ObservableList then add all user names in
                // the social network
                // Need to clear first so names don't show up twice
                allUserNames
                        .removeAll(socialNetwork.getGraph().getAllUsers());
                allUserNames.addAll(socialNetwork.getGraph().getAllUsers());
                allUserList.setItems(allUserNames);
                viewAllUsers.setTitle("All Users");
                viewAllUsers.setGraphic(allUserList);  
                if(allUserNames.isEmpty()) {
                    viewAllUsers.setContentText("No Users in the Social Network");
                }
                viewAllUsers.showAndWait();
            }
        });
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
                // This gets the index that the clicked name is stored
                // in the list
                // If you click on the list where no name is a negative number
                // is returned
                int indexOfFriend = friendList.getSelectionModel()
                        .getSelectedIndex();
                //
                if (indexOfFriend < 0) {
                    // Did not click on a name, DO NOT change what's displayed
                } else {
                    hbox.getChildren().clear();
                    vbox1.getChildren().clear();
                    socialNetwork.setCentralUser(socialNetwork.getGraph()
                            .search(namesOfFriends.get(indexOfFriend)));
                    createUserDisplay(socialNetwork.getCentralUser(), root,
                            vbox1, hbox);
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
                // Same logic as EventHandler for friendList above
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

////////////////////////////////////////////////////////////////////////////////
        Scene mainScene = new Scene(root, 800, 600);
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
