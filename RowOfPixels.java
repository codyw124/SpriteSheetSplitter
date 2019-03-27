import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

class RowOfPixels{
    ArrayList<Integer> pixels_;

    boolean allPixelsAreTheSame_;

    RowOfPixels(ArrayList<Integer> pixels, boolean allPixelsAreTheSame){
        pixels_ = pixels;
        allPixelsAreTheSame_ = allPixelsAreTheSame;
    }
	
	public Integer remove(int index){
		return pixels_.remove(index);
	}
	
	RowOfPixels subList(int from, int to){
		
		ArrayList<Integer> newPixels =  new ArrayList<Integer>(pixels_.subList(from,to));
		
		boolean newAllPixelsAreTheSame = true;
		
		Integer firstPixel = newPixels.get(0);
		
		for(Integer currentPixel: newPixels){
			if(currentPixel != firstPixel){
				newAllPixelsAreTheSame = false;
			}
		}
		
		return new RowOfPixels(newPixels, newAllPixelsAreTheSame);
	}

    Integer get(int i){
        return pixels_.get(i);
    }

    int size(){
        return pixels_.size();
    }

    ArrayList<Integer> getRowsOfPixels(){
        return pixels_;
    }

    boolean allPixelsAreTheSame(){
        return allPixelsAreTheSame_;
    }
}