require clutter-gst.inc

PV = "1.3.4+git${SRCPV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://clutter-gst/clutter-gst.h;beginline=1;endline=24;md5=95baacba194e814c110ea3bdf25ddbf4"

DEPENDS += "clutter-1.4"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git;branch=master \
           file://enable_tests.patch"

S = "${WORKDIR}/git"

do_configure_prepend () {
       # Disable DOLT
       sed -i -e 's/^DOLT//' ${S}/configure.ac
}
