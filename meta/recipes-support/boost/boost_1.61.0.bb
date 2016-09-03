include boost-${PV}.inc
include boost.inc

SRC_URI += "\
    file://arm-intrinsics.patch \
    file://consider-hardfp.patch \
    file://boost-CVE-2012-2677.patch \
    file://0001-boost-asio-detail-socket_types.hpp-fix-poll.h-includ.patch \
    file://0002-boost-test-execution_monitor.hpp-fix-mips-soft-float.patch \
"
