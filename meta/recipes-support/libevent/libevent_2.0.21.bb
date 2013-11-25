SUMMARY = "An asynchronous event notification library"
HOMEPAGE = "http://libevent.org/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=50884&atid=461322"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=45c5316ff684bcfe2f9f86d8b1279559"

PR = "r1"

SRC_URI = "http://github.com/downloads/libevent/libevent/${BPN}-${PV}-stable.tar.gz \
           file://obsolete_automake_macros.patch \
           file://disable_tests.patch \
"

SRC_URI[md5sum] = "b2405cc9ebf264aa47ff615d9de527a2"
SRC_URI[sha256sum] = "22a530a8a5ba1cb9c080cba033206b17dacd21437762155c6d30ee6469f574f5"

S = "${WORKDIR}/${BPN}-${PV}-stable"

EXTRA_OECONF = "--disable-openssl"

inherit autotools

# Needed for Debian packaging
LEAD_SONAME = "libevent-2.0.so"
