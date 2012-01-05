SECTION = "x11/wm"
DESCRIPTION = "Matchbox window manager"
LICENSE = "GPLv2.0+"
DEPENDS = "libmatchbox virtual/libx11 libxext libxcomposite libxfixes xdamage libxrender startup-notification expat"
PR = "r5"

SRC_URI = "http://matchbox-project.org/sources/matchbox-window-manager/1.2/matchbox-window-manager-${PV}.tar.bz2 \
           file://configure_fix.patch \
           file://kbdconfig \
           file://gconf-2.m4"

S = "${WORKDIR}/matchbox-window-manager-${PV}"

inherit autotools pkgconfig

FILES_${PN} = "${bindir}/* \
	       ${datadir}/matchbox \
	       ${sysconfdir}/matchbox \
	       ${datadir}/themes/blondie/matchbox \
	       ${datadir}/themes/Default/matchbox \
	       ${datadir}/themes/MBOpus/matchbox"

EXTRA_OECONF = " --enable-startup-notification \
                 --disable-xrm \
                 --enable-expat \
                 --with-expat-lib=${STAGING_LIBDIR} \
                 --with-expat-includes=${STAGING_INCDIR}"


do_configure_prepend () {
        cp ${WORKDIR}/gconf-2.m4 ${S}/
}

do_install_prepend() {
	install ${WORKDIR}/kbdconfig ${S}/data/kbdconfig
}
