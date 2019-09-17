//cody ware
//9/16/19

#pragma once

#include <iostream>

class Pixel
{
public:
    Pixel(const int& x, const int& y, const int& r, const int& g, const int& b);
    int getR() const;
    int getG() const;
    int getB() const;
    int getX() const;
    int getY() const;
    bool operator==(const Pixel & r) const;
    bool operator!=(const Pixel & r) const;
    bool isTouching(const Pixel& other) const;

private:
    int r_;
    int g_;
    int b_;
    int x_;
    int y_;
};

std::ostream& operator<<(std::ostream& os, const Pixel& p);