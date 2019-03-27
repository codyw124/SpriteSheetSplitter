import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

class Image {
	static int filesGenerated_ = 1;
	static String fileName_ = "";

	ArrayList<RowOfPixels> rowsOfPixels_;

	void addRowOfPixels(RowOfPixels rowToAdd){
		rowsOfPixels_.add(rowToAdd);
	}

	ArrayList<RowOfPixels> getRowsOfPixels(){
		return rowsOfPixels_;
	}

	Image(){
		rowsOfPixels_ = new ArrayList<RowOfPixels>();
	}

	Image(File file) throws Exception {
		fileName_ = file.getName().replaceFirst("[.][^.]+$", "");

		//initialize the arraylist
		rowsOfPixels_ = new ArrayList<RowOfPixels>();

		// load our file into a buffered image
		BufferedImage imageFromFile = ImageIO.read(file);

		// for every row
		for (int currentY = 0; currentY < imageFromFile.getHeight(); currentY++) {
				
			//figure out what the first pixel color is
			int firstPixelInRow = imageFromFile.getRGB(0,currentY);

			//track if all the pixels are the same
			boolean allPixelsAreSame = true;

			//make an array list to hold all  of the pixels
			ArrayList<Integer> currentRowsPixels = new ArrayList<Integer>();

			//for every column
			for (int currentX = 0; currentX < imageFromFile.getWidth(); currentX++) {

				//figure out what the current pixel color is
				int pixel = imageFromFile.getRGB(currentX,currentY);

				//check if this pixel is different than the first pixel in the row
				if(pixel != firstPixelInRow){
					allPixelsAreSame = false;
				}

				//add the pixels to the list of pixels
				currentRowsPixels.add(pixel);
			}

			//after scanning the entire row
			//make the RowOfPixels object that holds them and tells us if they were all the same
			RowOfPixels currentRow = new RowOfPixels(currentRowsPixels, allPixelsAreSame);

			//add the row to the rows of pixels
			rowsOfPixels_.add(currentRow);
		}
	}

	public ArrayList<Image> splitIntoRows() throws Exception{
		ArrayList<Image> images = new ArrayList<Image>();

		Image currentImage = new Image();
		
		for(RowOfPixels x : getRowsOfPixels()){
			if(x.allPixelsAreTheSame()){

				if(currentImage.getRowsOfPixels().size() > 0){
					images.add(currentImage);

					currentImage = new Image();
				}
			}
			else{
				currentImage.addRowOfPixels(x);
			}
		}

		return images;
	}

	public ArrayList<Image> splitIntoColumns()throws Exception{
		
		ArrayList<Integer> splitPoints = new ArrayList<Integer>();
		
		int width = rowsOfPixels_.get(0).size();
		
		//for every column
		for(int i = 0; i < width; i++){
			
			//so far all pixels we have checked are the same
			boolean allInColumnAreSame = true;
		
			//figure out and store what the first pixel is
			int firstPixelInColumn	= rowsOfPixels_.get(0).get(i);
			
			//for every row
			for(RowOfPixels row : rowsOfPixels_){
				//check if the pixel in that column matches
				if(row.get(i) != firstPixelInColumn){
					allInColumnAreSame = false;
				}
			}
			
			//after you have checked every rows nth column
			//if all pixels in that column are the same
			if(allInColumnAreSame){
				//mark that as a column we will split on
				splitPoints.add(i);
			}
		}
		
		//this is where we will store all the images post split
		ArrayList<Image> images = new ArrayList<Image>();
		//for each split point
		for(int i = 0; i < splitPoints.size() - 1; i++){
			//make an image
			Image currentImage = new Image();
			
			//for each row
			for(RowOfPixels row : rowsOfPixels_){
				//add a row that is the current start of the sprite to the current split location wide
				currentImage.addRowOfPixels(row.subList(splitPoints.get(i), splitPoints.get(i+1)));
			}
			
			//add the image to the list of images
			images.add(currentImage);
		}

		return images;
	}

	public void writeImageToFile() throws Exception {
		int height = rowsOfPixels_.size();
		int width = rowsOfPixels_.get(0).size();
		
		if(height > 1 && width > 1){
			// make an image thats a the height and width of the sprite we want
			BufferedImage bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);

			// create graphics for it
			Graphics2D ig2 = bi.createGraphics();

			// for each pixel we are grabbing
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					Color currentColor = new Color(rowsOfPixels_.get(y).get(x));

					// set the color
					ig2.setColor(currentColor);

					// draw the point we read
					ig2.fillRect(x, y, 1, 1);
				}
			}

			//make the directory to store them all in
			File dir = new File("./" + fileName_);

			// attempt to create the directory here
			dir.mkdir();

			//write the image file
			ImageIO.write(bi, "PNG", new File("./" + fileName_ + "/" + fileName_ + "-" + filesGenerated_ + ".png"));

			//increment the number of files we have generated
			filesGenerated_++;
		}
	}
	
	public void cropSides(){
		//top
		while(rowsOfPixels_.get(0).allPixelsAreTheSame()){
			rowsOfPixels_.remove(0);
		}
		
		//bottom
		
		while(rowsOfPixels_.get(rowsOfPixels_.size() - 1).allPixelsAreTheSame()){
			rowsOfPixels_.remove(rowsOfPixels_.size() - 1);
		}
		
		//left
		boolean allInLeftColumnAreSame = true;
		
		do{
			Integer firstPixelInColumn = rowsOfPixels_.get(0).get(0);
			for(RowOfPixels x : rowsOfPixels_){
				if(x.get(0) != firstPixelInColumn){
					allInLeftColumnAreSame = false;
				}
			}
			
			if(allInLeftColumnAreSame){
				for(RowOfPixels x : rowsOfPixels_){
					x.remove(0);
				}
			}
		}while(allInLeftColumnAreSame);
		
		//right
		boolean allInRightColumnAreSame = true;
		
		do{
			int lastColumnIndex = rowsOfPixels_.get(0).size() - 1;
			
			Integer firstPixelInColumn = rowsOfPixels_.get(0).get(lastColumnIndex);
			for(RowOfPixels x : rowsOfPixels_){
				if(x.get(lastColumnIndex) != firstPixelInColumn){
					allInRightColumnAreSame = false;
				}
			}
			
			if(allInRightColumnAreSame){
				for(RowOfPixels x : rowsOfPixels_){
					x.remove(lastColumnIndex);
				}
			}
		}while(allInRightColumnAreSame);
	}

	public static void main (String args[]) throws Exception{
		//make sure they gave us a file
		if (args.length != 1) {
			System.out.println("you didnt pass in a file");
		} else {
			//read the file
			Image originalImage = new Image(new File(args[0]));

			//split it into rows
			ArrayList<Image> rows = originalImage.splitIntoRows();
			
			//this will be what holds all the sprites
			ArrayList<Image> images = new ArrayList<Image>();

			//for each row
			for(Image row : rows){
				//get the sprites in the current row
				ArrayList<Image> spritesInRow = row.splitIntoColumns();
				
				//for each sprite
				for(Image currentSprite : spritesInRow){
					images.add(currentSprite);
				}
			}

			for(Image image : images){
				//image.cropSides();
				image.writeImageToFile();
			}
		}
	}
}