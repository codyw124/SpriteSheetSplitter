//cody ware
//9/16/19

#include "../headers/Pixel.h"

Pixel::Pixel(const int& r, const int& g, const int& b)
{
    r_ = r; 
    g_ = g;
    b_ = b;
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

bool Pixel::operator==(const Pixel & p) const
{
    return r_ == p.r_ && g_ == p.g_ && b_ == p.b_;
}

bool Pixel::operator!=(const Pixel & r) const
{
	return !(*this == r);
}

//output operator 
std::ostream & operator<<(std::ostream & os, const Pixel& p)
{
	os << "(" << p.getR() << ", " << p.getG() << ", " << p.getB() << ")";
	return os;
}