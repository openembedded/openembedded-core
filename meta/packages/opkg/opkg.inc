DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "curl gpgme"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/;module=opkg;proto=http \
           file://fix_prototype.patch;patch=1"

S = "${WORKDIR}/opkg"

inherit autotools_stage pkgconfig
