require recipes-graphics/clutter/clutter-gst.inc

PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://clutter-gst/clutter-gst.h;beginline=1;endline=24;md5=95baacba194e814c110ea3bdf25ddbf4"

DEPENDS += "clutter-1.8 gstreamer"
RDEPENDS_${PN} += "gst-meta-base"

SRC_URI = "http://source.clutter-project.org/sources/clutter-gst/1.4/clutter-gst-${PV}.tar.bz2 \
           file://enable_tests-1.8.patch"

S = "${WORKDIR}/clutter-gst-${PV}"

SRC_URI[md5sum] = "fd6b2a54f43d04382748e9e4d8a335c5"
SRC_URI[sha256sum] = "b0e7ff76ee14307b6d26856972ec9e718f62aec2bc8807fb3f2960e508e578b5"

do_configure_prepend () {
       # Disable DOLT
       sed -i -e 's/^DOLT//' ${S}/configure.ac
}
