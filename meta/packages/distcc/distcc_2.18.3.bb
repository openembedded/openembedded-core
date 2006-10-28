DESCRIPTION = "distcc is a parallel build system that distributes \
compilation of C/C++/ObjC code across machines on a network."
SECTION = "devel"
LICENSE = "GPLv2"

DEPENDS = "avahi gtk+"
RRECOMMENDS = "avahi-daemon"

SRC_URI = "http://distcc.samba.org/ftp/distcc/distcc-${PV}.tar.bz2 \
	   http://0pointer.de/public/distcc-avahi.patch;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF = " --with-gtk "

PACKAGES += "distcc-distmon-gnome"

FILES_${PN} = " ${bindir}/distcc \
		${bindir}/distccd \
		${bindir}/distccmon-text"
FILES_distcc-distmon-gnome = "  ${bindir}/distccmon-gnome \
				${datadir}/distcc"


