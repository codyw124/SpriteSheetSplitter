//cody ware
//9/16/19

#include "../headers/Pixel.h"

Pixel::Pixel(const int& x, const int& y, const int& r, const int& g, const int& b)
{
    r_ = r; 
    g_ = g;
    b_ = b;
    x_ = x;
    y_ = y;
}

int Pixel::getR() const
{
    return r_;
}

int Pixel::getG() const
{
    return g_;
}

int Pixel::getB() const
{
    return b_;
}

int Pixel::getX() const
{
    return x_;
}

int Pixel::getY() const
{
    return y_;
}

bool Pixel::operator==(const Pixel & p) const
{
    return r_ == p.r_ && g_ == p.g_ && b_ == p.b_;
}

bool Pixel::operator!=(const Pixel & r) const
{
	return !(*this == r);
}

bool Pixel::isTouching(const Pixel& other) const
{
    //they are touching if y is the same and x is 1 away 
    //or x is the same and y is 1 away
    return ( ( (x_ == other.x_ + 1 || x_ == other.x_ - 1 )  && y_ == other.y_ ) || ( (y_ == other.y_ + 1 || y_ == other.y_ - 1 )  && x_ == other.x_) );
}

//output operator 
std::ostream & operator<<(std::ostream & os, const Pixel& p)
{
	os << p.getX() << "," << p.getY() << " = (" << p.getR() << ", " << p.getG() << ", " << p.getB() << ")";
	return os;
}