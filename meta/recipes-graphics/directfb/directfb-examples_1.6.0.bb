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

SRC_URI[md5sum] = "27619d31e8a647fdd3023a0c1779b63d"
SRC_URI[sha256sum] = "b08293697c211b690856f76a25ad7acd4ca458b4132851e0a1e471d0bf23c821"
