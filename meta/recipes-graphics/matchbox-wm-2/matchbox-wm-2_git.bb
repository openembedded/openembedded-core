SECTION = "x11/wm"
DESCRIPTION = "Matchbox window manager"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://matchbox/core/mb-wm.h;endline=21;md5=1b1d328a527906350ea7ce7ab1bb7564"

DEPENDS = "virtual/libx11 libxext libxrender startup-notification expat gconf pango libxdamage libxcomposite gtk+"

SRCREV = "01fa5465743c9ee43d040350f4405d35293e4869"
PV = "0.1+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.yoctoproject.org/matchbox-window-manager-2;proto=git \
	file://fix_makefile.patch \
	  "

S = "${WORKDIR}/git"

inherit autotools pkgconfig update-alternatives

# Change this to x-session-manager->matchbox-session and put in
# matchbox-session, then change this to x-window-manager to
# matchbox-window-manager-2.
ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-window-manager"
ALTERNATIVE_PRIORITY = "10"

PACKAGES =+ "libmatchbox2"

FILES_libmatchbox2 = "${libdir}/*${SOLIBS}"

FILES_${PN} += "${datadir}/matchbox-2 \
                ${sysconfdir}/matchbox-2 \
                ${datadir}/themes/*"

EXTRA_OECONF = 	"--enable-debug			\
                 --enable-simple-manager \
                 --enable-compositing-manager	\
                 --enable-libmatchbox \
                 --enable-png-theme \
		"

do_install_append () {
	cd ${D}${bindir}
	ln -s matchbox-window-manager-2-simple matchbox-window-manager
}
