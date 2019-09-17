
package spritesplitter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

class Pair{
	public int x;
	public int y;

	public Pair(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean ret =  (obj != null) && (obj.getClass() == this.getClass());
		if(ret){
			Pair objectSpecificTester = (Pair)obj; 
			ret = x == objectSpecificTester.x && y == objectSpecificTester.y;
		}
		return ret;
	}
}

class Image{
	public static void main(String args[]) throws Exception {
		String[] a = new String[1];
		a[0] = "Goku.png";
		args = a;
		if (args.length != 1) {
			System.out.println("you didnt give me a file");
		} 
		else {
			Image originalImage = new Image(args[0]);
			originalImage.getImages();
		}
	}

	public int h;
	public int w;
	public int x;
	public int y;
	private int backgroundColor;
	private String fileName;
	private ArrayList<ArrayList<Integer>> pixels;
	private ArrayList<Pair> locationsThatAreNotBGColor;
	static private int filesGenerated = 1;
	
	public Image(String loadThis) throws Exception{
		fileName = loadThis.replaceFirst("[.][^.]+$", "");
		BufferedImage bi = ImageIO.read(new File(loadThis));
		backgroundColor = bi.getRGB(0,0);
		pixels = new ArrayList<ArrayList<Integer>>();
		locationsThatAreNotBGColor = new ArrayList<Pair>();
		x = 0;
		y = 0;
		h = bi.getHeight();
		w = bi.getWidth();
		for(int currentRow = 0; currentRow < h; currentRow++){
			ArrayList<Integer> pixelsInCurrentRow = new ArrayList<Integer>();
			for(int currentColumn = 0; currentColumn < w; currentColumn++){
				int currentColor = bi.getRGB(currentColumn,currentRow);
				pixelsInCurrentRow.add(currentColor);
				if(currentColor != backgroundColor){
					locationsThatAreNotBGColor.add(new Pair(currentRow,currentColumn));
				}
			}
			pixels.add(pixelsInCurrentRow);
		}
	}

	public Image(String fileName, int backgroundColor, int x, int y, int w, int h, ArrayList<ArrayList<Integer>> pixels){
		this.h = h;
		this.w = w;
		this.x = x;
		this.y = y;
		this.backgroundColor = backgroundColor;
		this.fileName = fileName;
		this.pixels = pixels;
	}

	public ArrayList<Image> getImages(){
		ArrayList<Image> individualImages = new ArrayList<Image>();
		while(!locationsThatAreNotBGColor.isEmpty()){
			Pair currentSmallest = findSmallestNotBGColor();
			int currentX = currentSmallest.x;
			while(pixels.get(currentSmallest.y).get(currentX+1) != backgroundColor){
				currentX++;
			}
			int currentY = currentSmallest.y;
			while(pixels.get(currentY+1).get(currentSmallest.x) != backgroundColor){
				currentY++;
			}
			int w = currentX - currentSmallest.x;
			int h = currentY - currentSmallest.y;
			for(int h2 = currentSmallest.y; h2 < currentSmallest.y + h; h2++){
				for(int w2 = currentSmallest.x; w2 < currentSmallest.x + w; w2++){
					
				}
			}
		}
		return individualImages;
	}

	public Pair findSmallestNotBGColor(){
		Pair currentSmallest = locationsThatAreNotBGColor.get(0);
		for(Pair current : locationsThatAreNotBGColor){
			if(currentSmallest.x > current.x){
				currentSmallest = current;
			}
			else if(currentSmallest.x == current.x && currentSmallest.y > current.y){
				currentSmallest = current;
			}
		}
		return currentSmallest;
	}

	/*
	make an array of images to return when we are done
	make an array of arrays of pixels to hold the image we are currently building
	i need a marker to keep track of where the new images topleft y will be. the first is going to be the same as source image
	i also need to track what the next ones topleft y will be
	for every row of pixels in this image
		increment the tracker to keep track of the next images top left y
		figure out if they are all the same
	if they are all the same
		check if there are any rows in the new image we are building
			if so add what we have to the list of new images
			start a new image
			and set the tracker for the next new images top left y 
		since they are all the same reset the current image we are building to go to the next one
	if they are not all the same
		add this row to this image we are currently making
	*/
	public ArrayList<Image> splitOnRowsOfSameColor(){
		ArrayList<Image> ret = new ArrayList<Image>();
		ArrayList<ArrayList<Integer>> pixelsInImage = new ArrayList<ArrayList<Integer>>();
		int topLeftYOfCurrentSplitImage = 0;
		int topLeftYOfNextSplitImage = 0;
		for(ArrayList<Integer> row : pixels){
			topLeftYOfNextSplitImage++;
			boolean allAreSame = true;
			Integer compareToMe = row.get(0);
			for(Integer pixel : row){
				if(!pixel.equals(compareToMe)){
					allAreSame = false;
					break;
				}
			}
			if(allAreSame){
				if(!pixelsInImage.isEmpty()){
					ret.add(new Image(fileName, backgroundColor, x, topLeftYOfCurrentSplitImage, w, pixelsInImage.size(), pixelsInImage));
					pixelsInImage = new ArrayList<ArrayList<Integer>>();
					topLeftYOfCurrentSplitImage = topLeftYOfNextSplitImage;
				}
			}
			else{
				pixelsInImage.add(row);
			}
		}
		return ret;
	}

