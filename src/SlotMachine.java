/** Author: Bryon Taylor
 *
 *  
 *  This application simulates a slot machine. Good luck!
 */

import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SlotMachine extends Application {

	// scales the interface for different size screens
	private static final double SCALAR = 0.9;

	// default width and height for game window
	private static final int WINDOW_WIDTH = 1100;
	private static final int WINDOW_HEIGHT = 1025;

	// default sizes for fields and their fonts // todo local?
	private static final int FONT_SIZE_FIELDS = 30;
	private static final int FONT_SIZE_FIELD_DESCRIPTION = 35;
	private static final int FIELD_WIDTH = 300;
	private static final int FIELD_HEIGHT = 50;

	// multipliers for winnings
	private static final int
			MULTIPLIER1 = 1,
			MULTIPLIER2 = 3,
			MULTIPLIER3 = 5,
			MULTIPLIER4 = 10,
			MULTIPLIER5 = 15,
			MULTIPLIER6 = 25,
			MULTIPLIER7 = 50,
			MULTIPLIER8 = 75,
			JACKPOT_MULTIPLIER = 1000;

	// columns for GridPanes
	private static final int
			COL0 = 0,
			COL1 = 1,
			COL2 = 2;

	// rows for GridPanes
	private static final int
			ROW0 = 0,
			ROW1 = 1,
			ROW2 = 2,
			ROW3 = 3,
			ROW4 = 4;
	
	// Array to hold dollar amounts. Amounts are displayed in the "add cash" dialog
	private static final Integer[] DOLLAR_AMOUNTS = {1, 5, 10, 20, 50, 100};

	// variables to manage bets and credits
	private int bet = 1; // default bet amount todo
	private int
			creditsPaid,
			creditsWon,
			credits;

	// Reel objects that display static images when the Reel is stopped or animated images otherwise
	private Reel
			reel1,
			reel2,
			reel3;

	// String representation of a reel's image, e.g. "cherries" or "triple7s" used in handleWinningCombos() method
	private String
			reel1Value,
			reel2Value,
			reel3Value;

	// UI components
	private TextField
			creditsField,
			betField,
			paidField;

	private Text
			creditsText,
			betText,
			paidText;

	private Button
			addBetBtn,
			minusBetBtn,
			addCashBtn,
			cashOutBtn,
			spinBtn;

	// Reels, e.g. "firstReel", display static image
	// Animations, e.g. "firstAnimation", display motion
	private Pane
			firstReel,
			secondReel,
			thirdReel,
			firstAnimation,
			secondAnimation,
			thirdAnimation;

	// containers for game UI elements
	private GridPane topImgPane;
	private GridPane gridPane;

	// lights that activate when there is a winning combination
	private ImageView winningLightsLeft;
	private ImageView winningLightsRight;

	// find user directory to create path for files
	private static final String ABSOLUTE_PATH = System.getProperty("user.dir");

	public void start(Stage mainStage) {

		initTextFields();
		initFieldDescriptions();
		initButtons();
		initButtonListeners();
		initGameLayout();

		// add components to stage
		GridPane root = new GridPane();
		root.setMaxWidth(WINDOW_WIDTH);
		root.setGridLinesVisible(false); // todo
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		root.setAlignment(Pos.TOP_CENTER);
		root.add(topImgPane, COL0, ROW0);
		root.add(gridPane, COL0, ROW1);

		// game window dimensions
		int width = (int) Math.round(WINDOW_WIDTH * SCALAR);
		int height = (int) Math.round(WINDOW_HEIGHT * SCALAR);

		Scene scene = new Scene(root, width, height, null);
		mainStage.setScene(scene);
		mainStage.setTitle("Stardust - Lucky Sevens");
		mainStage.initStyle(StageStyle.UTILITY);
		mainStage.show();
	}

	// initialize the "credits", "bet", and "winner paid" fields
	private void initTextFields() {
		creditsField = getStyledTextField(credits); // credits =  0
		betField = getStyledTextField(bet); // bet = 1
		paidField = getStyledTextField(creditsPaid); // creditsPaid = 0
	}

	private TextField getStyledTextField(int fieldValue) {
		TextField textField = new TextField(String.valueOf(fieldValue));
		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.setFont(new Font("SansSerif", (int) Math.round(FONT_SIZE_FIELDS * SCALAR)));
		textField.setEditable(false);
		textField.setPrefSize((int) Math.round(FIELD_WIDTH * SCALAR), (int) Math.round(FIELD_HEIGHT * SCALAR));
		return textField;
	}

	// initialize text descriptions for fields
	private void initFieldDescriptions() {
		creditsText = getFormattedText("CREDITS");
		betText = getFormattedText("BET");
		paidText = getFormattedText("WINNER PAID");
	}

	private Text getFormattedText(String label) {
		int fontSize = (int) Math.round(FONT_SIZE_FIELD_DESCRIPTION * SCALAR);
		Text text = new Text(label);
		text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
		text.setFill(Color.FIREBRICK);
		return text;
	}

	// initialize game buttons
	private void initButtons() {

		// default button font size
		final int smallBtnFontSize = 18;
		final int spinBtnFontSize = 45;

		int intFontSize = (int) Math.round(smallBtnFontSize * SCALAR);
		String strFontSize = String.valueOf(intFontSize);

		// create buttons (font size, label)
		addBetBtn = createButton(strFontSize, "ADD BET");
		minusBetBtn = createButton(strFontSize, "MINUS BET");
		addCashBtn = createButton(strFontSize,"ADD CASH");
		cashOutBtn = createButton(strFontSize,"CASHOUT");

		// spin button has larger font size which makes button larger than the rest
		intFontSize = (int) Math.round(spinBtnFontSize * SCALAR);
		strFontSize = String.valueOf(intFontSize);
		spinBtn = createButton((strFontSize),"SPIN");
	}

	private Button createButton(String fontSize, String label) {
		Button button = new Button(label);
		String fontStyleSize = "-fx-font-size:" + fontSize + ";";
		button.setStyle("-fx-background-color: #000000, linear-gradient(#56a8e3, #2f73a3)," +
				"linear-gradient(#14425a, #14425a);" +
				"-fx-background-radius: 3;" +
				"-fx-text-fill: white;" +
				fontStyleSize);
		return button;
	}

	// initialize button listeners for all game buttons and define behaviors
	private void initButtonListeners() {
		final int maxBet = 5;

		// "add bet" button listener adds to the bet amount up to maxBet (5)
		addBetBtn.setOnAction(e -> { // pass Event e to the EventHandler
			if(bet < maxBet) {
				bet++;
				betField.setText(Integer.toString(bet));
				playAudioClip("audio/SMB_Coin.wav"); // play audio to indicate bet increased
			}
		});

		// "minus bet" button listener subtracts from the bet amount
		minusBetBtn.setOnAction(e -> { // pass Event e to the EventHandler
			if(bet > 1){
				bet--;
				betField.setText(Integer.toString(bet));
				playAudioClip("audio/SMB_Coin.wav"); // play audio to indicate bet decreased
			}
		});

		// "add cash" button listener adds cash amount to existing credits
		addCashBtn.setOnAction(e -> { // pass Event e to the EventHandler
			int addedAmt = addCash();
			credits = Integer.parseInt(creditsField.getText());
			creditsField.setText(String.valueOf(credits + addedAmt));
		});

		// "cash out" button listener will end the game with an Alert dialog showing player winnings
		cashOutBtn.setOnAction(e -> { // pass Event e to the EventHandler

			// get current credits to display in alert then reset
			int currentCredits = Integer.parseInt(creditsField.getText());
			creditsField.setText("0");

			// display alert
			Alert cashOutAlert = new Alert(AlertType.INFORMATION);
			cashOutAlert.setTitle("Winner!");
			cashOutAlert.setHeaderText("Thanks for playing!");
			cashOutAlert.setHeaderText("Feel free to donate your winnings to the author.");
			cashOutAlert.setContentText("Your winnings: $" + currentCredits);
			cashOutAlert.showAndWait();

			playAudioClip("audio/coinsDropping.wav"); // play sound file to indicate user cashed out
		});

		// separate inner class to handle "spin" button click events
		spinBtn.setOnAction(new SpinButtonHandler());
	}

	private void initGameLayout() {

		// top Pane parameters
		final int topPaneHGap = 15;
		final int topPaneTopPadding = 20;
		final int topImageWidth = 750;
		final int topImageHeight = 357;
		final int winningLightsWidth = 101;
		final int winningLightsHeight = 350;

		// bottom Pane parameters
		final int buttonBoxSpacing = 10;
		final int bottomPaneHGap = 25;
		final int bottomPaneVGap = 15;
		final int spinBtnBoxTopPadding = 30;

		// Reel objects show static images and spinning animations
		reel1 = new Reel();
		reel2 = new Reel();
		reel3 = new Reel();

		// initialize top image and style
		Image imgStardust = new Image("file:" + ABSOLUTE_PATH + "/src/images/stardustLarger.gif");
		ImageView stardustImgView = new ImageView(imgStardust);
		stardustImgView.setFitWidth(topImageWidth * SCALAR);
		stardustImgView.setFitHeight(topImageHeight * SCALAR);

		winningLightsLeft = new ImageView(new Image(
				"file:" + ABSOLUTE_PATH + "/src/images/chasingLightsBlue.gif"));
		winningLightsRight = new ImageView(new Image(
				"file:" + ABSOLUTE_PATH + "/src/images/chasingLightsBlue2.gif"));

		// initialize gridPane container for top elements
		topImgPane = new GridPane();
		topImgPane.setGridLinesVisible(false); // todo
		topImgPane.setHgap(topPaneHGap);
		topImgPane.setPadding(new Insets(topPaneTopPadding, 0, 0, 0));
		topImgPane.setAlignment(Pos.TOP_CENTER);

		// initialize animated lights but don't set visible until there is a win
		winningLightsLeft.setFitWidth(winningLightsWidth * SCALAR);
		winningLightsLeft.setFitHeight(winningLightsHeight * SCALAR);
		winningLightsLeft.setVisible(false);

		winningLightsRight.setFitWidth(winningLightsWidth * SCALAR);
		winningLightsRight.setFitHeight(winningLightsHeight * SCALAR);
		winningLightsRight.setVisible(false);

		// add elements to top gridPane
		topImgPane.add(stardustImgView, COL1, ROW0);
		topImgPane.add(winningLightsLeft, COL0 , ROW0);
		topImgPane.add(winningLightsRight, COL2, ROW0 );

		// initializes reels to all triple 7's image
		firstReel = reel1.getDisplayReel();
		secondReel = reel2.getDisplayReel();
		thirdReel = reel3.getDisplayReel();

		// get animations of spinning reels
		firstAnimation = reel1.createAnimatedPane();
		secondAnimation = reel2.createAnimatedPane();
		thirdAnimation = reel3.createAnimatedPane();

		// create a new horizontal box for the "spin" button
		HBox spinBtnBox = new HBox();
		spinBtnBox.setAlignment(Pos.BOTTOM_CENTER);
		spinBtnBox.setPadding((new Insets((int) Math.round(spinBtnBoxTopPadding * SCALAR), 0, 0, 0)));
		spinBtnBox.getChildren().add(spinBtn);

		// create new horizontal box to contain "add bet" and "minus bet" buttons
		HBox adjustBetBox = new HBox(buttonBoxSpacing);
		adjustBetBox.setAlignment(Pos.CENTER);
		adjustBetBox.getChildren().add(addBetBtn);
		adjustBetBox.getChildren().add(minusBetBtn);



		// create new horizontal box to contain "add cash" and "cash out" buttons
		HBox playOrQuitBox = new HBox(buttonBoxSpacing);
		playOrQuitBox.setAlignment(Pos.CENTER);
		playOrQuitBox.getChildren().add(addCashBtn);
		playOrQuitBox.getChildren().add(cashOutBtn);

		// add arranges all UI components in a grid
		gridPane = new GridPane();
		gridPane.setGridLinesVisible(false); // todo
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.setMaxWidth(WINDOW_WIDTH); // todo

		// set spacing gaps between bottom pane elements
		gridPane.setHgap((int) (bottomPaneHGap * SCALAR));
		gridPane.setVgap((int) (bottomPaneVGap * SCALAR));

		// add all components to bottom gridPane layout
		gridPane.add(adjustBetBox, COL0, ROW4); // "add bet" & "minus bet" buttons
		gridPane.add(spinBtnBox, COL1, ROW4); // "spin" button
		gridPane.add(playOrQuitBox, COL2, ROW4); // "add cash" & "cash out" buttons

		// todo test
//		gridPane.add(addBetBtn, COL0, ROW4);
//		gridPane.add(minusBetBtn, COL0, ROW4);

		// add static images to the Panes that display reels
		gridPane.add(firstReel, COL0, ROW1);
		gridPane.add(secondReel, COL1, ROW1);
		gridPane.add(thirdReel, COL2, ROW1);

		// credits, bet, and paid fields
		gridPane.add(creditsField, COL0, ROW2);
		gridPane.add(betField, COL1, ROW2);
		gridPane.add(paidField, COL2, ROW2);

		// descriptions of fields above
		gridPane.add(creditsText, COL0, ROW3);
		gridPane.add(betText, COL1, ROW3);
		gridPane.add(paidText, COL2, ROW3);

		// center the text inside its cell
		GridPane.setHalignment(creditsText, HPos.CENTER);
		GridPane.setHalignment(betText, HPos.CENTER);
		GridPane.setHalignment(paidText, HPos.CENTER);
	}

	// creates a dialog box that warns the user that they are out of money and allows them to add cash
	// returns dollar amount chosen from drop down list
	public int addCash() {
		int dollarAmt = 0; // cash to be added

		// dialog displays a drop down list of dollar amounts, default is $1
		ChoiceDialog<Integer> addCashDialog = new ChoiceDialog<> (1, DOLLAR_AMOUNTS);
		addCashDialog.setTitle("You need more credits!");
		addCashDialog.setHeaderText("Add cash to play.");
		addCashDialog.setContentText("Select a dollar amount to add.    $");

		// Optional will only have the value "isPresent() if "ok" button clicked, else it will return isEmpty()
		Optional<Integer> btnClickResult = addCashDialog.showAndWait();

		// condition will only be true if user clicks "ok", false if "cancel" button clicked
		if(btnClickResult.isPresent()) {
			dollarAmt = addCashDialog.getSelectedItem();
		}
		return dollarAmt;
	}

	// determines proper pay out based on all three reels matching
	public int allReelsMatch(String reelValue) {
		switch(reelValue){
			case "cherries":
				creditsWon = MULTIPLIER2 * bet;
				break;
			case "singleBar":
				creditsWon = MULTIPLIER3 * bet;
				break;
			case "doubleBar":
				creditsWon = MULTIPLIER4 * bet;
				break;
			case "tripleBar":
				creditsWon = MULTIPLIER5 * bet;
				break;
			case "single7":
				creditsWon = MULTIPLIER6 * bet;
				break;
			case "double7s":
				creditsWon = MULTIPLIER7 * bet;
				break;
			case "triple7s":
				creditsWon = MULTIPLIER8 * bet;
				break;
			case "trip7sWinner":
				creditsWon = JACKPOT_MULTIPLIER * bet;
				break;
		}
		return creditsWon;
	}

	// pay out for all three reels that contain bars, but don't all match
	public int nonMatchedBars() {
		creditsWon = MULTIPLIER1 * bet;
		return creditsWon;
	}

	// pay out for all three reels that contain 7's, but don't all match
	public int nonMatchedSevens() {
		creditsWon = MULTIPLIER4 * bet;
		return creditsWon;
	}

	private void playAudioClip(String filePath) {
		AudioClip audioClip = new AudioClip(getClass().getResource(filePath).toString());
		audioClip.play();
	}

	// separate inner class to handle spin button clicks
	private class SpinButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent actionEvent) {

			// reels will animate during specified durations
			final int
					pauseTime1 = 1500, // 1.5 seconds
					pauseTime2 = 1900, // 1.9 seconds
					pauseTime3 = 2300; // 2.3 seconds

			paidField.setText("0");
			if(creditsPaid != 0) {
				//hBoxTop.getChildren().remove(viewCoinBurst);
				winningLightsLeft.setVisible(false);
				winningLightsRight.setVisible(false);
				//hBoxTop.getChildren().add(stardustImgView);
			}
			creditsPaid = 0;
			credits = Integer.parseInt(creditsField.getText()); // get current credits

			// if current credits > zero and credits minus bet is >= 0, update credits field minus bet
			if(credits > 0 && (credits - bet) >= 0) {
				credits -= bet;
				creditsField.setText(String.valueOf(credits));
			} else { // else prompt user to add cash

				// addCash() will return a dollar amount, add this amount to current credits
				int addedAmt = addCash();
				credits += addedAmt;

				// loop will continuously prompt user to add cash while the bet exceeds the current credits
				while((credits - bet) < 0) {
					creditsField.setText(String.valueOf(credits));
					addedAmt = addCash();
					credits += addedAmt;
				}
				creditsField.setText(String.valueOf(credits - bet));
			}

			animateReels(); // starts the reels spinning
			playAudioClip("audio/spinningReels.wav"); // plays audio while reels spin

			// to stop the reels from spinning after specified time
			PauseTransition pauseFirstReel = new PauseTransition(Duration.millis(pauseTime1));
			PauseTransition pauseSecondReel = new PauseTransition(Duration.millis(pauseTime2));
			PauseTransition pauseThirdReel = new PauseTransition(Duration.millis(pauseTime3));

			// after reel stops spinning replace the animation with a randomly generated static image
			pauseFirstReel.setOnFinished(event -> {
				gridPane.getChildren().remove(firstAnimation);
				gridPane.add(firstReel, COL0, ROW1);
				reel1Value = reel1.spinReel();
				playAudioClip("audio/reelStop.wav");
			});
			pauseFirstReel.play();

			// after reel stops spinning replace the animation with a randomly generated static image
			pauseSecondReel.setOnFinished(event -> {
				gridPane.getChildren().remove(secondAnimation);
				gridPane.add(secondReel, COL1, ROW1);
				reel2Value = reel2.spinReel();
				playAudioClip("audio/reelStop.wav");
			});
			pauseSecondReel.play();

			pauseThirdReel.setOnFinished(event -> {
				gridPane.getChildren().remove(thirdAnimation); // remove animation from Pane
				gridPane.add(thirdReel, COL2, ROW1); // replace animation with static image in Pane
				reel3Value = reel3.spinReel(); // String description of the image
				playAudioClip("audio/reelStop.wav"); // audio to indicate reel stopped spinning

				// after third reel stops, check all reels' values to determine if there is a winning combo
				handleWinningCombos();
			});
			pauseThirdReel.play();
		}

		// check if there are winning combos and award pay outs
		private void handleWinningCombos() {

			// if all three reels are equal
			if(reel1Value.equals(reel2Value) && reel2Value.equals(reel3Value)) {
				creditsPaid = allReelsMatch(reel1Value);

				// if all three reels contain bars but are not equal e.g. singleBar + doubleBar + doubleBar
			} else if(reel1Value.contains("Bar") && reel2Value.contains("Bar") && reel3Value.contains("Bar")) {
				creditsPaid = nonMatchedBars();

				// if all reels contain 7's but are not equal e.g. single7 + double7 + triple7
			} else if(reel1Value.contains("7") && reel2Value.contains("7") && reel3Value.contains("7")) {
				creditsPaid = nonMatchedSevens();
			}

			// if creditsPaid doesn't equal 0 then there was a winning combo
			if(creditsPaid != 0) {
				playAudioClip("audio/winner.wav");
				showWinningLights();
				updateCreditsAndPaidFields();
			}
		}

		private void showWinningLights() {
			winningLightsLeft.setVisible(true);
			winningLightsRight.setVisible(true);
		}

		// updates number of credits and winner paid fields after a win
		private void updateCreditsAndPaidFields() {
			paidField.setText(String.valueOf(creditsPaid));
			int updatedCredits = credits + creditsPaid;
			creditsField.setText(String.valueOf(updatedCredits));
		}

		// replaces static images in Panes with animations
		private void animateReels() {
			gridPane.getChildren().remove(firstReel); // remove static image
			gridPane.add(firstAnimation, 0 , 1); // replace with animation
			gridPane.getChildren().remove(secondReel);
			gridPane.add(secondAnimation, 1, 1);
			gridPane.getChildren().remove(thirdReel);
			gridPane.add(thirdAnimation, 2, 1);
		}
	}

	// to launch application
	public static void main(String[] args) {
		launch(args);
	}
}
