# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("environmentcheck")

message("FLATBUFFERS_SRC: " ${FLATBUFFERS_SRC})

include_directories(${FLATBUFFERS_SRC}/include)

add_subdirectory(flatbuffers)
add_subdirectory(snappy)

FILE(GLOB Generated_SRCS generated/*.h)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        environmentcheck

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        macros.h
        log_utils.h
        log_level.h
        jni_string.hpp
        qtnamespacemacros.h
        qglobal.h
        qjnihelpers_p.h
        qjnihelpers.cpp
        qjnienvironment.h
        qjnienvironment.cpp
        qjniobject.h
        qjniobject.cpp
        NativeObjectReference.cpp
        QtJniNativeViewModel.cpp
        ViewModel.h
        ViewModel.cpp
        MainViewModel.h
        MainViewModel.cpp
        native-lib.cpp
        ${Generated_SRCS})

add_library(
        snappy-android
        # Sets the library as a shared library.
        SHARED
        snappy_natvie.cpp
)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        environmentcheck
        flatbuffers
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        snappy-android
        snappy
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})