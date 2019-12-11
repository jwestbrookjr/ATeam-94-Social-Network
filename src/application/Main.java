package application;

////////////////////////////////////////////////////////////////////////////////
//
// Title:           (Main.java)
// Description:     (Contains Implementation for a Social Network GUI)
// Course:          (CS 400, Fall, 2019)
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
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
 * @authors Sam Peaslee, Jon Westbrook, Grant Hellenbrand, Alexander Bush, Cole
 *          Christophel
 */
public class Main extends Application {
	// Fields which dictate window size.
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = 650;
	// Creates a SocialNetwork Instance.
	SocialNetwork socialNetwork = new SocialNetwork();

	// Stores the names of the friends of the central user.
	ObservableList<String> namesOfFriends;
	// Displays the list of friends and makes them clickable.
	ListView<String> friendList = new ListView<String>();

///////////////////////////////////////////////////////////////////////////////
	/**
	 * This section dictates styles for this program.
	 */
	// CSS for left panel.
	String cssLeftPanelLayout = "-fx-background-color: #85C8F2;\n"
			+ "-fx-background-radius: 10 10 10 10;\n"
			+ "-fx-effect: dropshadow(three-pass-box, #2A558C, 20, 0, 0, 0);";

	// CSS for main border layout.
	String cssMainBorderLayout = "-fx-background-color: #D9D9D9;\n";

	// CSS for text fields.
	String cssTextStyle = "-fx-font-family: \"Verdana\";\n"
			+ "-fx-font-size: 14;\n";

	// CSS for title field.
	String cssTitleStyle = "-fx-font-family: \"Verdana\";\n"
			+ "-fx-font-size: 20;\n";

///////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates an HBox that shows the central user's name and list of friends.
	 * 
	 * @param centralUser - user of type GraphNode.
	 * @param root        - base BorderPane.
	 * @param vbox        - base VBox.
	 * @param hbox        - base HBox.
	 */
	public void createUserDisplay(GraphNode centralUser, BorderPane root,
			VBox vbox, HBox hbox) {

		// If central user is not null.
		if (centralUser != null) {
			// Clears both HBox's and VBox's children lists.
			vbox.getChildren().clear();
			hbox.getChildren().clear();

			// Get list of friends for the central user.
			ArrayList<String> friends = centralUser.getFriends();
			// Get the user name of the centralUser.
			Text currentUser = new Text(centralUser.getUserName());
			currentUser.setStyle(cssTextStyle);

			// Add currentUser to the VBox.
			vbox.getChildren().add(currentUser);
			vbox.setSpacing(10);

			// Create a label for the user.
			Label userLabel = new Label("" + centralUser.getUserName() + " has "
					+ centralUser.getFriends().size() + " friends:");
			userLabel.setStyle(cssTextStyle);
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
			// Aligns children to the center of the HBox.
			hbox.setAlignment(Pos.CENTER);

			// Add the HBox to the VBox.
			vbox.getChildren().add(hbox);
			// Set the VBox in the center of the BorderPane.
			root.setCenter(vbox);
		} else {
			// Else the file does not specify a central user. The below label
			// will
			// be displayed.
			root.setCenter(new Label("File did not specify a central user."));
		}
	}

///////////////////////////////////////////////////////////////////////////////

