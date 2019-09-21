//cody ware
//9/18/2019

#pragma once

#include "CImg.h"
#include "Pixel.h"
#include <string>

using namespace cimg_library;

class Image
{
private:
    CImg<int>* image_;
    int x_;
    int y_;
    int w_;
    int h_;

public:
    Image(CImg<int>* image, const int& x, const int& y, const int& w, const int& h);
    CImg<int>* getImage() const;
    int operator()(const int& x,const int& y,const int& z,const int& c);
    int getH() const;
    int getW() const;
    int getX() const;
    int getY() const;
    void trimAllSides(const Pixel& backgroundColor);
    bool allInRowAreColor(int rowIndex, const Pixel &color);
    bool allInColumnAreColor(const int& columnIndex, const Pixel &color);
};

std::ostream& operator<<(std::ostream& os, const Image & i);