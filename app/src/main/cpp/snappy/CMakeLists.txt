# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


get_filename_component(DSNAPPY_LIBRARY_SRC "${PROJECT_SOURCE_DIR}../../../../../third_party/snappy" ABSOLUTE)
message("DSNAPPY_LIBRARY_SRC: " ${DSNAPPY_LIBRARY_SRC})

set(Snappy_Library_SRCS
        ${DSNAPPY_LIBRARY_SRC}/snappy.cc
        ${DSNAPPY_LIBRARY_SRC}/snappy.h
        ${DSNAPPY_LIBRARY_SRC}/snappy-c.cc
        ${DSNAPPY_LIBRARY_SRC}/snappy-c.h
        ${DSNAPPY_LIBRARY_SRC}/snappy-internal.h
        ${DSNAPPY_LIBRARY_SRC}/snappy-sinksource.cc
        ${DSNAPPY_LIBRARY_SRC}/snappy-sinksource.h
        ${DSNAPPY_LIBRARY_SRC}/snappy-stubs-internal.cc
        ${DSNAPPY_LIBRARY_SRC}/snappy-stubs-internal.h
        ${DSNAPPY_LIBRARY_SRC}/snappy-stubs-public.h
        ${DSNAPPY_LIBRARY_SRC}/snappy_compress_fuzzer.cc
        ${DSNAPPY_LIBRARY_SRC}/snappy_uncompress_fuzzer.cc)

add_library( # Sets the name of the library.
             snappy
             ${Snappy_Library_SRCS}
)