	/**
	 * Starts creation of the GUI.
	 * 
	 * @param primaryStage - primary stage to set scenes upon.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// BorderPane used to construct the layout of the GUI.
		BorderPane root = new BorderPane();

		// Creates a new label for a title and updates its style and padding.
		Label title = new Label("BadgerNetBook");
		title.setStyle(cssTitleStyle);
		title.setPadding(new Insets(5));

		// Creates new status text box and sets its style.
		Text statsText = new Text("Network consists of: "
				+ socialNetwork.getGraph().order() + " users, "
				+ socialNetwork.getGraph().size() + " friendships, and "
				+ socialNetwork.getGraph().numberOfGroups()
				+ " connected groups.\n");
		statsText.setStyle(cssTextStyle);

		// Creates a new VBox, adds title label and status text to it, and sets
		// in top of border pane.
		VBox topVBox = new VBox();
		topVBox.getChildren().addAll(title, statsText);
		topVBox.setAlignment(Pos.CENTER);
		root.setTop(topVBox);

		// Sets padding of items around border of the GUI window: top, right,
		// bottom, left.
		root.setPadding(new Insets(20, 20, 60, 20));
		root.setStyle(cssMainBorderLayout);

		// ObservableList/ListView combo for displaying all users.
		ObservableList<String> allUserNamesDisplayed = FXCollections
				.observableArrayList();
		ListView<String> allUsersDisplayedList = new ListView<String>();
		// Sets maximum window size in pixels.
		allUsersDisplayedList.setMaxSize(200, 125);
		// Adds items to this list.
		allUsersDisplayedList.setItems(allUserNamesDisplayed);
		Text numOfUsersDisplayed = new Text("Users In Network");
		// Updates style of text in this list.
		numOfUsersDisplayed.setStyle(cssTextStyle);

		// ObservableList/ListView combo for displaying mutual friends of two
		// users.
		ObservableList<String> mutualUsersList = FXCollections
				.observableArrayList();
		ListView<String> mutualFriendsDisplayedList = new ListView<String>();
		// Sets maximum window size and places items in list.
		mutualFriendsDisplayedList.setMaxSize(200, 125);
		mutualFriendsDisplayedList.setItems(mutualUsersList);

		// Creates a new text box and updates its style.
		Text mutualFriendsText = new Text("Mutual Friends");
		mutualFriendsText.setStyle(cssTextStyle);

		// Creates a new VBox, adds items to it, and updates its spacing.
		VBox mutualFriendsVBox = new VBox();
		mutualFriendsVBox.getChildren().addAll(mutualFriendsText,
				mutualFriendsDisplayedList);
		mutualFriendsVBox.setSpacing(10);

		// Creates a new VBox, adds
		VBox imageBox = new VBox();
		imageBox.setStyle(cssLeftPanelLayout);
		imageBox.setPadding(new Insets(10));

		// Creates a new button.
		Button clearSN = new Button("Clear Social Network");

		// Adds items to the VBox and sets its spacing.
		imageBox.getChildren().addAll(mutualFriendsVBox, numOfUsersDisplayed,
				allUsersDisplayedList, clearSN);
		imageBox.setSpacing(10);

		// Adds this VBox to the right pane of the border pane.
		root.setRight(imageBox);

		// Put a VBox in the center of the pane and create an HBox. Both boxes
		// will be updated when createUserDisplay is called. Once a file is
		// loaded, this VBox will hold the name of the current central user and
		// his or her list of friends. See: createUserDisplay for details about
		// how these two boxes are used.

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
		 * A FileChooser instance that allows the user of the GUI to upload a
		 * file from their computer. Clicking the button will open up File
		 * Explorer. When a file is selected, it will be loaded in and if the
		 * central user is specified in the file, the user's info will be
		 * displayed. Otherwise, current central user will stayed displayed.
		 * Adding multiple files will keep updating the Social Network with the
		 * contents of the file.
		 * 
		 * If the file is not in the proper format a pop up window will be
		 * displayed that says "Error: Invalid File".
		 * 
		 * The following webpage was consulted for information on the
		 * FileChooser class:
		 * 
		 * https://www.geeksforgeeks.org/javafx-filechooser-class/
		 * 
		 */

		// Creates a VBox and updates its spacing.
		VBox fileBox = new VBox();
		fileBox.setSpacing(10);

