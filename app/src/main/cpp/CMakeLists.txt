# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("environmentcheck")

message("FLATBUFFERS_SRC: " ${FLATBUFFERS_SRC})
message("DOUBLE_CONVERSION_SRC: " ${DOUBLE_CONVERSION_SRC})

# Specifies a path to native header files.
include_directories(${FLATBUFFERS_SRC}/include)
include_directories(${DOUBLE_CONVERSION_SRC}/double-conversion)

add_subdirectory(flatbuffers)
add_subdirectory(snappy)
add_subdirectory(double-conversion)
# add_subdirectory(abseil-cpp)

FILE(GLOB Generated_SRCS generated/*.h)

find_package(curl REQUIRED CONFIG)
find_package(jsoncpp REQUIRED CONFIG)
find_package (oboe REQUIRED CONFIG)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        environmentcheck

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        log/log_level.h
        log/log_settings_state.cc
        log/log_settings.h
        log/log_settings.cc
        log/logging.h
        log/logging.cc
        macros.h
        log/log_utils.h
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
        string_conversion.h
        string_conversion.cc
        wakeable.h
        task_queue_id.h
        task_source_grade.h
        delayed_task.h
        delayed_task.cc
        task_source.h
        task_source.cc
        thread_local.h
        thread_local.cc
        make_copyable.h
        message_loop_task_queues.h
        message_loop_task_queues.cc
        android/scoped_java_ref.h
        android/scoped_java_ref.cc
        android/jni_util.h
        android/jni_util.cc
        android/jni_weak_ref.h
        android/jni_weak_ref.cc
        unique_object.h
        unique_fd.h
        unique_fd.cc
        message_loop_impl.h
        linux/timerfd.h
        linux/timerfd.cc
        android/message_loop_android.h
        android/message_loop_android.cc
        message_loop_impl.cc
        message_loop.h
        message_loop.cc
        task_runner.h
        task_runner.cc
        memory/task_runner_checker.h
        memory/task_runner_checker.cc
        thread.h
        thread.cc
        thread_host.h
        thread_host.cc
        task_runners.h
        task_runners.cc
        memory/ref_counted.h
        memory/ref_counted_internal.h
        memory/ref_ptr.h
        memory/ref_ptr_internal.h
        memory/thread_checker.h
        memory/weak_ptr.h
        memory/weak_ptr_internal.h
        memory/weak_ptr_internal.cc
        memory/atomic.h
        memory/shared_ptr.h
        memory/unique_ptr.h
        internal/type_traits.h
        meta/move.h
        timer/timestamp_provider.h
        timer/time_delta.h
        timer/time_point.h
        timer/time_point.cc
        timer/chrono_timestamp_provider.h
        timer/chrono_timestamp_provider.cc
        synchronization/atomic_object.h
        synchronization/waitable_event.h
        synchronization/waitable_event.cc
        synchronization/sync_switch.h
        synchronization/sync_switch.cc
        synchronization/shared_mutex.h
        synchronization/shared_mutex_std.h
        synchronization/shared_mutex_std.cc
        synchronization/semaphore.h
        synchronization/semaphore.cc
        synchronization/count_down_latch.h
        synchronization/count_down_latch.cc
        DefaultTaskExecutor.h
        DefaultTaskExecutor.cpp
        ArchTaskExecutor.h
        ArchTaskExecutor.cpp
        concurrent_message_loop.h
        concurrent_message_loop.cc
        ascii_trie.h
        ascii_trie.cc
        base32.h
        base32.cc
        hex_codec.h
        hex_codec.cc
        ${Generated_SRCS})

add_library(
        snappy-android
        # Sets the library as a shared library.
        SHARED
        snappy_natvie.cpp
)

add_library(
        double-conversion-android
        # Sets the library as a shared library.
        SHARED
        jni_double_conversion.cpp
)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
find_library(android android)
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
# now build app's shared lib
# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++17 -Wall -Werror")
set(disabled_compile_warnings "-Werror,-Wunused-function")
target_compile_options(environmentcheck PRIVATE -Wall -Werror)

target_link_libraries( # Specifies the target library.
        environmentcheck
        flatbuffers
        # Links the target library to the log library
        # included in the NDK.
        curl::curl
        jsoncpp::jsoncpp
        oboe::oboe
        ${android}
        ${log-lib})

target_link_libraries( # Specifies the target library.
        snappy-android
        snappy
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        double-conversion-android
        double-conversion
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
