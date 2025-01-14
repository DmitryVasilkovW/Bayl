#ifndef UTILS_H
#define UTILS_H

#include <cmath>
#include <boost/multiprecision/cpp_dec_float.hpp>

using namespace boost::multiprecision;

inline double roundToPrecision(double value, int precision) {
    cpp_dec_float_50 decimalValue(value);
    cpp_dec_float_50 multiplier = pow(cpp_dec_float_50(10), precision);
    cpp_dec_float_50 roundedValue = round(decimalValue * multiplier) / multiplier;
    return roundedValue.convert_to<double>();
}

#endif // UTILS_H
