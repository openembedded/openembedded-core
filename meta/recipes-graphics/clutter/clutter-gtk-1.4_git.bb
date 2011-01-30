require clutter-gtk.inc

PV = "0.90.0+git${SRCPV}"
PR = "r0"

SRC_URI =  "git://git.clutter-project.org/clutter-gtk.git;protocol=git;branch=master \
           file://disable_deprecated.patch \
           file://enable_tests.patch \
           "

LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

S = "${WORKDIR}/git"

DEPENDS += "gtk+ clutter-1.4"

EXTRA_OECONF += "--disable-introspection"
