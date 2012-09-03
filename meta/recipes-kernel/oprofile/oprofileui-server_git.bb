require oprofileui.inc

SRCREV = "82ecf8c6b53b84f80682a8312f9defa83a95f2a3"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/oprofileui;protocol=git"

EXTRA_OECONF += "--disable-client --enable-server"

RDEPENDS_${PN} = "oprofile"
