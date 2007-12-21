SECTION = "x11/wm"
DESCRIPTION = "Matchbox window manager"
LICENSE = "GPL"
DEPENDS = "virtual/libx11 libxext libxrender startup-notification expat gconf pango libxdamage libxcomposite"

PV = "0.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=matchbox-window-manager-2;proto=http \
	  "

S = "${WORKDIR}/matchbox-window-manager-2"

inherit autotools pkgconfig update-alternatives

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-session"
ALTERNATIVE_PRIORITY = "10"

FILES_${PN} = "${bindir}/* \
               ${datadir}/matchbox-2 \
               ${sysconfdir}/matchbox-2 \
               ${datadir}/themes/*"

EXTRA_OECONF = 	"--enable-debug			\
                 --enable-compositing-manager	\
		"

do_install_append () {
	cd ${D}${bindir}
	ln -s matchbox-window-manager-2-simple matchbox-window-manager
}
