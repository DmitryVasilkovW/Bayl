cmake_minimum_required(VERSION 3.30)

project(jitExecutors)

set(PROJECT_VERSION 1.0.0)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include(FetchContent)

FetchContent_Declare(
  asmjit
  GIT_REPOSITORY https://github.com/asmjit/asmjit.git
  GIT_TAG master
)

FetchContent_MakeAvailable(asmjit)

file(GLOB_RECURSE SOURCES src/main/cpp/*.cpp)

add_library(jitExecutors SHARED ${SOURCES})

target_link_libraries(jitExecutors PRIVATE asmjit)

if(UNIX AND NOT APPLE)
    include_directories(${JAVA_HOME}/include/linux)
elseif(APPLE)
    include_directories(${JAVA_HOME}/include/darwin)
endif()
