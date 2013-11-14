include boost-${PV}.inc
include boost.inc

SRC_URI += "file://arm-intrinsics.patch \
            file://glibc.patch \
            file://boost-1.54.0-thread-link_atomic.patch \
           "
