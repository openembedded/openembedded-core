DESCRIPTION = "Matchbox virtual keyboard for X11"
LICENSE = "GPL"
DEPENDS = "libfakekey expat libxft gtk+ matchbox-panel-2"
RDEPENDS = "formfactor"
SECTION = "x11"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
	   file://80matchboxkeyboard"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-cairo --enable-gtk-im --enable-applet"

PACKAGES += "matchbox-keyboard-im matchbox-keyboard-im-dbg \
             matchbox-keyboard-applet matchbox-keyboard-applet-dbg"

FILES_${PN} = "${bindir}/* \
	       ${sysconfdir} \
	       ${datadir}/applications \
	       ${datadir}/pixmaps \
	       ${datadir}/matchbox-keyboard"

FILES_matchbox-keyboard-im = "${libdir}/gtk-2.0/*/immodules/*.so"
FILES_matchbox-keyboard-im-dbg += "${libdir}/gtk-2.0/*/immodules/.debug"

FILES_matchbox-keyboard-applet = "${libdir}/matchbox-panel/*.so"
FILES_matchbox-keyboard-applet-dbg += "${libdir}/matchbox-panel/.debug"

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d/
	install -m 755 ${WORKDIR}/80matchboxkeyboard ${D}/${sysconfdir}/X11/Xsession.d/
}

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
