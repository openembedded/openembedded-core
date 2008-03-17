DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "curl gpgme"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/;module=opkg;proto=http"
S = "${WORKDIR}/opkg"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
