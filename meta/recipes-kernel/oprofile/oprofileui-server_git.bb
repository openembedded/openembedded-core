require oprofileui.inc

SRCREV = "82ecf8c6b53b84f80682a8312f9defa83a95f2a3"
PV = "0.0+git${SRCPV}"
PR = "r1"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/oprofileui;protocol=git \
           file://init \
           file://obsolete_automake_macros.patch \
"

EXTRA_OECONF += "--disable-client --enable-server"

RDEPENDS_${PN} = "oprofile"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/oprofileui-server
}

INITSCRIPT_NAME = "oprofileui-server"
INITSCRIPT_PARAMS = "start 999 5 2 . stop 20 0 1 6 ."

inherit update-rc.d
