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

public class Reel {
	
	// static variables so each instance knows if a reel configuration
	// is available or not
	private static boolean isAvailable = true;
	private static boolean isAvailable2 = true;

	// scales the interface for different size screens
	private static final float SCALAR = .7f;

	// for images
	private Pane reelPane; // pane displays images
	private static final String ABSOLUTE_PATH = "file:" + System.getProperty("user.dir"); // image path
	private static final int NUM_IMAGES = 8;
	
	// paths to images todo: private static final
	private static final String IMG_SINGLE_BAR = "/src/images/singleBar.png",
			IMG_DOUBLE_BAR = "/src/images/doubleBar.png",
			IMG_TRIPLE_BAR = "/src/images/tripleBar2.png",
			IMG_SINGLE_7 = "/src/images/single7.png",
			IMG_DOUBLE_7 = "/src/images/double7s.png",
			IMG_TRIPLE_7 = "/src/images/triple7s.png",
			IMG_CHERRIES = "/src/images/cherries.png",
			IMG_TRIP_7_WINNER = "/src/images/3x7sWinner.png";

	// width and height for square images displayed in reel
	private static final int IMAGE_DIMENS = 300;

	// Image and ImageView objects to store images and animations
	private Image reelImage;
	private ImageView displayReel;
	private Pane pane;

	// constructor sets the initial reel values
	public Reel() {
		pane = new Pane();
		reelImage = new Image(ABSOLUTE_PATH + IMG_TRIP_7_WINNER);
		displayReel = new ImageView(reelImage);
		displayReel.setFitWidth(IMAGE_DIMENS * SCALAR);
		displayReel.setFitHeight(IMAGE_DIMENS * SCALAR);
		
		pane.getChildren().add(displayReel);
		setDisplayReel(pane);
	}
	
	// uses random generator to select image for reel to display when animation stops and return its String value
	public String spinReel() {
				
		String reelValue = null;
		Random randNum = new Random();
		int reelNum = randNum.nextInt(NUM_IMAGES);
		
		switch(reelNum) {
		case 0:
			reelImage = new Image(ABSOLUTE_PATH + IMG_SINGLE_BAR);
			setReelImage(reelImage);
			reelValue = "singleBar";
			break;
			
		case 1:
			reelImage = new Image(ABSOLUTE_PATH + IMG_DOUBLE_BAR);
			setReelImage(reelImage);
			reelValue = "doubleBar";
			break;
		
		case 2:
			reelImage = new Image(ABSOLUTE_PATH + IMG_TRIPLE_BAR);
			setReelImage(reelImage);
			reelValue = "tripleBar";
			break;
			
		case 3:
			reelImage = new Image(ABSOLUTE_PATH + IMG_SINGLE_7);
			setReelImage(reelImage);
			reelValue = "single7";
			break;
			
		case 4:
			reelImage = new Image(ABSOLUTE_PATH + IMG_DOUBLE_7);
			setReelImage(reelImage);
			reelValue = "double7s";
			break;
			
		case 5:
			reelImage = new Image(ABSOLUTE_PATH + IMG_TRIPLE_7);
			setReelImage(reelImage);
			reelValue = "triple7s";
			break;
			
		case 6:
			reelImage = new Image( ABSOLUTE_PATH + IMG_CHERRIES);
			setReelImage(reelImage);
			reelValue = "cherries";
			break;
			
		case 7:
			reelImage = new Image(ABSOLUTE_PATH + IMG_TRIP_7_WINNER);
			setReelImage(reelImage);
			reelValue = "trip7sWinner";
			break;
		}
		
		return reelValue;
	}

	// sets the reel image after animation stops spinning
	private void setReelImage(Image reelImage) {
		displayReel = new ImageView(reelImage);
		displayReel.setFitWidth(IMAGE_DIMENS * SCALAR);
		displayReel.setFitHeight(IMAGE_DIMENS * SCALAR);
		pane.getChildren().add(displayReel);
		setDisplayReel(pane);
	}

	// sets the static image of reel
	public void setDisplayReel(Pane reelPane) {
		this.reelPane = reelPane;
	}
	
	// returns the static image of reel
	public Pane getDisplayReel() {
		return reelPane;
	}
	
	// creates a line path for images to animate on
	public PathTransition setTransition(ImageView image, int vPos1, int vPos2){
		
		//creates a vertical line transition path
		int xPos = (int) Math.round(150 * SCALAR); // todo
		Path vLinePath = new Path();
		vLinePath.getElements().add(new MoveTo(xPos, vPos1));
		vLinePath.getElements().add(new LineTo(xPos, vPos2));
		vLinePath.setOpacity(0);
		
		// creates a transition and adds image to animate along line
		PathTransition pathTrans = new PathTransition();
		pathTrans.setNode(image);
		pathTrans.setPath(vLinePath);
		pathTrans.setRate(1.8);
		pathTrans.setCycleCount(Timeline.INDEFINITE);
		return pathTrans;
	}
	
