DESCRIPTION = "libdaemon is a lightweight C library which eases the writing of UNIX daemons."
SECTION = "libs"
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://0pointer.de/lennart/projects/libdaemon/"
LICENSE = "LGPL"
PR = "r1"

SRC_URI = "http://0pointer.de/lennart/projects/libdaemon/libdaemon-${PV}.tar.gz"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-lynx --disable-doxygen"
