require clutter-gst.inc

SRCREV = "8a087fabc888a6a4a939760c392109dbb610adbf"
PV = "1.3.4+git${SRCPV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://clutter-gst/clutter-gst.h;beginline=1;endline=24;md5=3275ae5179d58dccf8852cdad3a44574"

DEPENDS += "clutter-1.4"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git;branch=master \
           file://enable_tests-1.4.patch"

S = "${WORKDIR}/git"

do_configure_prepend () {
       # Disable DOLT
       sed -i -e 's/^DOLT//' ${S}/configure.ac
}
