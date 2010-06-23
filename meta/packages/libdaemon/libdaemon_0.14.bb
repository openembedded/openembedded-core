DESCRIPTION = "libdaemon is a lightweight C library which eases the writing of UNIX daemons."
SECTION = "libs"
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://0pointer.de/lennart/projects/libdaemon/"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://libdaemon/daemon.h;startline=9;endline=21;md5=94c709a83d8251377c322322176d4ffe"
PR = "r0"

SRC_URI = "http://0pointer.de/lennart/projects/libdaemon/libdaemon-${PV}.tar.gz"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-lynx"
