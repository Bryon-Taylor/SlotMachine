import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

public class Reel {

	// scales the interface for different size screens
	private static final double SCALE_FACTOR = 0.9;
	
	// used to change image animation order for all three Reels
	private static boolean
			firstOrderAvailable = true,
			secondOrderAvailable = true;

	// for images
	private Pane reelPane; // pane displays images
	private static final String ABSOLUTE_PATH = "file:" + System.getProperty("user.dir"); // image path

	// paths for ImageViews to animate on
	private PathTransition
			tPath1,
			tPath2,
			tPath3,
			tPath4;

	// start and end y-coordinates on line path
	private static final int
			Y_START_1 = toIntExact(round(300 * SCALE_FACTOR)),
			Y_END_1 = toIntExact(round(1000 * SCALE_FACTOR)),

			Y_START_2 = toIntExact(round(-50 * SCALE_FACTOR)),
			Y_END_2 = toIntExact(round(650 * SCALE_FACTOR)),

			Y_START_3 = toIntExact(round(-500 * SCALE_FACTOR)),
			Y_END_3 = toIntExact(round(500 * SCALE_FACTOR)),

			Y_START_4 = toIntExact(round(-650 * SCALE_FACTOR)),
			Y_END_4 = toIntExact(round(250 * SCALE_FACTOR));

	// paths to images
	private static final String
			IMG_SINGLE_BAR = "/src/images/singleBar.png",
			IMG_DOUBLE_BAR = "/src/images/doubleBar.png",
			IMG_TRIPLE_BAR = "/src/images/tripleBar2.png",
			IMG_SINGLE_7 = "/src/images/single7.png",
			IMG_DOUBLE_7 = "/src/images/double7s.png",
			IMG_TRIPLE_7 = "/src/images/triple7s.png",
			IMG_CHERRIES = "/src/images/cherries.png",
			IMG_TRIP_7_WINNER = "/src/images/3x7sWinner.png";

	private static final int IMAGE_DIMENS = 300; // default width and height for square images displayed in reel
	private Pane pane; // Pane to display static images and animations
	private Random random; // Random number generator to determine reel's value

	// constructor sets the initial reel values
	public Reel() {
		random = new Random();

		// initializes all the reels to the "Triple 7 winner" image at the start of the game
		pane = new Pane();
		pane.getChildren().add(getImageView(new Image(ABSOLUTE_PATH + IMG_TRIP_7_WINNER)));
		setDisplayReel(pane);
	}
	
