require clutter-gst.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://clutter-gst/clutter-gst.h;beginline=1;endline=24;md5=95baacba194e814c110ea3bdf25ddbf4"

DEPENDS += "clutter-1.6"

PR = "r1"

SRC_URI = "http://source.clutter-project.org/sources/clutter-gst/1.3/clutter-gst-${PV}.tar.bz2 \
           file://enable_tests.patch"

SRC_URI[md5sum] = "5ec828cd10713c1c32996e5fa65aa956"
SRC_URI[sha256sum] = "68c79596ae46ed54885409cac1eeeaf6819673e2f41ee3014fe9705dfbbc1b5a"

S = "${WORKDIR}/clutter-gst-${PV}"

do_configure_prepend () {
       # Disable DOLT
       sed -i -e 's/^DOLT//' ${S}/configure.ac
}
