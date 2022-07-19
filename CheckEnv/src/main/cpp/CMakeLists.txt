# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("checkenv")

get_filename_component(DHASH_LIBRARY_SRC ${HASH_LIBRARY_SRC} ABSOLUTE)
message("HASH_LIBRARY_SRC: " ${HASH_LIBRARY_SRC})
message("DHASH_LIBRARY_SRC: " ${DHASH_LIBRARY_SRC})

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        checkenv

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${HASH_LIBRARY_SRC}/crc32.cpp
        ${HASH_LIBRARY_SRC}/digest.cpp
        ${HASH_LIBRARY_SRC}/keccak.cpp
        ${HASH_LIBRARY_SRC}/md5.cpp
        ${HASH_LIBRARY_SRC}/sha1.cpp
        ${HASH_LIBRARY_SRC}/sha3.cpp
        ${HASH_LIBRARY_SRC}/sha256.cpp
        base64.h
        base64.cc
        hex_codec.h
        hex_codec.cc
        string_conversion.h
        string_conversion.cc
        jni_base64.cpp
        linux_syscall_support.h
        logging.h
        magisk_detector.cpp
        common.h
        common.cpp
        mobile_info.h
        mobile_info.cpp
        jni_string.hpp
        check_env.cpp
        check_root.cpp
        check_debug.cpp
        check_cpu.cpp
        check_multi_open.cpp
        check_sign.cpp
        check_hook.cpp
        check_device_uuid.cpp
        timer/timestamp_provider.h
        timer/time_delta.h
        timer/time_point.h
        timer/time_point.cc
        timer/chrono_timestamp_provider.h
        timer/chrono_timestamp_provider.cc
        )

include_directories(${HASH_LIBRARY_SRC}/)

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
        checkenv

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
