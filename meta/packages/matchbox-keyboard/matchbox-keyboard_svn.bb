DESCRIPTION = "Matchbox virtual keyboard for X11"
LICENSE = "GPL"
DEPENDS = "libfakekey expat libxft gtk+"
SECTION = "x11"
PV = "0.0+svn${SRCDATE}"
PR="r3"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"


S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-cairo --enable-gtk-im"

PACKAGES += "matchbox-keyboard-im matchbox-keyboard-im-dbg"

FILES_${PN} = "${bindir}/* \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
	       ${datadir}/matchbox-keyboard"

FILES_matchbox-keyboard-im = "${libdir}/gtk-2.0/*/immodules/*.so"

FILES_matchbox-keyboard-dbg = "${libdir}/gtk-2.0/*/immodules/.debug"


pkg_postinst_matchbox-keyboard-im () {
if [ "x$D" != "x" ]; then
  exit 1
fi

gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules
}

pkg_postrm_matchbox-keyboard-im () {
if [ "x$D" != "x" ]; then
  exit 1
fi

gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules
}
