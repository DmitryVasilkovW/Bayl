cmake_minimum_required(VERSION 3.10)

project(jitExecutors)

include(FetchContent)

file(STRINGS ${CMAKE_HOME_DIRECTORY}/.env ENV_FILE)
foreach(VAR ${ENV_FILE})
    string(REGEX MATCH "^[^=]*" KEY ${VAR})
    string(REGEX REPLACE "^[^=]*=" "" VALUE ${VAR})
    set(ENV{${KEY}} ${VALUE})
endforeach()

set(JAVA_HOME $ENV{JAVA_HOME})

message(STATUS $ENV{EXAMPLE_ENV_VAR})

if (CMAKE_SYSTEM_NAME STREQUAL "Darwin")
    set(JAVA_INCLUDE_PATH_MAC ${JAVA_HOME}/include/darwin)
    include_directories(${JAVA_INCLUDE_PATH_MAC})
endif()

set(JAVA_INCLUDE_PATH ${JAVA_HOME}/include)
set(JAVA_AWT_INCLUDE_PATH ${JAVA_HOME}/include)
include_directories(${JAVA_INCLUDE_PATH} ${JAVA_AWT_INCLUDE_PATH})

find_package (JNI REQUIRED)

FetchContent_Declare(
  asmjit
  GIT_REPOSITORY https://github.com/asmjit/asmjit.git
  GIT_TAG master
)

FetchContent_MakeAvailable(asmjit)

if (CMAKE_SYSTEM_PROCESSOR STREQUAL "arm64")
    set(ARCH "arm64")
    set(ASM_FLAGS "-march=armv8-a")
elseif (CMAKE_SYSTEM_PROCESSOR STREQUAL "x86_64")
    set(ARCH "x86_64")
    set(ASM_FLAGS "-march=x86-64")
else()
    message(FATAL_ERROR "Unsupported architecture on macOS: ${CMAKE_SYSTEM_PROCESSOR}")
endif()

set(PROJECT_VERSION 1.0.0)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

file(GLOB_RECURSE SOURCES src/main/cpp/*.cpp)

add_library(jitExecutors SHARED ${SOURCES})

target_link_libraries(jitExecutors PRIVATE asmjit)

set(CMAKE_INSTALL_PREFIX ${CMAKE_BINARY_DIR}/install)
install(TARGETS jitExecutors DESTINATION bin)
