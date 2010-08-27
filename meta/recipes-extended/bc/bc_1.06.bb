DESCRIPTION = "An arbitrary precision calculator language."
HOMEPAGE = "http://www.gnu.org/software/bc/bc.html"
BUGTRACKER = ""

LICENSE = "GPLv2+ & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LIB;md5=d8045f3b8f929c1cb29a1e3fd737b499 \
                    file://bc/bcdefs.h;endline=31;md5=46dffdaf10a99728dd8ce358e45d46d8 \
                    file://dc/dc.h;endline=25;md5=2f9c558cdd80e31b4d904e48c2374328 \
                    file://lib/number.c;endline=31;md5=99434a0898abca7784acfd36b8191199"

SECTION = "base"
DEPENDS = "flex"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/bc/bc-${PV}.tar.gz"

inherit autotools