		// Creates a new HBox and updates it spacing.
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);

		// Creates new items to add to this VBox: FileChooser, button, and
		// label.
		FileChooser fileChooser = new FileChooser();
		Button fileButton = new Button("Upload File");
		Label fileAdd = new Label("Select file to update the Social Network.");
		fileAdd.setStyle(cssTextStyle);

		// Creates a new help button.
		Button helpBTN = new Button("Help!");

		// Adds button to this HBox.
		buttonBox.getChildren().addAll(fileButton, helpBTN);
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		// Adds items to this VBox.
		fileBox.getChildren().addAll(fileAdd, buttonBox);

		/**
		 * EventHandler for updating the social network.
		 */
		EventHandler<ActionEvent> updateSociallNetwork = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				// Get the file selected by the GUI user.
				File file = fileChooser.showOpenDialog(primaryStage);

				if (file != null) {
					try {// If an error occurs when trying to create
							// a new Social Network with the file the GUI
							// user chose, then do not update the social
							// network and have a pop up window display.
						SocialNetwork newSN = new SocialNetwork();
						newSN.createSocialNetWork(file);
						socialNetwork.createSocialNetWork(file);
						socialNetwork.updateLogFile(file, logFW);
						statsText.setText("Network consists of: "
								+ socialNetwork.getGraph().order() + " users, "
								+ socialNetwork.getGraph().size()
								+ " friendships, and "
								+ socialNetwork.getGraph().numberOfGroups()
								+ " connected groups.\n");
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
					// If the central user is changed by the file, then
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
						return;
					}

					allUserNamesDisplayed
							.addAll(socialNetwork.getGraph().getAllUsers());
					allUsersDisplayedList.setItems(allUserNamesDisplayed);
					// numOfUsersDisplayed.setText("Users In Network");
					root.setRight(imageBox);
				}
			}
		};

		/**
		 * EventHandler instance which implements functionality for help alert.
		 */
		EventHandler<ActionEvent> helpEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Alert helpMe = new Alert(AlertType.NONE,
						"Please contact the system administrator for help.",
						ButtonType.OK);
				helpMe.setTitle("Help!");
				helpMe.showAndWait();
			}
		};

		// When the button is pressed the method above will execute.
		fileButton.setOnAction(updateSociallNetwork);
		helpBTN.setOnAction(helpEvent);
		fileBox.setAlignment(Pos.BOTTOM_CENTER);
		root.setBottom(fileBox);

////////////////////////////////////////////////////////////////////////////////

		/*
		 * vbox2 holds the TextFields, Buttons, and Labels that allow the Social
		 * Network to be updated via the GUI. vbox2 is displayed on the left
		 * side of the GUI. A user can be added and removed and a new central
		 * user can be chosen as well.
		 */

		// Creates a new VBox and updates its styling.
		VBox vbox2 = new VBox();
		vbox2.setPadding(new Insets(10));
		vbox2.setSpacing(10);
		vbox2.setStyle(cssLeftPanelLayout);

		// Creates button, label, and text field to handle adding a user for the
		// user in the GUI.
		Button addUserBTN = new Button("Add User");
		Label addUserLabel = new Label("Add User:");
		addUserLabel.setStyle(cssTextStyle);
		TextField addUser = new TextField();
		addUser.setMaxWidth(100);

		// Creates button, label, and text field to handle removing a user for
		// the user in the GUI.
		Button removeUserBTN = new Button("Remove User");
		Label removeUserLabel = new Label("Remove User:");
		removeUserLabel.setStyle(cssTextStyle);
		TextField removeUser = new TextField();
		removeUser.setMaxWidth(100);

		// Creates button, label, and text field to handle updating central
		// user for the user in the GUI.
		Button setCentralUser = new Button("Set Central User");
		Label setCentralLabel = new Label("View User:");
		setCentralLabel.setStyle(cssTextStyle);
		TextField setUser = new TextField();
		setUser.setMaxWidth(100);

		// Creates button, labels, and text fields to handle adding a
		// friendship for the user in the GUI.
		Button addFriendship = new Button("Add Friendship");
		Label addFriendLabel = new Label("Add Friendship:");
		addFriendLabel.setStyle(cssTextStyle);
		TextField addFriend1 = new TextField();
		TextField addFriend2 = new TextField();
		addFriend1.setMaxWidth(100);
		addFriend2.setMaxWidth(100);

		// Creates a new HBox and adds TextFields to it.
		HBox friendBox = new HBox();
		friendBox.setSpacing(5);
		friendBox.getChildren().addAll(addFriend1, addFriend2);

		// Creates a button, label, and text fields to handle removing a
		// friendship for the user in the GUI.
		Button removeFriend = new Button("Remove Friendship");
		Label removeLabel = new Label("Remove Friendship:");
		removeLabel.setStyle(cssTextStyle);
		TextField removeFriend1 = new TextField();
		TextField removeFriend2 = new TextField();
		removeFriend1.setMaxWidth(100);
		removeFriend2.setMaxWidth(100);

		// Creates a new HBox and adds TextFields to it.
		HBox removeBox = new HBox();
		removeBox.setSpacing(5);
		removeBox.getChildren().addAll(removeFriend1, removeFriend2);

		// Creates a new VBox and adds items for adding user GUI components.
		VBox addUserVBox = new VBox();
		addUserVBox.setSpacing(10);
		addUserVBox.getChildren().addAll(addUserLabel, addUser, addUserBTN);

		// Creates a new VBox and adds items for removing user GUI components.
		VBox removeUserVBox = new VBox();
		removeUserVBox.setSpacing(10);
		removeUserVBox.getChildren().addAll(removeUserLabel, removeUser,
				removeUserBTN);

		// Creates a new HBox and adds boxes with add/remove user GUI
		// components.
		HBox addRemoveBox = new HBox();
		addRemoveBox.setSpacing(10);
		addRemoveBox.getChildren().addAll(addUserVBox, removeUserVBox);

		// Creates a button, label, and text fields to handle listing mutual
		// friends.
		Button mutualFriend = new Button("List Mutual Friends");
		Label mutualLabel = new Label("List Mutual Friends:");
		mutualLabel.setStyle(cssTextStyle);
		TextField mutualFriend1 = new TextField();
		TextField mutualFriend2 = new TextField();
		mutualFriend1.setMaxWidth(100);
		mutualFriend2.setMaxWidth(100);

		// Creates a new HBox and adds TextFields to it.
		HBox mutualFriendBox = new HBox();
		mutualFriendBox.setSpacing(5);
		mutualFriendBox.getChildren().addAll(mutualFriend1, mutualFriend2);

		// Adds all user action GUI components to the VBox.
		vbox2.getChildren().addAll(addRemoveBox, setCentralLabel, setUser,
				setCentralUser, addFriendLabel, friendBox, addFriendship,
				removeLabel, removeBox, removeFriend, mutualLabel,
				mutualFriendBox, mutualFriend);
		vbox2.setStyle(cssLeftPanelLayout);
		// Sets this VBox in border pane's left pane.
		root.setLeft(vbox2);

		// A pop up window to let the user of the GUI know if their attempt
		// to update the Social Network worked.
		Alert update = new Alert(AlertType.NONE);
		// Creates a button type OK to allow user to exit alert dialogue.
		update.getDialogPane().getButtonTypes().add(ButtonType.OK);

