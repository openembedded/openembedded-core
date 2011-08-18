DESCRIPTION = "Matchbox virtual keyboard for X11"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/matchbox-keyboard.h;endline=20;md5=4ba16ff913ad245dd6d95a6c67f72526 \
                    file://applet/applet.c;endline=20;md5=e9201b3efa0a81a160b88d6feb5cf75b"

DEPENDS = "libfakekey expat libxft gtk+ matchbox-panel-2"
RDEPENDS_${PN} = "formfactor dbus-wait"
SECTION = "x11"
SRCREV = "b38f24036cff3be6c2fbcf9ca9881803e69003ac"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://configure_fix.patch;maxrev=1819 \
           file://single-instance.patch \
	   file://80matchboxkeyboard.shbg"

S = "${WORKDIR}/git"

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
	install -m 755 ${WORKDIR}/80matchboxkeyboard.shbg ${D}/${sysconfdir}/X11/Xsession.d/
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
