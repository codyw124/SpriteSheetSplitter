//cody ware
//9/16/19

#include "../headers/Pixel.h"
#include "../headers/CImg.h"
#include <iostream>
#include <vector>
#include <map>
#include <utility>

using namespace cimg_library;

int main() 
{
    CImg<int> src("Goku.png");
    int width = src.width();
    int height = src.height();

    std::vector<Pixel*> potentialImages;

    Pixel* backgroundColor = new Pixel(0,0,src(0,0,0,0),src(0,0,0,1), src(0,0,0,2));

    for (int r = 0; r < height; r++)
    {
        for (int c = 0; c < width; c++)
        {
            Pixel* currentPixel = new Pixel(r,c,src(c,r,0,0),src(c,r,0,1), src(c,r,0,2));

            if(*currentPixel!=*backgroundColor)
            {
                potentialImages.push_back(currentPixel);
            }
            else
            {
                delete currentPixel;
            }
        }
    }

    std::vector<std::vector<Pixel*>*> images;

    int count = 1;

    //scan all pixels and load into an image
    for(Pixel* pixel : potentialImages)
    {
        std::cout<< count << " / " << potentialImages.size() <<std::endl;

        bool pixelGotAddedToExistingImage = false;
        
        for(std::vector<Pixel*>* image : images)
        {
            for(Pixel* pixelInCurrentImage : *image)
            {
                if(pixelInCurrentImage->isTouching(*pixel))
                {
                    image->push_back(pixel);
                    pixelGotAddedToExistingImage = true;
                    break;
                }
            }

            if(pixelGotAddedToExistingImage)
            {
                break;
            }
        }

        if(!pixelGotAddedToExistingImage)
        {
            std::vector<Pixel*>* newImage = new std::vector<Pixel*>();
            newImage->push_back(pixel);
            images.push_back(newImage);
        }

        count++;
    }

    //make sure no images are touching 
    std::cout<<images.size() <<std::endl;

    std::cout<<"<spritesheet>\n";

    for(std::vector<Pixel*>* image : images)
    {
        Pixel* topLeft = (*image)[0];
        Pixel* bottomRight = (*image)[0];

        for(Pixel* pixel : *image)
        {
            if((topLeft->getX() > pixel->getX() && topLeft->getY() > pixel->getY()) ||
            (topLeft->getX() == pixel->getX() && topLeft->getY() > pixel->getY()) ||
            (topLeft->getX() > pixel->getX() && topLeft->getY() == pixel->getY()))
            {
                topLeft = pixel;
            }
            else if()
            {
                bottomRight = pixel;
            }
        }

        std::cout<<"\t<sprite>\n";
        std::cout<<"\t\t<x>" << topLeft->getX() << "</x>\n";

        std::cout<<"\t</sprite>\n";
    }

    //cleanup
    delete backgroundColor;
    backgroundColor = NULL;
    for(Pixel* x : potentialImages)
    {
        delete x;
    }

    potentialImages.clear();
}