///////////////////////////////////////////////////////////////////////////////

		/**
		 * Class which implements the functionality to register a new user.
		 */
		class RegisterAction implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				if (!addUser.getText().isBlank()) {
					// Creates an array of commands and text associated with
					// those commands.
					String[] words = { "a",
							addUser.getText().trim().toLowerCase() };
					try {
						int numberOfUsers = socialNetwork.getGraph().order();
						// If user is already in the social network, do not add
						// user; alert GUI user and exit method.
						if (socialNetwork.getGraph().search(addUser.getText()
								.trim().toLowerCase()) != null) {
							update.setTitle("Duplicate User");
							update.setContentText(addUser.getText().trim()
									.toLowerCase()
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
							update.setContentText(
									addUser.getText().trim().toLowerCase()
											+ " added to Social Network.");
							statsText.setText("Network consists of: "
									+ socialNetwork.getGraph().order()
									+ " users, "
									+ socialNetwork.getGraph().size()
									+ " friendships, and "
									+ socialNetwork.getGraph().numberOfGroups()
									+ " connected groups.\n");
							update.showAndWait();
							// Add command to the log file.
							socialNetwork.updateLogFile(words, logFW);

						}

						// Updates the ObservableList containing all users.
						if (!allUserNamesDisplayed.contains(
								addUser.getText().trim().toLowerCase())) {
							allUserNamesDisplayed.add(
									addUser.getText().trim().toLowerCase());
						}

					} catch (Exception e) {
						Alert addUserError = new Alert(AlertType.ERROR,
								"Add user error!", ButtonType.OK);
						addUserError.show();
					}
				}
				addUser.clear();
			}
		}

		// When the addUserBTN is pressed or enter is pressed in the addUser
		// TextField, calls an event handler.
		addUserBTN.setOnAction(new RegisterAction());
		addUser.setOnAction(new RegisterAction());

