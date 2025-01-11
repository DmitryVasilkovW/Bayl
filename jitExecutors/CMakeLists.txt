cmake_minimum_required(VERSION 3.30)

project(jitExecutors)

set(CMAKE_CXX_STANDARD 26)

set(SOURCES
    src/main/cpp/generateMultiplication.cpp
)

add_library(asmjit_executor
              src/main/cpp/generateMultiplication.cpp
)

target_link_libraries(asmjit_executor asmjit)

include_directories(/usr/include/asmjit)

if(WIN32)
    include_directories("${JAVA_HOME}/include")
    include_directories("${JAVA_HOME}/include/win32")
elseif(UNIX AND NOT APPLE)
    include_directories(${JAVA_HOME}/include/linux)
elseif(APPLE)
    include_directories(${JAVA_HOME}/include/darwin)
endif()

if(WIN32)
    target_link_libraries(asmjit_executor /path/to/asmjit.lib) # Good Luck =)
elseif(UNIX AND NOT APPLE)
    target_link_libraries(asmjit_executor asmjit)
endif()