SUMMARY = "An asynchronous event notification library"
DESCRIPTION = "An asynchronous event notification library"
HOMEPAGE = "http://libevent.org/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=50884&atid=461322"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=50aad300de703db62baae12146832b45"

PR = "r1"

SRC_URI = "http://github.com/downloads/libevent/libevent/${BPN}-${PV}-stable.tar.gz \
           file://libevent-2.0.16_fix_for_x32.patch"

SRC_URI[md5sum] = "899efcffccdb3d5111419df76e7dc8df"
SRC_URI[sha256sum] = "a578c7bcaf3bab1cc7924bd4d219f2ea621ab8c51dfc4f886e234b6ef4d38295"

S = "${WORKDIR}/${BPN}-${PV}-stable"

inherit autotools

# Needed for Debian packaging
LEAD_SONAME = "libevent-2.0.so"