///////////////////////////////////////////////////////////////////////////////

		/**
		 * Class which implements the functionality to remove a user.
		 */
		class RemoveAction implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				if (!removeUser.getText().isBlank()) {
					String[] words = { "r",
							removeUser.getText().trim().toLowerCase() };
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
							update.setContentText(removeUser.getText().trim()
									.toLowerCase()
									+ " removed from the Social Network.");
							statsText.setText("Network consists of: "
									+ socialNetwork.getGraph().order()
									+ " Users, "
									+ socialNetwork.getGraph().size()
									+ " friendships, and "
									+ socialNetwork.getGraph().numberOfGroups()
									+ " connected groups.\n");
							update.showAndWait();
							// Add command to the log file.
							socialNetwork.updateLogFile(words, logFW);
						} else {
							// User not found, no one was removed from the
							// social network.
							update.setTitle("User Not Removed");
							update.setContentText(removeUser.getText().trim()
									.toLowerCase()
									+ " was not found in the Social Network.");
							update.showAndWait();
						}
						// Updating the ObservableList allUserNames
						// in the GUI if necessary.
						if (allUserNamesDisplayed.contains(
								removeUser.getText().trim().toLowerCase())) {
							allUserNamesDisplayed.remove(
									removeUser.getText().trim().toLowerCase());
						}
						// If the current list of friends being displayed
						// contains the user that was just removed, update
						// the list.
						if (namesOfFriends.contains(
								removeUser.getText().trim().toLowerCase())) {
							namesOfFriends.remove(
									removeUser.getText().trim().toLowerCase());
						}
						// If current central user is deleted, replace the user
						// info being displayed with "Removed Central User".
						if (socialNetwork.getCentralUser().getUserName().equals(
								removeUser.getText().trim().toLowerCase())) {
							root.setCenter(new Label("Removed central user."));
						}
					} catch (Exception e) {
						Alert failRemove = new Alert(AlertType.ERROR,
								"Remove user error!", ButtonType.OK);
						failRemove.show();
					}
				}
				removeUser.clear();
			}

		}

		// Removes a user when GUI user clicks button or presses enter in text
		// field.
		removeUserBTN.setOnAction(new RemoveAction());
		removeUser.setOnAction(new RemoveAction());

///////////////////////////////////////////////////////////////////////////////

		/**
		 * Class which implements the functionality to add friendships.
		 */
		class AddFriendAction implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				if (!addFriend1.getText().isBlank()
						&& !addFriend2.getText().isBlank()) {
					String[] words = { "a",
							addFriend1.getText().trim().toLowerCase(),
							addFriend2.getText().trim().toLowerCase() };
					// If same user is entered twice, no friendship is added
					// and an alert shows up.
					if (addFriend1.getText().trim().toLowerCase().equals(
							addFriend2.getText().trim().toLowerCase())) {
						update.setTitle("Error!");
						update.setContentText(
								"You entered the same user twice.");
						update.showAndWait();
						addFriend1.clear();
						addFriend2.clear();
						return;
					}

					try {
						GraphNode friend1 = socialNetwork.getGraph().search(
								addFriend1.getText().trim().toLowerCase());
						if (friend1 != null) {
							// If friendship already exists between the two
							// users do not create a new friendship; GUI user is
							// alerted.
							if (friend1.getFriends().contains(addFriend2
									.getText().trim().toLowerCase())) {
								update.setTitle("Friendship not added: ");
								update.setContentText(addFriend1.getText()
										.trim().toLowerCase()
										+ " and "
										+ addFriend2.getText().trim()
												.toLowerCase()
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
							update.setContentText(
									addFriend1.getText().trim().toLowerCase()
											+ " and "
											+ addFriend2.getText().trim()
													.toLowerCase()
											+ " are now friends.");
							statsText.setText("Network consists of: "
									+ socialNetwork.getGraph().order()
									+ " Users, "
									+ socialNetwork.getGraph().size()
									+ " friendships, and "
									+ socialNetwork.getGraph().numberOfGroups()
									+ " connected groups.\n");
							update.showAndWait();
							// Add command to the log file.
							socialNetwork.updateLogFile(words, logFW);
						}

						// Updating the ObservableList allUserNames
						// in the GUI if necessary.
						if (!allUserNamesDisplayed.contains(
								addFriend1.getText().trim().toLowerCase())) {

							allUserNamesDisplayed.add(
									addFriend1.getText().trim().toLowerCase());
						}
						if (!allUserNamesDisplayed.contains(
								addFriend2.getText().trim().toLowerCase())) {
							allUserNamesDisplayed.add(
									addFriend2.getText().trim().toLowerCase());
						}
						// If either user in the new friendship is currently
						// the central user (user being displayed)
						// update the list of friends being displayed.
						if (socialNetwork.getCentralUser() != null) {
							if (socialNetwork.getCentralUser().getUserName()
									.equals(addFriend2.getText().trim()
											.toLowerCase())) {
								if (!namesOfFriends.contains(addFriend1
										.getText().trim().toLowerCase())) {
									namesOfFriends.add(addFriend1.getText()
											.trim().toLowerCase());
								}
							}
							if (socialNetwork.getCentralUser().getUserName()
									.equals(addFriend1.getText().trim()
											.toLowerCase())) {
								if (!namesOfFriends.contains(addFriend2
										.getText().trim().toLowerCase())) {
									namesOfFriends.add(addFriend2.getText()
											.trim().toLowerCase());
								}
							}
						}
					} catch (Exception e) {
						Alert addFriendError = new Alert(AlertType.ERROR,
								"Add friendship error!", ButtonType.OK);
						addFriendError.show();
					}
				}
				addFriend1.clear();
				addFriend2.clear();
			}
		}

		// Adds a friendship between two users if the button is pressed or enter
		// is pressed in the text field.
		addFriendship.setOnAction(new AddFriendAction());
		addFriend2.setOnAction(new AddFriendAction());

