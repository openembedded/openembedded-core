DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "curl gpgme"
PV = "0.0+svnr${SRCREV}"
PE = "1"

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;proto=http \
	file://opkg_unarchive.patch;patch=1;maxrev=201 \
	file://opkg-intercept-cleanup.patch;patch=1 \
	file://add_vercmp.patch;patch=1"

S = "${WORKDIR}/trunk"

# Werror gives all kinds bounds issuses with gcc 4.3.3
do_configure_prepend() {
    sed -i -e s:-Werror::g ${S}/libopkg/Makefile.am
}

inherit autotools_stage pkgconfig
