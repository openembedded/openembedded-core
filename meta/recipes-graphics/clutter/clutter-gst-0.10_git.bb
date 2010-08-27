require clutter-gst.inc

PV = "0.10.0+git${SRCPV}"

DEPENDS += "clutter-1.0"

SRC_URI = "git://git.clutter-project.org/clutter-gst.git;protocol=git;branch=clutter-gst-0.10"

S = "${WORKDIR}/git"

do_configure_prepend () {
       # Disable DOLT
       sed -i -e 's/^DOLT//' ${S}/configure.ac
}
