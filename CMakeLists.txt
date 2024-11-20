cmake_minimum_required(VERSION 3.16)
project(Bayl)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(LLVM_DIR "/usr/local/opt/llvm/lib/cmake/llvm")

find_package(LLVM REQUIRED CONFIG)

include_directories(${LLVM_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

file(GLOB_RECURSE SRC_FILES src/*.cpp)
file(GLOB_RECURSE HEADER_FILES include/*.h)

add_executable(Bayl ${SRC_FILES} ${HEADER_FILES})

llvm_map_components_to_libnames(LLVM_LIBS core orcjit native)
target_link_libraries(Bayl ${LLVM_LIBS})