	public ArrayList<Image> splitOnColumsnOfSameColor(){
		return null;
	}

	public void writeToFile() throws Exception{
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig2 = bi.createGraphics();
		for (int currentY = 0; currentY < h; currentY++) {
			for (int currentX = 0; currentX < w; currentX++) {
				Color currentColor = new Color(pixels.get(currentY).get(currentX));
				ig2.setColor(currentColor);
				ig2.fillRect(currentX, currentY, 1, 1);
			}
		}
		File dir = new File("./" + fileName);
		dir.mkdir();
		ImageIO.write(bi, "PNG", new File("./" + fileName + "/" + fileName + "-" + filesGenerated + ".png"));
		filesGenerated++;
	}
}















































// class Image {
// 	public static void main(String args[]) throws Exception {
// 		// if they did not give me a file
// 		if (args.length != 1) {
// 			//tell them they did not give me a file
// 			System.out.println("you didnt give me a file");
// 		} 
// 		//if i did get a file
// 		else {
// 			// read the file into an instance of the image class(which is really just a list of RowOfPixels)
// 			Image originalImage = new Image(new File(args[0]));

// 			// split the original image into  itsrows
// 			ArrayList<Image> rows = originalImage.splitIntoArrayOfImagesWhereRowAllSameColor();

// 			// make a place that will hold all the images that we generate
// 			ArrayList<Image> images = new ArrayList<Image>();

// 			// for each row
// 			for (Image row : rows) {
// 				// get the sprites in the current row
// 				ArrayList<Image> spritesInRow = row.splitIntoArrayOfImagesWhereColumnAllSameColor();

// 				// for each sprite
// 				for (Image currentSprite : spritesInRow) {
// 					images.add(currentSprite);
// 				}
// 			}

// 			boolean needToCreateXMLFile = true;

// 			for (Image image : images) {

// 				image.cropSides();
				
// 				image.writeImageToFile(needToCreateXMLFile);

// 				needToCreateXMLFile = false;
// 			}

// 			Files.write(Paths.get("./" + fileName_ + "/" + fileName_ + ".xml"), "</spritesheet>".getBytes(), StandardOpenOption.APPEND);
// 		}
// 	}

// 	static public int filesGenerated_ = 1;
// 	static public String fileName_ = "";
// 	static public int backgroundColor_;
// 	private ArrayList<RowOfPixels> rowsOfPixels_;
// 	private int topLeftX;
// 	private int topLeftY;

// 	Image() {
// 		rowsOfPixels_ = new ArrayList<RowOfPixels>();
// 	}

// 	Image(File file) throws Exception {
// 		fileName_ = file.getName().replaceFirst("[.][^.]+$", "");

// 		// initialize the arraylist
// 		rowsOfPixels_ = new ArrayList<RowOfPixels>();

// 		// load our file into a buffered image
// 		BufferedImage imageFromFile = ImageIO.read(file);

// 		// set the background color
// 		backgroundColor_ = imageFromFile.getRGB(0, 0);

// 		// for every row
// 		for (int currentY = 0; currentY < imageFromFile.getHeight(); currentY++) {

// 			// track if all the pixels are the same
// 			boolean allPixelsAreBackgroundColor = true;

// 			// make an array list to hold all of the pixels
// 			ArrayList<Integer> currentRowsPixels = new ArrayList<Integer>();

// 			// for every column
// 			for (int currentX = 0; currentX < imageFromFile.getWidth(); currentX++) {

// 				// figure out what the current pixel color is
// 				int pixel = imageFromFile.getRGB(currentX, currentY);

// 				// check if this pixel is different than the first pixel in the
// 				// row
// 				if (pixel != backgroundColor_) {
// 					allPixelsAreBackgroundColor = false;
// 				}

