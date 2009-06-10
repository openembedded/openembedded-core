DESCRIPTION = "GNOME settings daemon"
LICENSE = "GPL"

PR = "r1"

DEPENDS = "libxklavier libgnomekbd gnome-doc-utils gtk+ libglade libgnomecanvas librsvg libxml2 libart-lgpl gnome-desktop"

inherit gnome

FILES_${PN} += "${libdir}/gnome-settings-daemon-2.0/*.so ${libdir}/gnome-settings-daemon-2.0/*plugin \
                ${datadir}/dbus-1/ \
                ${datadir}/icon* \
                ${datadir}/xsession*"

FILES_${PN}-dbg += "${libdir}/gnome-settings-daemon-2.0/.debug"
FILES_${PN}-dev += "${libdir}/gnome-settings-daemon-2.0/*.a ${libdir}/gnome-settings-daemon-2.0/*.la"

do_stage() {    
	autotools_stage_all
}

ASNEEDED = ""