///////////////////////////////////////////////////////////////////////////////

		/**
		 * Class which implements the functionality to set the central user.
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
					String[] words = { "s",
							setUser.getText().trim().toLowerCase() };
					try {
						// Add command to the log file.
						socialNetwork.updateLogFile(words, logFW);
					} catch (Exception e) {
						Alert centralUserError = new Alert(AlertType.ERROR,
								"Set central user error!", ButtonType.OK);
						centralUserError.show();
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

///////////////////////////////////////////////////////////////////////////////

		/**
		 * Class which implements the functionality to remove friendships.
		 */
		class RemoveFriendship implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				if (!removeFriend1.getText().isBlank()
						&& !removeFriend2.getText().isBlank()) {
					String[] words = { "r",
							removeFriend1.getText().trim().toLowerCase(),
							removeFriend2.getText().trim().toLowerCase() };
					// If same user is entered twice, no friendship is removed
					// and an alert shows up.
					if (removeFriend1.getText().trim().toLowerCase().equals(
							removeFriend2.getText().trim().toLowerCase())) {
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
						GraphNode friendA = socialNetwork.getGraph().search(
								removeFriend1.getText().trim().toLowerCase());
						if (friendA != null) {
							// If friendship exists between the two entered
							// names, need to remove the friendship.
							if (friendA.getFriends().contains(removeFriend2
									.getText().trim().toLowerCase())) {
								socialNetwork.updateSocialNetwork("r", words);
								update.setTitle("Friendship removed: ");
								update.setContentText(removeFriend1.getText()
										.trim().toLowerCase()
										+ " and "
										+ removeFriend2.getText().trim()
												.toLowerCase()
										+ " are no longer friends.");
								statsText.setText("Network consists of: "
										+ socialNetwork.getGraph().order()
										+ " Users, "
										+ socialNetwork.getGraph().size()
										+ " friendships, and "
										+ socialNetwork.getGraph()
												.numberOfGroups()
										+ " connected groups.\n");
								update.showAndWait();
								// Add command to the log file.
								socialNetwork.updateLogFile(words, logFW);
							}
						}
						// If the number of edges is the same, a friendship
						// was not removed, GUI user is alerted.
						if (numberOfFriendships == socialNetwork.getGraph()
								.size()) {
							update.setTitle("Friendship not removed: ");
							update.setContentText("Friendship between "
									+ removeFriend1
											.getText().trim().toLowerCase()
									+ " and "
									+ removeFriend2.getText().trim()
											.toLowerCase()
									+ "  does not exist.");
							update.showAndWait();
							removeFriend1.clear();
							removeFriend2.clear();
							return;
						}

						// If either user in the friendship removed is currently
						// the central user (user being displayed),
						// updates the list of friends being displayed.
						if (socialNetwork.getCentralUser() != null) {
							if (socialNetwork.getCentralUser().getUserName()
									.equals(removeFriend2.getText().trim()
											.toLowerCase())) {
								if (namesOfFriends.contains(removeFriend1
										.getText().trim().toLowerCase())) {
									namesOfFriends.remove(removeFriend1
											.getText().trim().toLowerCase());
								}
							}

							if (socialNetwork.getCentralUser().getUserName()
									.equals(removeFriend1.getText().trim()
											.toLowerCase())) {
								if (namesOfFriends.contains(removeFriend2
										.getText().trim().toLowerCase())) {
									namesOfFriends.remove(removeFriend2
											.getText().trim().toLowerCase());
								}
							}
						}
					} catch (Exception e) {
						Alert removeFriendError = new Alert(AlertType.ERROR,
								"Remove friendship error!", ButtonType.OK);
						removeFriendError.show();
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
		 * Class which implements the functionality to display mutual
		 * friendships.
		 */
		class MutualFriendship implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) {
				if (!mutualFriend1.getText().isBlank()
						&& !mutualFriend2.getText().isBlank()) {
					// If same user is entered twice, no mutual friends are
					// displayed
					// and an alert shows up.
					if (mutualFriend1.getText().trim().toLowerCase().equals(
							mutualFriend2.getText().trim().toLowerCase())) {
						update.setTitle("Error!");
						update.setContentText(
								"You entered the same user twice.");
						update.showAndWait();
						mutualFriend1.clear();
						mutualFriend2.clear();
						return;
					}

					try {
						GraphNode friendA = socialNetwork.getGraph().search(
								mutualFriend1.getText().trim().toLowerCase());
						GraphNode friendB = socialNetwork.getGraph().search(
								mutualFriend2.getText().trim().toLowerCase());
						if (friendA != null & friendB != null) {
							if (friendA.getFriends().contains(mutualFriend2
									.getText().trim().toLowerCase())) {

							}
							// Clear list of mutual friends currently displayed.
							mutualUsersList.clear();
							ArrayList<String> mutualUsers = new ArrayList<String>(
									friendA.getFriends());
							mutualUsers.retainAll(friendB.getFriends());
							mutualUsersList.addAll(mutualUsers);
							mutualFriendsText.setText("Mutual Friends between "
									+ mutualFriend1
											.getText().trim().toLowerCase()
									+ " and "
									+ mutualFriend2.getText().trim()
											.toLowerCase()
									+ "\nNumber of Mutual Friends: "
									+ mutualUsers.size());
						}

					} catch (Exception e) {
						Alert mutualFriendError = new Alert(AlertType.ERROR);
						mutualFriendError.show();
					}
				}
				// Removes text in text fields.
				mutualFriend1.clear();
				mutualFriend2.clear();
			}
		}

		// Displays Mutual friendships if button is pressed or enter is pressed
		// in second text field.
		mutualFriend.setOnAction(new MutualFriendship());
		mutualFriend2.setOnAction(new MutualFriendship());