// 				// add the pixels to the list of pixels
// 				currentRowsPixels.add(pixel);
// 			}

// 			// after scanning the entire row
// 			// make the RowOfPixels object that holds them and tells us if they
// 			// were all the same
// 			RowOfPixels currentRow = new RowOfPixels(currentRowsPixels, allPixelsAreBackgroundColor);

// 			// add the row to the rows of pixels
// 			rowsOfPixels_.add(currentRow);
// 		}
// 	}

// 	public void addRowOfPixels(RowOfPixels rowToAdd) {
// 		rowsOfPixels_.add(rowToAdd);
// 	}

// 	public ArrayList<RowOfPixels> getRowsOfPixels() {
// 		return rowsOfPixels_;
// 	}

// 	public ArrayList<Image> splitIntoArrayOfImagesWhereRowAllSameColor() throws Exception {
// 		// where i will store the images that this is split into
// 		ArrayList<Image> images = new ArrayList<Image>();

// 		// make an image to store the current pixels we ha eread
// 		Image currentImage = new Image();

// 		// for each row
// 		for (RowOfPixels x : getRowsOfPixels()) {
// 			// if the row is all the same
// 			if (x.allPixelsAreBackgroundColor()) {

// 				// if the image has pixels in it
// 				if (currentImage.getRowsOfPixels().size() > 0) {

// 					// add that image to the list of rows we have made
// 					images.add(currentImage);

// 					// reset the current image
// 					currentImage = new Image();
// 				}
// 			} else {
// 				// add the current row to the current image
// 				currentImage.addRowOfPixels(x);
// 			}
// 		}

// 		// get that last image
// 		// if the image has pixels in it
// 		if (currentImage.getRowsOfPixels().size() > 0) {

// 			// add that image to the list of rows we have made
// 			images.add(currentImage);

// 			// reset the current image
// 			currentImage = new Image();
// 		}

// 		// return the rows
// 		return images;
// 	}

// 	public ArrayList<Image> splitIntoArrayOfImagesWhereColumnAllSameColor() throws Exception {

// 		ArrayList<Integer> splitPoints = new ArrayList<Integer>();

// 		int width = rowsOfPixels_.get(0).size();

// 		// for every column
// 		for (int i = 0; i < width; i++) {

// 			// so far all pixels we have checked are the same
// 			boolean allInColumnAreSame = true;

// 			// for every row
// 			for (RowOfPixels row : rowsOfPixels_) {
// 				// check if the pixel in that column matches
// 				if (row.get(i) != backgroundColor_) {
// 					allInColumnAreSame = false;
// 				}
// 			}

// 			// after you have checked every rows nth column
// 			// if all pixels in that column are the same
// 			if (allInColumnAreSame) {
// 				// mark that as a column we will split on
// 				splitPoints.add(i);
// 			}
// 		}

// 		// this is where we will store all the images post split
// 		ArrayList<Image> images = new ArrayList<Image>();
// 		if (splitPoints.size() > 0) {
// 			// for each split point
// 			for (int i = 0; i < splitPoints.size() - 1; i++) {
// 				// make an image
// 				Image currentImage = new Image();

// 				// for each row
// 				for (RowOfPixels row : rowsOfPixels_) {

// 					// add a row that is the current start of the sprite to the
// 					// current split location wide
// 					currentImage
// 							.addRowOfPixels(row.subList(splitPoints.get(i), splitPoints.get(i + 1), backgroundColor_));
// 				}

// 				// add the image to the list of images
// 				if (currentImage.getRowsOfPixels().size() > 1 && currentImage.getRowsOfPixels().get(0).size() > 1) {
// 					images.add(currentImage);
// 				}
// 			}
// 		} else {
// 			images.add(this);
// 		}

// 		return images;
// 	}

// 	public void writeWidthHeightXML(String fileName, String nameOfRelativeGeneratedFile) throws Exception{

// 		String toAppend = "\t<sprite>\n"
// 		+ "\t\t<name>" + nameOfRelativeGeneratedFile + "</name>\n"
// 		+ "\t\t<direction>SOMEDIRECTION</direction>\n"
// 		+ "\t\t<x>" + "X" + "</x>\n"
// 		+ "\t\t<y>" + "Y" + "</y>\n"
// 		+ "\t\t<w>" + String.valueOf(rowsOfPixels_.get(0).size()) + "</w>\n"
// 		+ "\t\t<h>" + String.valueOf(rowsOfPixels_.size()) + "</h>\n"
// 		+ "\t</sprite>\n";

