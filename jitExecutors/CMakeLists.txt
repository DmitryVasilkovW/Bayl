cmake_minimum_required(VERSION 3.30)

project(jitExecutors)

set(PROJECT_VERSION 1.0.0)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include(FetchContent)

file(STRINGS ${CMAKE_HOME_DIRECTORY}/.env ENV_FILE)
foreach(VAR ${ENV_FILE})
    string(REGEX MATCH "^[^=]*" KEY ${VAR})
    string(REGEX REPLACE "^[^=]*=" "" VALUE ${VAR})
    set(ENV{${KEY}} ${VALUE})
endforeach()

set(JAVA_HOME $ENV{JAVA_HOME})

FetchContent_Declare(
  asmjit
  GIT_REPOSITORY https://github.com/asmjit/asmjit.git
  GIT_TAG master
)

FetchContent_MakeAvailable(asmjit)

file(GLOB_RECURSE SOURCES src/main/cpp/*.cpp)

add_library(jitExecutors SHARED ${SOURCES})

target_link_libraries(jitExecutors PRIVATE asmjit)

if (CMAKE_SYSTEM_NAME STREQUAL "Darwin")
    include_directories(${JAVA_HOME}/include/darwin)
else()
    include_directories(${JAVA_HOME}/include/linux)
endif()

include_directories(${JAVA_HOME}/include)

find_package (JNI REQUIRED)
