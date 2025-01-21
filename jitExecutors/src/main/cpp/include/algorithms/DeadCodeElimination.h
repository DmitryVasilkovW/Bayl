#pragma once

#include <boost/any.hpp>
#include <vector>
#include <stdexcept>
#include <cmath>
#include <iostream>

class DeadCodeElimination {
public:
    DeadCodeElimination() = default;

    static bool isDeadCode(const std::vector<boost::any>& args) {
        return args.empty() || (args.size() == 2 && getArgAs<double>(args[0]) == 0.0 && getArgAs<double>(args[1]) == 0.0);
    }


private:
    template <typename T>
    static T getArgAs(const boost::any& arg) {
        return boost::any_cast<T>(arg);
    }
};
