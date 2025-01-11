cmake_minimum_required(VERSION 3.10)

# Устанавливаем имя проекта
project(jitExecutors)

# Устанавливаем версию проекта
set(PROJECT_VERSION 1.0.0)

# Указываем флаги компилятора (если необходимо)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Определяем, что будет собираться (например, статическая и динамическая библиотеки)
option(BUILD_SHARED_LIBS "Build shared libraries" ON)

# Указываем путь для установки (если нужно)
set(CMAKE_INSTALL_PREFIX ${CMAKE_BINARY_DIR}/install)

# Добавляем исходные файлы проекта
set(SOURCES
    src/main/cpp/main.cpp
)

# Создаем библиотеку (если нужно)
#add_library(mylibrary STATIC ${SOURCES})

# Устанавливаем библиотеки
#install(TARGETS mylibrary DESTINATION lib)

# Устанавливаем заголовочные файлы
#install(DIRECTORY include/ DESTINATION include)

# Добавляем исполнимые файлы (если необходимо)
add_executable(jitExecutors src/main/cpp/main.cpp)

# Связываем исполнимый файл с библиотекой
#target_link_libraries(myapp PRIVATE mylibrary)

# Устанавливаем исполнимые файлы
#install(TARGETS myapp DESTINATION bin)

# Указываем дополнительные параметры CMake, если нужно
# Например, добавление путей для поиска заголовочных файлов
#include_directories(${PROJECT_SOURCE_DIR}/include)
