DESCRIPTION = "distcc is a parallel build system that distributes \
compilation of C/C++/ObjC code across machines on a network."
SECTION = "devel"
LICENSE = "GPLv2"
PR = "r1"

DEPENDS = "avahi gtk+"
RRECOMMENDS = "avahi-daemon"

SRC_URI = "http://distcc.samba.org/ftp/distcc/distcc-${PV}.tar.bz2 \
	   http://0pointer.de/public/distcc-avahi.patch;patch=1 \
	   file://default \
	   file://distcc"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "distcc"

EXTRA_OECONF = " --with-gtk "

do_install_append() {
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/default
    install -m 0755 ${WORKDIR}/distcc ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/default ${D}${sysconfdir}/default/distcc
}

PACKAGES += "distcc-distmon-gnome"

FILES_${PN} = " ${sysconfdir} \
		${bindir}/distcc \
		${bindir}/distccd \
		${bindir}/distccmon-text"
FILES_distcc-distmon-gnome = "  ${bindir}/distccmon-gnome \
				${datadir}/distcc"