	// returns an animated pane
	public Pane createAnimatedPane(){
		
		// create ImageView objects to animate

		ImageView viewSingle7 = getImageViewToAnimate(new Image(ABSOLUTE_PATH + IMG_SINGLE_7));
		ImageView viewDoubleBar = getImageViewToAnimate(new Image(ABSOLUTE_PATH + IMG_DOUBLE_BAR));
		ImageView viewSingleBar = getImageViewToAnimate(new Image(ABSOLUTE_PATH + IMG_SINGLE_BAR));
		ImageView viewCherries = getImageViewToAnimate(new Image(ABSOLUTE_PATH + IMG_CHERRIES));

		// group images to later add to a Pane
		Group group = new Group();
		group.getChildren().add(viewSingle7);
		group.getChildren().add(viewDoubleBar);
		group.getChildren().add(viewSingleBar);
		group.getChildren().add(viewCherries);
	    
		// creates path transitions with linear interpolation for images to animate along
		PathTransition tPath1 = getInterpolatedPathTransition();
		PathTransition tPath2 = getInterpolatedPathTransition();
		PathTransition tPath3 = getInterpolatedPathTransition();
		PathTransition tPath4 = getInterpolatedPathTransition();
//
//		// sets linear motion with no ease in or ease out
//		tPath1.setInterpolator(Interpolator.LINEAR);
//		tPath2.setInterpolator(Interpolator.LINEAR);
//		tPath3.setInterpolator(Interpolator.LINEAR);
//		tPath4.setInterpolator(Interpolator.LINEAR);
		
		// set images on transition path from one horizontal position to another horizontal position (straight line)
		int hPos1 = (int) Math.round(300 * SCALAR), // todo
			hPos2 = (int) Math.round(1000 * SCALAR),
			hPos3 = (int) Math.round(-50 * SCALAR),
			hPos4 = (int) Math.round(650 * SCALAR),
			hPos5 = (int) Math.round(-500 * SCALAR),
			hPos6 = (int) Math.round(500 * SCALAR),
			hPos7 = (int) Math.round(-650 * SCALAR),
			hPos8 = (int) Math.round(250 * SCALAR);
		
		// ensure each reel appears different while spinning
		if(isAvailable) { // first reel instantiation gets this order // todo create method for random orders?
			tPath1 = setTransition(viewDoubleBar, hPos1, hPos2);
			tPath2 = setTransition(viewCherries, hPos3, hPos4);
			tPath3 = setTransition(viewSingleBar, hPos5, hPos6);
			tPath4 = setTransition(viewSingle7, hPos7, hPos8);
			isAvailable = false;
		} else if(isAvailable2) { // second reel instantiation gets this order
			tPath1 = setTransition(viewSingle7, hPos1, hPos2);
			tPath2 = setTransition(viewSingleBar, hPos3, hPos4);
			tPath3 = setTransition(viewDoubleBar, hPos5, hPos6);
			tPath4 = setTransition(viewCherries, hPos7, hPos8);
			isAvailable2 = false;
		} else { // third reel instantiation gets this order
			tPath1 = setTransition(viewCherries, hPos1, hPos2);
			tPath2 = setTransition(viewSingle7, hPos3, hPos4);
			tPath3 = setTransition(viewSingleBar, hPos5, hPos6);
			tPath4 = setTransition(viewDoubleBar, hPos7, hPos8);
		}
		
		// parallel transition allows multiple animations to display simultaneously
		ParallelTransition parallelTrans = new ParallelTransition(tPath1, tPath2, tPath3, tPath4);
		parallelTrans.setInterpolator(Interpolator.LINEAR);
		parallelTrans.play();
		
		// adds elements to pane to return
		Pane animatedPane = new Pane();

		// masks the image to only show a square with specified dimensions todo getAnimatedPane()
		int clipDimension = (int) Math.round(IMAGE_DIMENS * SCALAR);
		Rectangle clipShape = new Rectangle(clipDimension, clipDimension); // clipShape is a mask
		animatedPane.setMaxHeight(clipDimension);
		animatedPane.setClip(clipShape);
		animatedPane.getChildren().add(group);
		return animatedPane;
	}

	private PathTransition getInterpolatedPathTransition() {
		PathTransition pt = new PathTransition();
		pt.setInterpolator(Interpolator.LINEAR);
		return pt;
	}

	private ImageView getImageViewToAnimate(Image image) {
		ImageView imgView = new ImageView(image);
		imgView.setFitWidth(IMAGE_DIMENS * SCALAR);
		imgView.setFitHeight(IMAGE_DIMENS * SCALAR);
		return imgView;
	}
}
