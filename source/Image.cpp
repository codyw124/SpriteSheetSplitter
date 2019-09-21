//cody ware
//9/18/2019

#include "../headers/Image.h"

Image::Image(CImg<int>* image, const int &x, const int &y, const int &w, const int &h)
{
    image_ = image;
    x_ = x;
    y_ = y;
    w_ = w;
    h_ = h;
}

CImg<int>* Image::getImage() const
{
    return image_;
}

int Image::getH() const
{
    return h_;
}

int Image::getW() const
{
    return w_;
}

int Image::getX() const
{
    return x_;
}

int Image::getY() const
{
    return y_;
}

void Image::trimAllSides(const Pixel &backgroundColor)
{
    // top
    // while there are rows and all pixels are the same in the first row
    while (h_ > 0 && allInRowAreColor(0,backgroundColor))
    {
        h_--;
        y_++;
    }

    // bottom
    // while there are rows and all pixels are the same in the first row
    while (h_ > 0 && rowsOfPixels_.get(rowsOfPixels_.size() - 1).allPixelsAreBackgroundColor())
    {
        rowsOfPixels_.remove(rowsOfPixels_.size() - 1);
    }

    // left
    // if there are rows
    // and the first row has columns
    if (w_ > 0 && rowsOfPixels_.get(0).size() > 0)
    {
        boolean allInLeftColumnAreSame = true;

        do
        {
            Integer firstPixelInColumn = rowsOfPixels_.get(0).get(0);
            for (RowOfPixels x : rowsOfPixels_)
            {
                if (x.get(0).intValue() != firstPixelInColumn.intValue())
                {
                    allInLeftColumnAreSame = false;
                }
            }

            if (allInLeftColumnAreSame)
            {
                for (RowOfPixels x : rowsOfPixels_)
                {
                    x.remove(0);
                }
            }
        } while (allInLeftColumnAreSame);
    }

    // right
    // if there are rows
    // and the first row has columns
    if (w_ > 0 && rowsOfPixels_.get(0).size() > 0)
    {
        boolean allInRightColumnAreSame = true;

        do
        {
            int lastColumnIndex = rowsOfPixels_.get(0).size() - 1;

            Integer firstPixelInColumn = rowsOfPixels_.get(0).get(lastColumnIndex);
            for (RowOfPixels x : rowsOfPixels_)
            {
                if (x.get(lastColumnIndex).intValue() != firstPixelInColumn.intValue())
                {
                    allInRightColumnAreSame = false;
                }
            }

            if (allInRightColumnAreSame)
            {
                for (RowOfPixels x : rowsOfPixels_)
                {
                    x.remove(lastColumnIndex);
                }
            }
        } while (allInRightColumnAreSame);
    }
}

int Image::operator()(const int& x,const int& y,const int& z,const int& c)
{
    return image_(x,y,z,c);
}

bool Image::allInRowAreColor(const int& rowIndex, const Pixel &color)
{
    bool ret = true;
    for (int c = 0; c < w_ && ret; c++)
    {
        Pixel currentPixel(image_(c, 0, 0, 0), image_(c, 0, 0, 1), image_(c, 0, 0, 2));

        if (currentPixel != color)
        {
            ret = false;
        }
    }
    return ret;
}

bool Image::allInColumnAreColor(const int& columnIndex, const Pixel &color)
{
    bool ret = true;
    for (int r = 0; r < h_ && ret; r++)
    {
        Pixel currentPixel(image_(0, r, 0, 0), image_(0, r, 0, 1), image_(0, r, 0, 2));

        if (currentPixel != color)
        {
            ret = false;
        }
    }
    return ret;
}

std::ostream &operator<<(std::ostream &os, const Image &i)
{
    os << "\t\t<x>" + std::to_string(i.getX()) + "</x>\n";
    os << "\t\t<y>" + std::to_string(i.getY()) + "</y>\n";
    os << "\t\t<w>" + std::to_string(i.getW()) + "</w>\n";
    os << "\t\t<h>" + std::to_string(i.getH()) + "</h>\n";
    return os;
}