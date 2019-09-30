//cody ware
//9/16/19

#include "../headers/Pixel.h"
#include "../headers/Image.h"
#include <iostream>

using namespace cimg_library;
using namespace std;

int main()
{
    Image src("/home/user/SpriteSheetSplitter/Goku.png");

    Pixel backgroundColor = src(0,0,0);

    vector<Image*> srcSplit = src.split(backgroundColor);

    cout<<"<spritesheet>\n";

    for(Image* img : srcSplit)
    {
        cout<<"\t<sprite>\n";
        img->save();
        cout<<img;
        cout<<"\t</sprite>\n";
        delete img;
    }

    cout<<"</spritesheet>\n";
}
