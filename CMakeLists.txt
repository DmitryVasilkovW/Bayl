cmake_minimum_required(VERSION 3.16)
project(Bayl)

# Указываем стандарт C++
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Подключение LLVM
find_package(LLVM REQUIRED CONFIG)

# Добавляем пути к заголовкам LLVM
include_directories(${LLVM_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

# Указываем файлы проекта
file(GLOB_RECURSE SRC_FILES src/*.cpp)
file(GLOB_RECURSE HEADER_FILES include/*.h)

# Создаем исполняемый файл
add_executable(Bayl ${SRC_FILES} ${HEADER_FILES})

# Линкуем LLVM
target_link_libraries(Bayl LLVM)
