include boost-${PV}.inc
include boost.inc

SRC_URI += "\
    file://arm-intrinsics.patch \
    file://consider-hardfp.patch \
    file://boost-CVE-2012-2677.patch \
"
