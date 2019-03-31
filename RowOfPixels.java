import java.util.ArrayList;

class RowOfPixels{
    private ArrayList<Integer> pixels_;

    private boolean allPixelsAreTheBackgroundColor_;

    RowOfPixels(ArrayList<Integer> pixels, boolean allPixelsAreTheBackgroundColor){
        pixels_ = pixels;
        allPixelsAreTheBackgroundColor_ = allPixelsAreTheBackgroundColor;
    }

    public void setAllPixelsAreSame(boolean newAllBGColor){
        allPixelsAreTheBackgroundColor_ = newAllBGColor;
    }
	
	public Integer remove(int index){
		return pixels_.remove(index);
	}
	
	public RowOfPixels subList(int from, int to, int backgroundColor){
		
		ArrayList<Integer> newPixels =  new ArrayList<Integer>(pixels_.subList(from,to));
		
		boolean newAllPixelsAreBGColor = true;
		
		for(Integer currentPixel: newPixels){
			if(currentPixel.intValue() != backgroundColor){
				newAllPixelsAreBGColor = false;
				break;
			}
		}
		
		return new RowOfPixels(newPixels, newAllPixelsAreBGColor);
	}

	public Integer get(int i){
        return pixels_.get(i);
    }

	public int size(){
        return pixels_.size();
    }

	public ArrayList<Integer> getRowsOfPixels(){
        return pixels_;
    }

	public boolean allPixelsAreBackgroundColor(){
        return allPixelsAreTheBackgroundColor_;
    }
}