DESCRIPTION = "An sh-compatible command language interpreter."
HOMEPAGE = "http://cnswww.cns.cwru.edu/~chet/bash/bashtop.html"
SECTION = "base/shell"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fd5d9bcabd8ed5a54a01ce8d183d592a"
DEPENDS = "ncurses"

PR = "r8"

SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-049;apply=yes;striplevel=0 \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-050;apply=yes;striplevel=0 \
           ${GNU_MIRROR}/bash/bash-3.2-patches/bash32-051;apply=yes;striplevel=0"

inherit autotools gettext

PARALLEL_MAKE = ""

bindir = "/bin"
sbindir = "/sbin"

EXTRA_OECONF = "--with-ncurses"
export CC_FOR_BUILD = "${BUILD_CC}"

export AUTOHEADER = "true"

do_configure_prepend () {
	if [ ! -e acinclude.m4 ]; then
		cat aclocal.m4 > acinclude.m4
	fi
}

pkg_postinst_${PN} () {
	touch $D${sysconfdir}/shells
	grep -q "bin/bash" $D${sysconfdir}/shells || echo /bin/bash >> $D${sysconfdir}/shells
	grep -q "bin/sh" $D${sysconfdir}/shells || echo /bin/sh >> $D${sysconfdir}/shells
}
