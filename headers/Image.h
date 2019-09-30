//cody ware
//9/18/2019

#pragma once

#include "CImg.h"
#include "Pixel.h"
#include <string>
#include <vector>

using namespace cimg_library;

class Image
{
private:
    CImg<int>* image_;
    int x_;
    int y_;
    int w_;
    int h_;
    std::string filename_;


    Image(CImg<int>* image, const int& x, const int& y, const int& w, const int& h, const std::string& filename);
    void trimAllSides(const Pixel& color);
    bool allInRowAreColor(const int& rowIndex, const Pixel &color) const;
    bool allInColumnAreColor(const int& columnIndex, const Pixel &color) const;
    std::vector<Image*> splitIntoRowsByColor(const Pixel &color) const;
    std::vector<Image*> splitIntoColumnsByColor(const Pixel &color) const;

public:
    Image(const std::string& filename);
    Pixel operator()(const int& x,const int& y,const int& z) const;
    std::string toXML() const;
    std::vector<Image*> split(const Pixel& color) const;
    void save() const;
    ~Image();
};

std::ostream& operator<<(std::ostream& os, const Image & i);