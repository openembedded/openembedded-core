SUMMARY = "User Interface for the System-Wide Profiler"
DESCRIPTION = "User interface for the OProfile tool"
HOMEPAGE = "http://labs.o-hand.com/oprofileui/"
BUGTRACKER = "http://bugzilla.o-hand.com/"

SECTION = "x11"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "glib-2.0 gtk+ libglade libxml2 avahi-ui gconf"

inherit autotools pkgconfig gtk-icon-cache

PACKAGES =+ "oprofileui-server oprofileui-viewer"

EXTRA_OECONF = "--with-avahi"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/"
RDEPENDS_oprofile-viewer = "oprofile"

FILES_oprofileui-server = "${bindir}/oprofile-server"
RDEPENDS_oprofile-server = "oprofile"
