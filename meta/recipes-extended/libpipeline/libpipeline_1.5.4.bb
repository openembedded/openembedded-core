SUMMARY = "pipeline manipulation library"
DESCRIPTION = "This is a C library for setting up and running pipelines of processes, \
without needing to involve shell command-line parsing which is often \
error-prone and insecure."
HOMEPAGE = "http://libpipeline.nongnu.org/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "${SAVANNAH_GNU_MIRROR}/libpipeline/libpipeline-${PV}.tar.gz"
SRC_URI[sha256sum] = "db785bddba0a37ef14b4ef82ae2d18b8824e6983dfb9910319385e28df3f1a9c"

inherit pkgconfig autotools

acpaths = "-I ${S}/gl/m4 -I ${S}/m4"
