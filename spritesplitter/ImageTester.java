package spritesplitter;

import java.util.ArrayList;

class ImageTester{
    public static void main(String args[]) throws Exception {
        ArrayList<ArrayList<Integer>> loadedImage = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < 5; i++) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            row.add(0);
            if(i == 1 || i == 2){
                row.add(1);
                row.add(1);
            }
            else{
                row.add(0);
                row.add(0);
            }
            row.add(0);
            row.add(0);
            loadedImage.add(row);
        }


        for(ArrayList<Integer> x : loadedImage){
            for(Integer y : x){
                System.out.print(y);
            }
            System.out.println();
        }

        Image test = new Image("",0,0,0,5,5,loadedImage);

        ArrayList<Image> splitIntoImages = test.getImages();

        System.out.println(splitIntoImages.get(0).x);
        System.out.println(splitIntoImages.get(0).y);
        System.out.println(splitIntoImages.get(0).w);
        System.out.println(splitIntoImages.get(0).h);
	}
}