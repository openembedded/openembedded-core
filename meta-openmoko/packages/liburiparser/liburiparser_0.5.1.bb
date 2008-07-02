DESCRIPTION = "uriparser is a strictly RFC 3986 compliant URI parsing library."
HOMEPAGE = "http://uriparser.sf.net"
SECTION = "libs"
LICENSE = "BSD"

SRC_URI = "${SOURCEFORGE_MIRROR}/uriparser/uriparser-${PV}.tar.bz2 \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/uriparser-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}

