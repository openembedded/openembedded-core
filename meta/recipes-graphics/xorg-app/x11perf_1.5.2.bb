require xorg-app-common.inc

SUMMARY = "X11 server performance test program"

DESCRIPTION = "The x11perf program runs one or more performance tests \
and reports how fast an X server can execute the tests."


DEPENDS += "libxmu libxrender libxft libxext fontconfig"

LIC_FILES_CHKSUM = "file://COPYING;md5=428ca4d67a41fcd4fc3283dce9bbda7e \
                    file://x11perf.h;endline=24;md5=29555066baf406a105ff917ac25b2d01"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "3459958a7fdccf02fa43b70fda9cd87f"
SRC_URI[sha256sum] = "a1fd752abd3496568614c2f2209d21452bdd5b8c9a5c14a5705725bd8c298e12"
