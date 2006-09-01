SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "libogg"
DESCRIPTION = "tremor is a fixed point implementation of the vorbis codec."
LICENSE = "BSD"
SRCDATE = "${PV}"
PR = "r1"

SRC_URI = "svn://svn.xiph.org/trunk;module=Tremor;rev=4573;proto=http"

S = "${WORKDIR}/Tremor"

inherit autotools

EXTRA_OECONF=" --enable-shared --disable-rpath  "

#do_configure_prepend() {
#    ./autogen.sh
#}

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR}/tremor \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
