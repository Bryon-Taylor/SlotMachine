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
	static boolean isAvailable = true;
	static boolean isAvailable2 = true;
	
	// scales sizes
	double scalar = .7;
	// pane to store image
	Pane reelPane;
	
	// for path of images
	String absolutePath = System.getProperty("user.dir");
	
	// random number generator to select images
	Random randNum = null;
	
	// Strings point to path of images todo: private static final
	String reelAnimation = "/src/reelAnimation.gif"; // TODO: is this used?
	String singleBar = "/src/singleBar.png";
	String doubleBar = "/src/doubleBar.png";
	String tripleBar = "/src/tripleBar2.png";
	String single7 = "/src/single7.png";
	String double7s = "/src/double7s.png";
	String triple7s = "/src/triple7s.png";
	String cherries = "/src/cherries.png";
	String trip7sWinner = "/src/3x7sWinner.png";
	
	// String array to store images // TODO: delete array?
	String[] icons = {singleBar, doubleBar, tripleBar, single7,
					  double7s, triple7s, cherries, trip7sWinner};
	
	// Image and ImageView objects to store images and animations
	Image reelImage = null;
	ImageView displayReel = null;
	Pane pane = null;
	Image imageAnimation = null;
	ImageView displayAnimation = null;
	Pane pane2 = null;
	
	/** constructor sets the initial reel values */
	public Reel() {
		pane = new Pane();
		reelImage = new Image("file:" + absolutePath + trip7sWinner);
		displayReel = new ImageView(reelImage);
		displayReel.setFitWidth(300 * scalar); // todo: magic nums
		displayReel.setFitHeight(300 * scalar);
		
		pane.getChildren().add(displayReel);
		setDisplayReel(pane);
	}
	
	/** method uses random generator to select image for reel to display
	 * and returns its String value */
	public String spinReel() { 
				
		String reelValue = null;
		randNum = new Random();
		int reelNum = randNum.nextInt(8) + 1;
		
		switch(reelNum) {
		case 1:
			reelImage = new Image("file:" + absolutePath + singleBar);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "singleBar";
			break;
			
		case 2:
			reelImage = new Image("file:" + absolutePath + doubleBar);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "doubleBar";
			break;
		
		case 3:
			reelImage = new Image("file:" + absolutePath + tripleBar);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "tripleBar";
			break;
			
		case 4:
			reelImage = new Image("file:" + absolutePath + single7);
			displayReel = new ImageView(reelImage);
			pane.getChildren().add(displayReel);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			setDisplayReel(pane);
			reelValue = "single7";
			break;
			
		case 5:
			reelImage = new Image("file:" + absolutePath + double7s);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "double7s";
			break;
			
		case 6:
			reelImage = new Image("file:" + absolutePath + triple7s);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "triple7s";
			break;
			
		case 7:
			reelImage = new Image("file:" + absolutePath + cherries);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "cherries";
			break;
			
		case 8:
			reelImage = new Image("file:" + absolutePath + trip7sWinner);
			displayReel = new ImageView(reelImage);
			displayReel.setFitWidth(300 * scalar);
			displayReel.setFitHeight(300 * scalar);
			pane.getChildren().add(displayReel);
			setDisplayReel(pane);
			reelValue = "trip7sWinner";
			break;
		}
		
		return reelValue;
	}
	
	/** creates a new animation to display and returns it in a pane */
	public Pane setAnimation() {
		pane2 = new Pane();
		imageAnimation = new Image("file:" + absolutePath + reelAnimation);
		displayAnimation = new ImageView(imageAnimation);
		//displayAnimation.setFitWidth(300 * scalar);
		//displayAnimation.setFitHeight(300 * scalar);
		pane2.getChildren().add(displayAnimation);
		setDisplayReel(pane2);
		
		return pane2;
	}
	
	/** sets the static image of reel */
	public void setDisplayReel(Pane reelPane) {
		this.reelPane = reelPane;
	}
	
	/** returns the static image of reel */
	public Pane getDisplayReel() {
		return reelPane;
	}
	
	/** creates a line transition path for images to animate on */
	public PathTransition setTransition(ImageView image, int vPos1, int vPos2){
		
		//creates a vertical line transition path
		int xPos = (int) Math.round(150 * scalar);
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
	
	/** creates an animated pane and returns it */
	public Pane createAnimatedPane(){
		
		//images to animate
		Group group = new Group();
		String single7 = "/src/single7.png";
		String singleBar = "/src/singleBar.png";
		String doubleBar = "/src/doubleBar.png";
		String cherries = "/src/cherries.png";
		
		// directory for images
		String absolutePath = System.getProperty("user.dir");
		
		// create image objects to animate 
	    Image seven = new Image("file:" + absolutePath + single7);
	    ImageView viewSeven = new ImageView(seven);
	    viewSeven.setFitWidth(300 * scalar);
	    viewSeven.setFitHeight(300 * scalar);
	      
	    Image doubleBars = new Image("file:" + absolutePath + doubleBar);
	    ImageView viewDoubleBar = new ImageView(doubleBars);
	    viewDoubleBar.setFitWidth(300 * scalar);
	    viewDoubleBar.setFitHeight(300 * scalar);
	      
	    Image singleBr = new Image("file:" + absolutePath + singleBar);
	    ImageView viewSingleBar = new ImageView(singleBr);
	    viewSingleBar.setFitWidth(300 * scalar);
	    viewSingleBar.setFitHeight(300 * scalar);
	      
	    Image cherriesBar = new Image("file:" + absolutePath + cherries);
	    ImageView viewCherries = new ImageView(cherriesBar);
	    viewCherries.setFitWidth(300 * scalar);
	    viewCherries.setFitHeight(300 * scalar);
	     
	    group.getChildren().add(viewSeven);
	    group.getChildren().add(viewSingleBar);
	    group.getChildren().add(viewDoubleBar);
	    group.getChildren().add(viewCherries);
	    
	    // creates path transitions for image objects
		PathTransition tPath1 = new PathTransition();
		PathTransition tPath2 = new PathTransition();
		PathTransition tPath3 = new PathTransition();
		PathTransition tPath4 = new PathTransition();
		
		// sets linear motion with no ease in or ease out
		tPath1.setInterpolator(Interpolator.LINEAR);
		tPath2.setInterpolator(Interpolator.LINEAR);
		tPath3.setInterpolator(Interpolator.LINEAR);
		tPath4.setInterpolator(Interpolator.LINEAR);
		
		// set images on transition path from one horizontal 
		// position to another horizontal position (straight line)
		int hPos1 = (int) Math.round(300 * scalar),
			hPos2 = (int) Math.round(1000 * scalar),
			hPos3 = (int) Math.round(-50 * scalar),
			hPos4 = (int) Math.round(650 * scalar),
			hPos5 = (int) Math.round(-500 * scalar),
			hPos6 = (int) Math.round(500 * scalar),
			hPos7 = (int) Math.round(-650 * scalar),
			hPos8 = (int) Math.round(250 * scalar);
		
		// ensure each reel appears different while spinning
		if(isAvailable){
			tPath1 = setTransition(viewDoubleBar, hPos1, hPos2);
			tPath2 = setTransition(viewCherries, hPos3, hPos4);
			tPath3 = setTransition(viewSingleBar, hPos5, hPos6);
			tPath4 = setTransition(viewSeven, hPos7, hPos8);
			isAvailable = false;
		} else if (isAvailable2) {
			tPath1 = setTransition(viewSeven, hPos1, hPos2);
			tPath2 = setTransition(viewSingleBar, hPos3, hPos4);
			tPath3 = setTransition(viewDoubleBar, hPos5, hPos6);
			tPath4 = setTransition(viewCherries, hPos7, hPos8);
			isAvailable2 = false;
		} else {
			tPath1 = setTransition(viewCherries, hPos1, hPos2);
			tPath2 = setTransition(viewSeven, hPos3, hPos4);
			tPath3 = setTransition(viewSingleBar, hPos5, hPos6);
			tPath4 = setTransition(viewDoubleBar, hPos7, hPos8);
		}
		
		// parallel transition allows multiple animations
		// to display simultaneously
		ParallelTransition parallelTrans = new ParallelTransition
										   (tPath1, tPath2, tPath3, tPath4);
		parallelTrans.setInterpolator(Interpolator.LINEAR);
		parallelTrans.play();
		
		// adds elements to pane to return
		Pane animatedPane = new Pane();
		// masks the image to only show a 300, 300 square
		int clipDimension = (int) Math.round(300 * scalar);
		Rectangle clipShape = new Rectangle(clipDimension, clipDimension);
		animatedPane.setMaxHeight(clipDimension);
		animatedPane.setClip(clipShape);
		animatedPane.getChildren().add(group);
		return animatedPane;
	}
}
