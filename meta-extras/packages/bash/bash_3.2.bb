DESCRIPTION = "An sh-compatible command language interpreter."
HOMEPAGE = "http://cnswww.cns.cwru.edu/~chet/bash/bashtop.html"
DEPENDS = "ncurses"
SECTION = "base/shell"
LICENSE = "GPL"

SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz \
           file://001-005.patch;patch=1"

inherit autotools gettext

PARALLEL_MAKE = ""

bindir = "/bin"
sbindir = "/sbin"

EXTRA_OECONF = "--with-ncurses"
export CC_FOR_BUILD = "${BUILD_CC}"

do_configure () {
	gnu-configize
	oe_runconf
}

pkg_postinst () {
	grep -q "bin/bash" ${sysconfdir}/shells || echo /bin/bash >> ${sysconfdir}/shells
	grep -q "bin/sh" ${sysconfdir}/shells || echo /bin/sh >> ${sysconfdir}/shells
}
