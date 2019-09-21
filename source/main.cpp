//cody ware
//9/16/19

#include "../headers/Pixel.h"
#include "../headers/CImg.h"
#include "../headers/Image.h"
#include <iostream>
#include <vector>

using namespace cimg_library;
using namespace std;

vector<Image> splitOnBGColorIntoRows(const Image &src, const Pixel &backgroundColor);
vector<Image> splitOnBGColorIntoColumns(const Image &src, const Pixel &backgroundColor);

int main()
{
    CImg<int> src("Goku.png");

    Pixel backgroundColor(src(0, 0, 0, 0), src(0, 0, 0, 1), src(0, 0, 0, 2));

    vector<Image> rows = splitOnBGColorIntoRows(src, backgroundColor);

    vector<Image> generatedImages;

    for (Image row : rows)
    {
        vector<Image> columns = splitOnBGColorIntoColumns(row, backgroundColor);

        for (Image image : columns)
        {
            generatedImages.push_back(image);
        }
    }

    int count = 1;

    cout<<"<spritesheet>\n";

    for(Image img : generatedImages)
    {
        cout<<"\t<sprite>\n";
        string imageName = "Goku_" + to_string(count) + ".png";
        img.trimAllSides(backgroundColor);
        src.getImage().get_crop(img.getX(), img.getY(),img.getX()+img.getW()-1, img.getY()+img.getH()-1).save_png(imageName.c_str());
        count++;
        cout<<img;
        cout<<"\t</sprite>\n";
        break;
    }

    cout<<"</spritesheet>\n";
}

vector<Image> splitOnBGColorIntoColumns(const Image &src, const Pixel &backgroundColor)
{
    int x = 0;
    int y = 0;
    int w = 0;
    int h = src.getH();
    vector<Image> ret;

    for (int i = 0; i < src.getW(); i++)
    {
        if (allInColumnAreBGColor(src.getImage().get_column(i), backgroundColor))
        {
            if (w > 1)
            {
                Image currentImage(src.getImage().get_crop(x, src.getY(), x + w, y + h - 1), x,y,w,h);
                ret.push_back(currentImage);
            }
            x = x + w;
            w = 0;
        }
        
        w++;
        
    }
    return ret;
}

vector<Image> splitOnBGColorIntoRows(const Image &src, const Pixel &backgroundColor)
{
    int x = 0;
    int y = 0;
    int w = src.getW();
    int h = 0;
    vector<Image> ret;

    for (int i = 0; i < src.getH(); i++)
    {
        if (allInRowAreBGColor(src.getImage().get_row(i), backgroundColor))
        {
            if (h > 1)
            {
                Image currentImage(src.getImage().get_crop(x, y, x + w - 1, y + h), x,y,w,h);
                ret.push_back(currentImage);
            }
            y = y + h;
            h = 0;
        }

        h++;
    }
    return ret;
}