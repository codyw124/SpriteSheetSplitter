//cody ware
//9/18/2019

#include "../headers/Image.h"

void Image::trimAllSides(const Pixel &color)
{
//     // top
//     // while there are rows and all pixels are the same in the first row
//     while (h_ > 0 && allInRowAreColor(0,backgroundColor))
//     {
//         h_--;
//         y_++;
//     }

//     // bottom
//     // while there are rows and all pixels are the same in the first row
//     while (h_ > 0 && rowsOfPixels_.get(rowsOfPixels_.size() - 1).allPixelsAreBackgroundColor())
//     {
//         rowsOfPixels_.remove(rowsOfPixels_.size() - 1);
//     }

//     // left
//     // if there are rows
//     // and the first row has columns
//     if (w_ > 0 && rowsOfPixels_.get(0).size() > 0)
//     {
//         boolean allInLeftColumnAreSame = true;

//         do
//         {
//             Integer firstPixelInColumn = rowsOfPixels_.get(0).get(0);
//             for (RowOfPixels x : rowsOfPixels_)
//             {
//                 if (x.get(0).intValue() != firstPixelInColumn.intValue())
//                 {
//                     allInLeftColumnAreSame = false;
//                 }
//             }

//             if (allInLeftColumnAreSame)
//             {
//                 for (RowOfPixels x : rowsOfPixels_)
//                 {
//                     x.remove(0);
//                 }
//             }
//         } while (allInLeftColumnAreSame);
//     }

//     // right
//     // if there are rows
//     // and the first row has columns
//     if (w_ > 0 && rowsOfPixels_.get(0).size() > 0)
//     {
//         boolean allInRightColumnAreSame = true;

//         do
//         {
//             int lastColumnIndex = rowsOfPixels_.get(0).size() - 1;

//             Integer firstPixelInColumn = rowsOfPixels_.get(0).get(lastColumnIndex);
//             for (RowOfPixels x : rowsOfPixels_)
//             {
//                 if (x.get(lastColumnIndex).intValue() != firstPixelInColumn.intValue())
//                 {
//                     allInRightColumnAreSame = false;
//                 }
//             }

//             if (allInRightColumnAreSame)
//             {
//                 for (RowOfPixels x : rowsOfPixels_)
//                 {
//                     x.remove(lastColumnIndex);
//                 }
//             }
//         } while (allInRightColumnAreSame);
//     }
}































Image::Image(CImg<int>* image, const int &x, const int &y, const int &w, const int &h, const std::string& filename)
{
    image_ = image;
    x_ = x;
    y_ = y;
    w_ = w;
    h_ = h;
    filename_ = filename;
}

Image::Image(const std::string& filename)
{
    image_ = new CImg<int>(filename.c_str());
    x_ = 0;
    y_ = 0;
    h_ = image_->height();
    w_ = image_->width();
    int indexOfExtensionStart = filename.size() - 1;
    while (indexOfExtensionStart >= 0 && filename[indexOfExtensionStart] != '.')
    {
        indexOfExtensionStart--;
    }
    filename_ = filename.substr(0, indexOfExtensionStart);
}

Image::~Image()
{
    delete image_;
    image_ = NULL;
}

Pixel Image::operator()(const int& x,const int& y,const int& z) const
{
    Pixel ret((*image_)(x,y,z,0), (*image_)(x,y,z,1), (*image_)(x,y,z,2));

    return ret;
}

std::vector<Image*> Image::split(const Pixel& color) const
{
    std::vector<Image*> ret;

    std::vector<Image*> splitRows = splitIntoRowsByColor(color);

    for(Image* rowImage : splitRows)
    {
        for(Image* columnImage : rowImage->splitIntoColumnsByColor(color))
        {
            columnImage->trimAllSides(color);
            ret.push_back(columnImage);
        }
        delete rowImage;
    }

    return ret;
}

void Image::save() const
{
    image_->get_crop(x_,y_,x_+w_-1, y_+h_-1).save_png(filename_.c_str());
}

bool Image::allInRowAreColor(const int& rowIndex, const Pixel &color) const
{
    bool ret = true;
    for (int c = 0; c < w_ && ret; c++)
    {
        Pixel currentPixel = (*this)(c,rowIndex,0);

        if (currentPixel != color)
        {
            ret = false;
        }
    }
    return ret;
}

bool Image::allInColumnAreColor(const int& columnIndex, const Pixel &color) const
{
    bool ret = true;
    for (int r = 0; r < h_ && ret; r++)
    {
        Pixel currentPixel = (*this)(columnIndex,r,0);

        if (currentPixel != color)
        {
            ret = false;
        }
    }
    return ret;
}

std::vector<Image*> Image::splitIntoColumnsByColor(const Pixel &color) const
{
    int w = 0;

    int imagesIveSplitSoFar = 0;

    std::vector<Image*> ret;

    for (int x = 0; x < w_; x++)
    {
        if (allInColumnAreColor(x, color))
        {
            if (w_ > 1)
            {
                ret.push_back(new Image(image_, x,y_,w,h_,filename_ + "_" + std::to_string(imagesIveSplitSoFar)));
                imagesIveSplitSoFar++;
            }
            w = 0;
        }

        w++;
    }
    return ret;
}

std::vector<Image*> Image::splitIntoRowsByColor(const Pixel &color) const
{
    int h = 0;

    int imagesIveSplitSoFar = 0;

    std::vector<Image*> ret;

    for (int y = 0; y < h_; y++)
    {
        if (allInRowAreColor(y, color))
        {
            if (h > 1)
            {
                ret.push_back(new Image(image_, x_,y,w_,h, filename_ + '_' + std::to_string(imagesIveSplitSoFar)));
                imagesIveSplitSoFar++;
            }
            h = 0;
        }

        h++;
    }
    return ret;
}

std::string Image::toXML() const
{
    std::string ret = "\t<sprite>";
    ret += "\t\t<name>" + filename_ + "</name>";
    ret += "\t\t<x>" + std::to_string(x_) + "</x>\n";
    ret += "\t\t<y>" + std::to_string(y_) + "</y>\n";
    ret += "\t\t<w>" + std::to_string(w_) + "</w>\n";
    ret += "\t\t<h>" + std::to_string(h_) + "</h>\n";
    ret += "\t</sprite>";

    return ret;
}

std::ostream &operator<<(std::ostream &os, const Image &i)
{
    os<< i.toXML();
    return os;
}
