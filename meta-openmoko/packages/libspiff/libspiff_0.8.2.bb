DESCRIPTION = "libSpiff brings XSPF playlist reading and writing support to your C++ application."
HOMEPAGE = "http://libspiff.sf.net"
SECTION = "libs"
LICENSE = "BSD"
DEPENDS = "expat liburiparser"

SRC_URI = "${SOURCEFORGE_MIRROR}/libspiff/libspiff-${PV}.tar.bz2 \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/libspiff-${PV}"

inherit autotools pkgconfig lib_package

EXTRA_OECONF = "\
  --with-expat=${STAGING_LIBDIR}/.. \
  --with-uriparser=${STAGING_LIBDIR}/.. \
"

CPPFLAGS += "-I${S}/include"

do_stage() {
	autotools_stage_all
}