// 		Files.write(Paths.get(fileName), toAppend.getBytes(), StandardOpenOption.APPEND);
// 	}

// 	public void writeImageToFile(boolean needToCreateXML) throws Exception {
// 		int height = rowsOfPixels_.size();
// 		int width = rowsOfPixels_.get(0).size();

// 		//if we need to create an xml file
// 		if(needToCreateXML){

// 			File xmlFile = new File("./" + fileName_ + "/" + fileName_ + ".xml");

// 			if(xmlFile.exists()){
// 				//delete the old one
// 				xmlFile.delete();
// 			} 
			
// 			//create the file
// 			xmlFile.createNewFile();

// 			Files.write(Paths.get("./" + fileName_ + "/" + fileName_ + ".xml"), "<spritesheet>\n".getBytes(), StandardOpenOption.APPEND);
// 		}

// 		writeWidthHeightXML("./" + fileName_ + "/" + fileName_ + ".xml", fileName_ + "-" + filesGenerated_);

// 		if (height > 1 && width > 1) {
// 			// make an image thats a the height and width of the sprite we want
// 			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

// 			// create graphics for it
// 			Graphics2D ig2 = bi.createGraphics();

// 			// for each pixel we are grabbing
// 			for (int y = 0; y < height; y++) {
// 				for (int x = 0; x < width; x++) {

// 					Color currentColor = new Color(rowsOfPixels_.get(y).get(x));

// 					// set the color
// 					ig2.setColor(currentColor);

// 					// draw the point we read
// 					ig2.fillRect(x, y, 1, 1);
// 				}
// 			}

// 			// make the directory to store them all in
// 			File dir = new File("./" + fileName_);

// 			// attempt to create the directory here
// 			dir.mkdir();

// 			// write the image file
// 			ImageIO.write(bi, "PNG", new File("./" + fileName_ + "/" + fileName_ + "-" + filesGenerated_ + ".png"));

// 			System.out.println("file " + Image.filesGenerated_);

// 			// increment the number of files we have generated
// 			filesGenerated_++;
// 		}
// 	}

// 	public void cropSides() {

// 		// top
// 		// while there are rows and all pixels are the same in the first row
// 		while (rowsOfPixels_.size() > 0 && rowsOfPixels_.get(0).allPixelsAreBackgroundColor()) {
// 			rowsOfPixels_.remove(0);
// 		}

// 		// bottom
// 		// while there are rows and all pixels are the same in the first row
// 		while (rowsOfPixels_.size() > 0 && rowsOfPixels_.get(rowsOfPixels_.size() - 1).allPixelsAreBackgroundColor()) {
// 			rowsOfPixels_.remove(rowsOfPixels_.size() - 1);
// 		}

// 		// left
// 		// if there are rows
// 		// and the first row has columns
// 		if (rowsOfPixels_.size() > 0 && rowsOfPixels_.get(0).size() > 0) {
// 			boolean allInLeftColumnAreSame = true;

// 			do {
// 				Integer firstPixelInColumn = rowsOfPixels_.get(0).get(0);
// 				for (RowOfPixels x : rowsOfPixels_) {
// 					if (x.get(0).intValue() != firstPixelInColumn.intValue()) {
// 						allInLeftColumnAreSame = false;
// 					}
// 				}

// 				if (allInLeftColumnAreSame) {
// 					for (RowOfPixels x : rowsOfPixels_) {
// 						x.remove(0);
// 					}
// 				}
// 			} while (allInLeftColumnAreSame);
// 		}

// 		// right
// 		// if there are rows
// 		// and the first row has columns
// 		if (rowsOfPixels_.size() > 0 && rowsOfPixels_.get(0).size() > 0) {
// 			boolean allInRightColumnAreSame = true;

// 			do {
// 				int lastColumnIndex = rowsOfPixels_.get(0).size() - 1;

// 				Integer firstPixelInColumn = rowsOfPixels_.get(0).get(lastColumnIndex);
// 				for (RowOfPixels x : rowsOfPixels_) {
// 					if (x.get(lastColumnIndex).intValue() != firstPixelInColumn.intValue()) {
// 						allInRightColumnAreSame = false;
// 					}
// 				}

// 				if (allInRightColumnAreSame) {
// 					for (RowOfPixels x : rowsOfPixels_) {
// 						x.remove(lastColumnIndex);
// 					}
// 				}
// 			} while (allInRightColumnAreSame);
// 		}
// 	}
// }