	// uses random generator to select image for reel to display when animation stops and returns its String value
	// String reelValue variable is used in SlotMachine.java's handleWinningCombos() method to determine a winning spin
	public String spinReel() {
		final int randomUpperBound = 100; // non-inclusive
		String reelValue;
		int reelNum = random.nextInt(randomUpperBound); // generate random number between from 0 to randomUpperBound - 1

		System.out.println(reelNum);
		// non-inclusive UPPER bounds to adjust probabilities
		final int singleBarBound = 16;
		final int doubleBarBound = 31;
		final int tripleBarBound = 44;
		final int cherriesBound = 47;
		final int single7Bound = 64;
		final int double7Bound = 78;
		final int triple7Bound	= 90;

		// each if statement creates a range bucket for the random number to fall in
		// first "if" statement's LOWER bound is 0, all "else (if)" statements' LOWER bound is the
		// UPPER bound of the statement that precedes it
		if(reelNum < singleBarBound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_SINGLE_BAR));
			reelValue = "singleBar";
		} else if(reelNum < doubleBarBound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_DOUBLE_7));
			reelValue = "double7s";
		} else if(reelNum < tripleBarBound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_TRIPLE_BAR));
			reelValue = "tripleBar";
		} else if(reelNum < cherriesBound) {
			setReelImage(new Image( ABSOLUTE_PATH + IMG_CHERRIES));
			reelValue = "cherries";
		} else if(reelNum < single7Bound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_SINGLE_7));
			reelValue = "single7";
		} else if(reelNum < double7Bound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_DOUBLE_BAR));
			reelValue = "doubleBar";
		} else if(reelNum < triple7Bound) {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_TRIPLE_7));
			reelValue = "triple7s";
		} else {
			setReelImage(new Image(ABSOLUTE_PATH + IMG_TRIP_7_WINNER));
			reelValue = "trip7sWinner";
		}
		return reelValue;
	}

	// sets the reel image during first initialization or when animation stops spinning
	private void setReelImage(Image reelImage) {
		ImageView displayReel = new ImageView(reelImage);
		displayReel.setFitWidth(IMAGE_DIMENS * SCALE_FACTOR);
		displayReel.setFitHeight(IMAGE_DIMENS * SCALE_FACTOR);
		pane.getChildren().add(displayReel);
		setDisplayReel(pane);
	}

	// sets the static image of reel
	private void setDisplayReel(Pane reelPane) {
		this.reelPane = reelPane;
	}
	
	// returns the Pane that contains the static image of reel
	public Pane getDisplayReel() {
		return reelPane;
	}

	// returns an animated pane
	public Pane createAnimatedPane(){

		// create ImageView objects to animate
		ImageView viewSingle7 = getImageView(new Image(ABSOLUTE_PATH + IMG_SINGLE_7));
		ImageView viewDoubleBar = getImageView(new Image(ABSOLUTE_PATH + IMG_DOUBLE_BAR));
		ImageView viewSingleBar = getImageView(new Image(ABSOLUTE_PATH + IMG_SINGLE_BAR));
		ImageView viewCherries = getImageView(new Image(ABSOLUTE_PATH + IMG_CHERRIES));

		// group images to add to animatedPane
		Group group = new Group();
		group.getChildren().add(viewSingle7);
		group.getChildren().add(viewDoubleBar);
		group.getChildren().add(viewSingleBar);
		group.getChildren().add(viewCherries);

		// ensure each reel appears in a different order while spinning
		if(firstOrderAvailable) { // first reel instantiation gets this order
			changeReelImageOrder(viewDoubleBar, viewCherries, viewSingleBar, viewSingle7);
			firstOrderAvailable = false;
		} else if(secondOrderAvailable) { // second reel instantiation gets this order
			changeReelImageOrder(viewSingle7, viewSingleBar, viewDoubleBar, viewCherries);
			secondOrderAvailable = false;
		} else { // third reel instantiation gets this order
			changeReelImageOrder(viewCherries, viewSingle7, viewDoubleBar, viewSingleBar);
		}

		// parallel transition allows multiple animations to display simultaneously
		ParallelTransition parallelTrans = new ParallelTransition(tPath1, tPath2, tPath3, tPath4);
		parallelTrans.play();

		// masks the image to only show a square with specified dimensions
		Pane animatedPane = new Pane();
		int clipDimension = toIntExact(round(IMAGE_DIMENS * SCALE_FACTOR));
		Rectangle clipShape = new Rectangle(clipDimension, clipDimension); // clipShape is a mask
		animatedPane.setMaxHeight(clipDimension);
		animatedPane.setClip(clipShape); // apply mask
		animatedPane.getChildren().add(group);
		return animatedPane;
	}

	// creates a vertical line path for images to animate on, yPos = y coordinate
	private PathTransition setTransition(ImageView image, int yPos1, int yPos2){

		// creates a vertical line transition path
		// (IMAGE_DIMENS / 2.0) keeps x position centered in Pane
		int xPos = toIntExact(round((IMAGE_DIMENS / 2.0) * SCALE_FACTOR));
		Path vLinePath = new Path();
		vLinePath.getElements().add(new MoveTo(xPos, yPos1)); // beginning coordinates
		vLinePath.getElements().add(new LineTo(xPos, yPos2)); // ending coordinates
		vLinePath.setOpacity(0); // invisible line

		// creates a transition and adds image to animate along a vertical line
		PathTransition pathTrans = new PathTransition();
		pathTrans.setNode(image);
		pathTrans.setPath(vLinePath);
		pathTrans.setRate(2);
		pathTrans.setCycleCount(Timeline.INDEFINITE);
		return pathTrans;
	}

	// first arg is object to translate, second arg is starting y-coordinate, third arg is ending y-coordinate
	private void changeReelImageOrder(ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4) {
		tPath1 = setTransition(iv1, Y_START_1, Y_END_1);
		tPath2 = setTransition(iv2, Y_START_2, Y_END_2);
		tPath3 = setTransition(iv3, Y_START_3, Y_END_3);
		tPath4 = setTransition(iv4, Y_START_4, Y_END_4);
	}

	// return an ImageView container from the passed image path
	private ImageView getImageView(Image image) {
		ImageView imgView = new ImageView(image);
		imgView.setFitWidth(IMAGE_DIMENS * SCALE_FACTOR);
		imgView.setFitHeight(IMAGE_DIMENS * SCALE_FACTOR);
		return imgView;
	}
}
