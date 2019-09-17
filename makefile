COMPILER = g++
COMPILER_FLAGS = -Wall -g -c -std=c++11 -Dcimg_use_png
LINKER_FLAGS = -lpng -lz -lX11 -lpthread

all: objects main

#make the objects directory so we can build everything else
objects:
	mkdir objects

main: objects/main.o objects/Pixel.o
	$(COMPILER) objects/main.o objects/Pixel.o $(LINKER_FLAGS) -o main

objects/main.o: source/main.cpp  headers/Pixel.h headers/CImg.h
	$(COMPILER) $(COMPILER_FLAGS) source/main.cpp -o objects/main.o

objects/Pixel.o: source/Pixel.cpp headers/Pixel.h
	$(COMPILER) $(COMPILER_FLAGS) source/Pixel.cpp -o objects/Pixel.o

clean:
	rm main; rm -r objects;