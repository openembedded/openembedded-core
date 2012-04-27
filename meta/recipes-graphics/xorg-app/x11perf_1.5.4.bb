require xorg-app-common.inc

SUMMARY = "X11 server performance test program"

DESCRIPTION = "The x11perf program runs one or more performance tests \
and reports how fast an X server can execute the tests."


DEPENDS += "libxmu libxrender libxft libxext fontconfig"

LIC_FILES_CHKSUM = "file://COPYING;md5=428ca4d67a41fcd4fc3283dce9bbda7e \
                    file://x11perf.h;endline=24;md5=29555066baf406a105ff917ac25b2d01"

PR = "${INC_PR}.0"
PE = "1"

FILES_${PN} += "${libdir}/X11/x11perfcomp/*"

SRC_URI[md5sum] = "5c3c7431a38775caaea6051312a49bc9"
SRC_URI[sha256sum] = "24ee8857a2bf414b360addabf1c27ef797f7f504ee9bc409c151760bfbe53184"
