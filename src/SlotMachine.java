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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SlotMachine extends Application {
	
	// scales the interface for different size screens todo: scale entire application
	private static final double SCALAR = .7f;

	// default width and height for game window
	private static final int WINDOW_WIDTH = 1100;
	private static final int WINDOW_HEIGHT = 1000;

	// default sizes for fields and their fonts
	private static final int FONT_SIZE_FIELDS = 30;
	private static final int FONT_SIZE_FIELD_DESCRIPTION = 35;
	private static final int FIELD_WIDTH = 300;
	private static final int FIELD_HEIGHT = 50;

	// variables to manage bets and credits
	private int bet = 1; // default bet amount
	private int maxBet = 5;
	private int creditsPaid,
			creditsWon,
			credits,
			updatedCredits;

	// multipliers for winnings
	private static final int MULTIPLIER1 = 1,
			MULTIPLIER2 = 3,
			MULTIPLIER3 = 5,
			MULTIPLIER4 = 10,
			MULTIPLIER5 = 15,
			MULTIPLIER6 = 25,
			MULTIPLIER7 = 50,
			MULTIPLIER8 = 75,
			JACKPOT_MULTIPLIER = 1000;
	
	// Array to hold dollar amounts. Amounts are displayed in the "add cash" dialog
	private static final Integer[] dollarAmounts = {1, 5, 10, 20, 50, 100};

	// instantiate three Reel objects
	private Reel reel1 = new Reel();
	private Reel reel2 = new Reel();
	private Reel reel3 = new Reel();

	// String representation of a reel's image, e.g. "cherries" or "triple7s"
	private String reel1Value,
			reel2Value,
			reel3Value;

	// UI components
	private TextField creditsField,
			betField,
			paidField;

	private Text creditsText,
			betText,
			paidText;

	private Button addBetBtn,
			minusBetBtn,
			addCashBtn,
			cashOutBtn,
			spinBtn;

	// Reels, e.g. "firstReel", display static image
	// Animations, e.g. "firstAnimation", display motion
	private Pane firstReel,
			secondReel,
			thirdReel,
			firstAnimation,
			secondAnimation,
			thirdAnimation;

	private HBox hBoxTop; // Container for top image
	private GridPane gridPane; // Container for game UI elements

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
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		root.setAlignment(Pos.TOP_CENTER);
		root.add(hBoxTop, 0, 0);
		root.add(gridPane, 0, 1);

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

		// font style for fields
		int fontSize = (int) Math.round(FONT_SIZE_FIELDS * SCALAR);
		Font font = new Font("SansSerif", fontSize);

		// width and height for CREDITS, BET, PAID fields
		int fieldWidth = (int) Math.round(FIELD_WIDTH * SCALAR);
		int fieldHeight = (int) Math.round(FIELD_HEIGHT * SCALAR);

		// text field showing available credits
		int initialCredits = 0;
		creditsField = new TextField(String.valueOf(initialCredits));
		creditsField.setAlignment(Pos.CENTER_RIGHT);
		creditsField.setFont(font);
		creditsField.setEditable(false);
		creditsField.setPrefSize(fieldWidth, fieldHeight);
		creditsField.setMaxWidth(fieldWidth);

		// text field for bet amount
		betField = new TextField(String.valueOf(bet));
		betField.setAlignment(Pos.CENTER_RIGHT);
		betField.setFont(font);
		betField.setEditable(false);
		betField.setPrefSize(fieldWidth, fieldHeight);
		betField.setMaxWidth(fieldWidth);

		// text field to show the pay out for a spin
		paidField = new TextField(String.valueOf(creditsPaid));
		paidField.setAlignment(Pos.CENTER_RIGHT);
		paidField.setFont(font);
		paidField.setEditable(false);
		paidField.setPrefSize(fieldWidth, fieldHeight);
		paidField.setMaxWidth(fieldWidth);
	}

	// initialize text descriptions for fields
	private void initFieldDescriptions() {

		// scale font size
		int fontSize = (int) Math.round(FONT_SIZE_FIELD_DESCRIPTION * SCALAR);

		// CREDITS
		creditsText = new Text("CREDITS");
		creditsText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
		creditsText.setFill(Color.FIREBRICK);

		// BET
		betText = new Text("BET");
		betText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
		betText.setFill(Color.FIREBRICK);

		// WINNER PAID
		paidText = new Text("WINNER PAID");
		paidText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
		paidText.setFill(Color.FIREBRICK);
	}

	// initialize game buttons
	private void initButtons() {

		// default button scale values
		int defaultBtnScale = 2; // "add bet", "minus bet", "add cash", "cashout" buttons
		int defaultSpinBtnScale = 4; // spin button

		int modBtnScale = (int) Math.round(defaultBtnScale * SCALAR); // modified button scale

		// create buttons
		addBetBtn = createButton(modBtnScale, "ADD BET");
		minusBetBtn = createButton(modBtnScale, "MINUS BET");
		addCashBtn = createButton(modBtnScale,"ADD CASH");
		cashOutBtn = createButton(modBtnScale,"CASHOUT");

		// spin button has larger scale
		int modSpinBtnScale = (int) Math.round(defaultSpinBtnScale * SCALAR); // modified spin button scale
		spinBtn = createButton(modSpinBtnScale,"SPIN");
	}

	private Button createButton(int buttonScale, String label) {
		Button button = new Button(label);
		button.setScaleX(buttonScale);
		button.setScaleY(buttonScale);
		button.setStyle("-fx-base: #14425a;");
		return button;
	}

	// initialize button listeners for all game buttons and define behaviors
	private void initButtonListeners() {

		// "add bet" button listener adds to the bet amount up to maxBet (5)
		addBetBtn.setOnAction(e -> {
			if(bet < maxBet) {
				bet++;
				betField.setText(Integer.toString(bet));

				// play audio to indicate bet increased
				playAudioClip("audio/SMB_Coin.wav");
			}
		});

		// "minus bet" button listener subtracts from the bet amount
		minusBetBtn.setOnAction(e -> {
			if(bet > 1){
				bet--;
				betField.setText(Integer.toString(bet));

				// play audio to indicate bet decreased
				playAudioClip("audio/SMB_Coin.wav");
			}
		});

		// "add cash" button listener adds cash amount to existing credits
		addCashBtn.setOnAction(e -> {
			int addedAmt = addCash();
			credits = Integer.parseInt(creditsField.getText());
			creditsField.setText(String.valueOf(credits + addedAmt));
		});

		// "cash out" button listener will end the game with an Alert dialog showing player winnings
		cashOutBtn.setOnAction(e -> {

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

			// play sound file of coins dropping to indicate user cashed out
			playAudioClip("audio/coinsDroppingShort.wav");
		});

		// separate inner class to handle "spin" button click events
		spinBtn.setOnAction(new SpinButtonHandler());
	}

	private void initGameLayout() {
		final int DEFAULT_TOP_PADDING = 40;
		final int DEFAULT_TOP_IMAGE_WIDTH = 650;
		final int DEFAULT_TOP_IMAGE_HEIGHT = 309;
		final int DEFAULT_BUTTON_BOX_SPACING = 10;
		final int DEFAULT_HOR_GRID_GAP = 25;
		final int DEFAULT_VERT_GRID_GAP = 15;

		int hBoxInsetTop = (int) Math.round(DEFAULT_TOP_PADDING * SCALAR); // modified top padding

		// initialize top image and style
		Image imgStardust = new Image("file:" + ABSOLUTE_PATH + "/src/images/stardustLarger.gif");
		ImageView stardustImgView = new ImageView(imgStardust);
		stardustImgView.setFitWidth(DEFAULT_TOP_IMAGE_WIDTH * SCALAR);
		stardustImgView.setFitHeight(DEFAULT_TOP_IMAGE_HEIGHT * SCALAR);

		// create a new horizontal box to contain top image
		hBoxTop = new HBox();
		hBoxTop.setAlignment(Pos.TOP_CENTER);
		hBoxTop.setPadding(new Insets(hBoxInsetTop, 0, 0, 0));
		hBoxTop.getChildren().add(stardustImgView);

		// initializes reels to all triple 7's image
		firstReel = reel1.getDisplayReel();
		secondReel = reel2.getDisplayReel();
		thirdReel = reel3.getDisplayReel();

		// get animations of spinning reels
		firstAnimation = reel1.createAnimatedPane();
		secondAnimation = reel2.createAnimatedPane();
		thirdAnimation = reel3.createAnimatedPane();

		// create a new horizontal box for the "spin" button
		HBox hBoxBottom = new HBox();
		hBoxBottom.setAlignment(Pos.BOTTOM_CENTER);
		hBoxBottom.setPadding((new Insets(hBoxInsetTop, 0, 0, 0)));
		hBoxBottom.getChildren().add(spinBtn);

		// create new horizontal box to contain "add bet" and "minus bet" buttons
		HBox adjustBetBox = new HBox(DEFAULT_BUTTON_BOX_SPACING);
		adjustBetBox.setAlignment(Pos.BOTTOM_CENTER);
		adjustBetBox.getChildren().add(addBetBtn);
		adjustBetBox.getChildren().add(minusBetBtn);

		// create new horizontal box to contain "add cash" and "cash out" buttons
		HBox playOrQuitBox = new HBox(DEFAULT_BUTTON_BOX_SPACING);
		playOrQuitBox.setAlignment(Pos.BOTTOM_CENTER);
		playOrQuitBox.getChildren().add(addCashBtn);
		playOrQuitBox.getChildren().add(cashOutBtn);

		// columns for gridPane holding UI elements
		final int COL0 = 0,
				COL1 = 1,
				COL2 = 2;

		// rows for gridPane holding UI elements
		final int ROW1 = 1,
				ROW2 = 2,
				ROW3 = 3,
				ROW4 = 4;

		// add arranges all UI components in a grid
		gridPane = new GridPane();

		int horizontalGap = (int) Math.round(DEFAULT_HOR_GRID_GAP * SCALAR); // modified gaps
		int verticalGap = (int) Math.round(DEFAULT_VERT_GRID_GAP * SCALAR);

		// set spacing gaps between elements
		gridPane.setHgap(horizontalGap);
		gridPane.setVgap(verticalGap);

		// add all components to gridPane layout
		gridPane.add(adjustBetBox, COL0, ROW4); // "add bet" & "minus bet" buttons
		gridPane.add(hBoxBottom, COL1, ROW4); // "spin" button
		gridPane.add(playOrQuitBox, COL2, ROW4); // "add cash" & "cash out" buttons

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
		int dollarAmt = 0;

		// dialog displays a drop down list of dollar amounts, default is $1
		ChoiceDialog<Integer> addCashDialog = new ChoiceDialog<> (1, dollarAmounts);
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
	public int reelsMatch(String reel1) {
		switch(reel1){
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
			paidField.setText("0");

			// get current credits
			credits = Integer.parseInt(creditsField.getText());

			// if current credits > zero and credits minus bet is >= 0, update credits field minus bet
			if(credits > 0 && (credits - bet) >= 0) {
				credits -= bet;
				creditsField.setText(String.valueOf(credits));
			} else { // else prompt user to add cash to play

				// addCash() will return a dollar amount, add this amount to current credits
				int addedAmt = addCash();
				credits += addedAmt;

				// loop will continuously prompt user to add cash while the bet exceeds the added amount plus current credits
				while((credits - bet) < 0) {
					creditsField.setText(String.valueOf(credits));
					addedAmt = addCash();
					credits += addedAmt;
				}
				creditsField.setText(String.valueOf(credits - bet));
			}

			animateReels(); // starts the reels spinning
			playAudioClip("audio/spinningReels.wav"); // plays audio while reels spin

			// durations in milliseconds to stop animations
			final int PAUSE_TIME1 = 1500, // 1.5 seconds
								PAUSE_TIME2 = 1900, // 1.9 seconds
								PAUSE_TIME3 = 2300; // 2.3 seconds

			// to stop the reels from spinning after specified time
			PauseTransition pauseFirstReel = new PauseTransition(Duration.millis(PAUSE_TIME1));
			PauseTransition pauseSecondReel = new PauseTransition(Duration.millis(PAUSE_TIME2));
			PauseTransition pauseThirdReel = new PauseTransition(Duration.millis(PAUSE_TIME3));

			// after reel stops spinning replace the animation with a randomly generated static image
			pauseFirstReel.setOnFinished(event -> {
				gridPane.getChildren().remove(firstAnimation); // remove animation from Pane
				gridPane.add(firstReel, 0, 1); // replace animation with static image in Pane
				reel1Value = reel1.spinReel(); // String description of the image

				playAudioClip("audio/reelStop.wav"); // audio to indicate reel stopped spinning
			});
			pauseFirstReel.play();

			// after reel stops spinning replace the animation with a randomly generated static image
			pauseSecondReel.setOnFinished(event -> {
				gridPane.getChildren().remove(secondAnimation); // remove animation from Pane
				gridPane.add(secondReel, 1, 1); // replace animation with static image in Pane
				reel2Value = reel2.spinReel(); // String description of the image

				// audio to indicate reel stopped spinning
				playAudioClip("audio/reelStop.wav");
			});
			pauseSecondReel.play();

			pauseThirdReel.setOnFinished(event -> {
				gridPane.getChildren().remove(thirdAnimation); // remove animation from Pane
				gridPane.add(thirdReel, 2, 1); // replace animation with static image in Pane
				reel3Value = reel3.spinReel(); // String description of the image

				// audio to indicate reel stopped spinning
				playAudioClip("audio/reelStop.wav");

				// after third reel stops, check all reels' values to determine if there is a winning combo
				handleWinningCombos();
			});
			pauseThirdReel.play();
		}

		// check if there are winning combos and award pay outs
		private void handleWinningCombos() {

			// if all three reels are equal
			if(reel1Value.equals(reel2Value) && reel2Value.equals(reel3Value)) {

				// play audio to indicate winner
				playAudioClip("audio/casinoWinShort.wav");

				// gets proper pay out and updates credit field
				creditsPaid = reelsMatch(reel1Value);
				updateCreditsAndPaidFields();

				// if all three reels contain bars but are not equal e.g. singleBar + doubleBar + doubleBar
			} else if(reel1Value.contains("Bar") && reel2Value.contains("Bar") && reel3Value.contains("Bar")) {

				//play sound to indicate winner
				playAudioClip("audio/casinoWinShort.wav");

				// gets proper pay out and updates credit field
				creditsPaid = nonMatchedBars();
				updateCreditsAndPaidFields();

				// if all reels contain 7's but are not equal e.g. single7 + double7 + triple7
			} else if(reel1Value.contains("7") && reel2Value.contains("7") && reel3Value.contains("7")) {

				// play sound to indicate winner
				playAudioClip("audio/casinoWinShort.wav");

				// determines proper pay out and updates credit field
				creditsPaid = nonMatchedSevens();
				updateCreditsAndPaidFields();
			}
		}

		// updates number of credits and winner paid fields after a win
		private void updateCreditsAndPaidFields() {
			paidField.setText(String.valueOf(creditsPaid));
			updatedCredits = credits + creditsPaid;
			creditsField.setText(String.valueOf(updatedCredits));
		}

		// replaces static images in Panes with animations
		private void animateReels() {

			// replaces static images with animated reels
			gridPane.getChildren().remove(firstReel);
			gridPane.add(firstAnimation, 0 , 1);
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
