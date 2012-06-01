DESCRIPTION = "The DirectFB-examples package contains a set of simple DirectFB \
      applications that can be used to test and demonstrate various DirectFB \
      features"
DEPENDS = "directfb"
SECTION = "libs"
LICENSE = "MIT"
PR = "r0"

SRC_URI = " \
           http://www.directfb.org/downloads/Extras/DirectFB-examples-${PV}.tar.gz \
          "

LIC_FILES_CHKSUM = "file://COPYING;md5=ecf6fd2b19915afc4da56043926ca18f"

S = "${WORKDIR}/DirectFB-examples-${PV}"

inherit autotools

SRC_URI[md5sum] = "f2a5dac3bf1eb8bc0f766525831ac467"
SRC_URI[sha256sum] = "7ceb9539a39e4221d838a3b8e8a3834c0c254ecdb34afc27e9bbcf55027cbbb1"