///////////////////////////////////////////////////////////////////////////////

		// Creates new buttons.
		ButtonType clear = new ButtonType("I'm Sure");
		ButtonType noClear = new ButtonType("I Changed My Mind");

		// Alert confirmation a user wishes to clear the social network.
		Alert makeSure = new Alert(AlertType.NONE,
				"Are you sure you want to clear the Social Network?", clear,
				noClear);

		// Alert letting user know social network was successfully cleared.
		Alert isCleared = new Alert(AlertType.NONE, "Social Network cleared!",
				ButtonType.OK);

		/**
		 * EventHandler instance which implements functionality to clear the
		 * social network.
		 */
		EventHandler<ActionEvent> clearSocialNetwork = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Alert GUI user to make sure they want to clear the Social
				// Network.
				Optional<ButtonType> result = makeSure.showAndWait();
				if (result.orElse(clear) == clear) {
					Set<String> allUsers = socialNetwork.getGraph()
							.getAllUsers();
					// Updating the log file since every user is being removed.
					for (String user : allUsers) {
						String[] cmd = { "r", user };
						try {
							socialNetwork.updateLogFile(cmd, logFW);
						} catch (Exception e) {
							Alert logUpdateError = new Alert(AlertType.ERROR,
									"Log file update error!");
							logUpdateError.show();
						}
					}

					// Setting the Social Network's graph to new empty graph.
					SocialGraph newGraph = new SocialGraph();
					socialNetwork.setGraph(newGraph);

					// Delete all users currently stored in the ObservableLists.
					allUserNamesDisplayed.clear();
					mutualUsersList.clear();

					// Resets the mutual friend text.
					mutualFriendsText.setText("Mutual Friends: ");

					// Clears the current central user.
					socialNetwork.setCentralUser(null);

					// Sets the center to a new label.
					statsText.setText("Network consists of: "
							+ socialNetwork.getGraph().order() + " Users, "
							+ socialNetwork.getGraph().size()
							+ " friendships, and "
							+ socialNetwork.getGraph().numberOfGroups()
							+ " connected groups.\n");

					// Sets center pane of border pane to new label.
					root.setCenter(new Label(""));

					isCleared.showAndWait();
				}
			}
		};

		// Calls the event handler when the clear social network button is
		// pressed.
		clearSN.setOnAction(clearSocialNetwork);

