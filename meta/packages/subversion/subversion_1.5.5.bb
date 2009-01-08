DESCRIPTION = "The Subversion (svn) client"
SECTION = "console/network"
DEPENDS = "apr-util neon"
RDEPENDS = "neon"
LICENSE = "Apache BSD"
HOMEPAGE = "http://subversion.tigris.org"

PR = "r4"

SRC_URI = "http://subversion.tigris.org/downloads/${P}.tar.bz2 \
           file://disable-revision-install.patch;patch=1 \
	   file://neon-detection.patch;patch=1 \
	   file://libtool2.patch;patch=1"

EXTRA_OECONF = "--with-neon=${STAGING_EXECPREFIXDIR} \
                --without-berkeley-db --without-apxs --without-apache \
                --without-swig --with-apr=${STAGING_BINDIR_CROSS} \
                --with-apr-util=${STAGING_BINDIR_CROSS}"

inherit autotools_stage

export LDFLAGS += " -L${STAGING_LIBDIR} "

acpaths = "-I build/ -I build/ac-macros/"

do_configure_prepend () {
	rm -f ${S}/libtool
	rm -f ${S}/build/libtool.m4
}
