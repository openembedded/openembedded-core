SUMMARY = "An asynchronous event notification library"
DESCRIPTION = "An asynchronous event notification library"
HOMEPAGE = "http://www.monkey.org/~provos/libevent/"
SECTION = "libs"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://event.c;endline=26;md5=bc20aa63bf60c36c2d8edb77746f6b7c"

PR = "r0"

SRC_URI = "http://www.monkey.org/~provos/${BPN}-${PV}-stable.tar.gz"

SRC_URI[md5sum] = "a00e037e4d3f9e4fe9893e8a2d27918c"
SRC_URI[sha256sum] = "afa61b476a222ba43fc7cca2d24849ab0bbd940124400cb699915d3c60e46301"
S = "${WORKDIR}/${BPN}-${PV}-stable"

inherit autotools

LEAD_SONAME = "libevent-1.4.so"
