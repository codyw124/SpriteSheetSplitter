Image.class:RowOfPixels.class Image.java
	javac Image.java

RowOfPixels.class: RowOfPixels.java
	javac RowOfPixels.java

clean:
	rm *.class
