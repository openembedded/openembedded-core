require xorg-app-common.inc

SUMMARY = "X11 server performance test program"

DESCRIPTION = "The x11perf program runs one or more performance tests \
and reports how fast an X server can execute the tests."


DEPENDS += "libxmu libxrender libxft libxext fontconfig"

LIC_FILES_CHKSUM = "file://COPYING;md5=428ca4d67a41fcd4fc3283dce9bbda7e \
                    file://x11perf.h;endline=24;md5=29555066baf406a105ff917ac25b2d01"

PR = "r1"
PE = "1"

FILES_${PN} += "${libdir}/X11/x11perfcomp/*"

SRC_URI[md5sum] = "c3ac3667a6f5c3cead9847fbf4b5f36e"
SRC_URI[sha256sum] = "394d7355afe7f3b054ce6f30db78794c6305c6593d48b7fb86a9c89d9d9e21bd"
