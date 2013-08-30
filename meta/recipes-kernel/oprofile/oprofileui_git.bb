require oprofileui.inc

DEPENDS += "gtk+ libglade libxml2 avahi-ui gconf"

SRCREV = "f168b8bfdc63660033de1739c6ddad1abd97c379"
PV = "0.0+git${SRCPV}"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/oprofileui"

EXTRA_OECONF += "--enable-client --disable-server"

PACKAGES =+ "oprofileui-viewer"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/ ${datadir}/icons"
RDEPENDS_oprofileui-viewer = "oprofile"