///////////////////////////////////////////////////////////////////////////////

		// Alert which shows the shortest path between two users.
		Alert graphAlgorithms = new Alert(AlertType.NONE, "", ButtonType.OK);
		graphAlgorithms.setTitle("Show Shortest Path Between These Users");

		// Creates new UI components for user to find shortest path between two
		// users.
		Button shortestPathBTN = new Button("View");
		Label shortestPathLabel = new Label("Get Shortest Path");
		shortestPathLabel.setStyle(cssTextStyle);
		TextField shortestPathFriend1 = new TextField();
		TextField shortestPathFriend2 = new TextField();

		shortestPathFriend1.setMaxWidth(100);
		shortestPathFriend2.setMaxWidth(100);

		// Creates new HBox and adds the new UI components to it, as well as
		// sets the box parameters.
		HBox shortestPathFriendBox = new HBox();
		shortestPathFriendBox.setSpacing(5);
		shortestPathFriendBox.getChildren().addAll(shortestPathFriend1,
				shortestPathFriend2);
		imageBox.getChildren().addAll(shortestPathLabel, shortestPathFriendBox,
				shortestPathBTN);

		/**
		 * EventHandler instance which implements functionality to show shortest
		 * path between two users in the social network.
		 */
		EventHandler<ActionEvent> shortestPath = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				ArrayList<String> shortestPath = socialNetwork.getGraph()
						.getShortestPath(shortestPathFriend1.getText(),
								shortestPathFriend2.getText());
				if (shortestPath != null) {
					graphAlgorithms.setContentText("" + shortestPath);
					graphAlgorithms.showAndWait();
				} else {
					graphAlgorithms.setContentText("NO PATH!");
					graphAlgorithms.showAndWait();
				}
			}
		};

		// Calls methods when button and text fields are interacted with by
		// user.
		shortestPathBTN.setOnAction(shortestPath);
		shortestPathFriend1.setOnAction(shortestPath);
		shortestPathFriend2.setOnAction(shortestPath);

///////////////////////////////////////////////////////////////////////////////

		/**
		 * EventHandler instance that updates what user is displayed when a
		 * friend is clicked in the current user's list of friends.
		 */
		friendList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// This gets the index where the clicked name is stored
				// in the list. Clicking on the list where no name is stored
				// results in a negative number being returned.
				int indexOfFriend = friendList.getSelectionModel()
						.getSelectedIndex();
				if (indexOfFriend < 0) {
					return;
				} else {
					hbox.getChildren().clear();
					vbox1.getChildren().clear();
					String[] words = { "s", namesOfFriends.get(indexOfFriend) };
					try {
						// Add command to the log file.
						socialNetwork.updateLogFile(words, logFW);
					} catch (Exception e) {

					}

					socialNetwork.setCentralUser(socialNetwork.getGraph()
							.search(namesOfFriends.get(indexOfFriend)));
					createUserDisplay(socialNetwork.getCentralUser(), root,
							vbox1, hbox);
				}
			}
		});

		/*
		 * EventHandler instance that updates what user is displayed when a name
		 * is clicked in the list that contains all users.
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

		// Buttons to create save UI components.
		ButtonType save = new ButtonType("Save File and Exit");
		ButtonType noSave = new ButtonType("Exit Without Save");

		Alert alertOnExit = new Alert(AlertType.NONE,
				"Do you want to save the log file to your computer?", save,
				noSave);
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

				// If the save button was clicked.
				if (result.orElse(save) == save) {
					// FileChooser to prompt user to save a file on their
					// computer.
					File fileToSave = fileChooser.showSaveDialog(primaryStage);
					// If the GUI user chooses a file.
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

		// Creates a Scene and opens it upon the primary stage.
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
}