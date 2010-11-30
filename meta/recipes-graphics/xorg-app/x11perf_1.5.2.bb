require xorg-app-common.inc

DESCRIPTION = "X11 server performance test program"
DEPENDS += "libxmu libxrender libxft libxext fontconfig"

LIC_FILES_CHKSUM = "file://COPYING;md5=428ca4d67a41fcd4fc3283dce9bbda7e \
                    file://x11perf.h;endline=24;md5=29555066baf406a105ff917ac25b2d01"

PR = "r0"
PE = "1"
