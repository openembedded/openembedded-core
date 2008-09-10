SECTION = "x11/wm"
DESCRIPTION = "Matchbox window manager"
LICENSE = "GPL"
DEPENDS = "virtual/libx11 libxext libxrender startup-notification expat gconf pango libxdamage libxcomposite gtk+"

PV = "0.0+svnr${SRCREV}"
PR = "r4"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=matchbox-window-manager-2;proto=http \
	  "

S = "${WORKDIR}/matchbox-window-manager-2"

inherit autotools pkgconfig update-alternatives

# Change this to x-session-manager->matchbox-session and put in
# matchbox-session, then change this to x-window-manager to
# matchbox-window-manager-2.
ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/matchbox-session"
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

do_stage() {
           autotools_stage_all
}
