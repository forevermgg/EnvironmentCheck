cmake_minimum_required(VERSION 3.0)

# include_directories(${DOUBLE_CONVERSION_SRC}/double-conversion)
set(headers
        ${DOUBLE_CONVERSION_SRC}/double-conversion/bignum.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/cached-powers.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/diy-fp.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/double-conversion.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/double-to-string.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/fast-dtoa.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/fixed-dtoa.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/ieee.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/string-to-double.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/strtod.h
        ${DOUBLE_CONVERSION_SRC}/double-conversion/utils.h)

add_library(double-conversion
        ${DOUBLE_CONVERSION_SRC}/double-conversion/bignum.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/bignum-dtoa.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/cached-powers.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/double-to-string.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/fast-dtoa.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/fixed-dtoa.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/string-to-double.cc
        ${DOUBLE_CONVERSION_SRC}/double-conversion/strtod.cc
            ${headers})

