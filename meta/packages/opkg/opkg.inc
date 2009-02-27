DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "curl gpgme"
PV = "0.0+svnr${SRCREV}"

# opkg-fix-gcc-warnings.patch: the patch is already upstreamed and could be
#                              taken out once okpg is updated to new repo
#                              http://opkg.googlecode.com/svn/trunk
#                              with rev >= 150

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/;module=opkg;proto=http \
           file://fix_prototype.patch;patch=1 \
           file://opkg-fix-gcc-warnings.patch;patch=1"

S = "${WORKDIR}/opkg"

inherit autotools_stage pkgconfig
