require oprofileui.inc

DEPENDS += "gtk+ libglade libxml2 avahi-ui gconf"

SRCREV = "82ecf8c6b53b84f80682a8312f9defa83a95f2a3"
PV = "0.0+git${SRCPV}"
PR = "r1"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/oprofileui;protocol=git"

EXTRA_OECONF += "--enable-client --disable-server"

PACKAGES =+ "oprofileui-viewer"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/ ${datadir}/icons"
RDEPENDS_oprofileui-viewer = "oprofile"
