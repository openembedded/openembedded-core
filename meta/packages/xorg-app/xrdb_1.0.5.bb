require xorg-app-common.inc

DESCRIPTION = "X server resource database utility"
LICENSE = "xrdb"
DEPENDS += "libxmu"
PE = "1"
PR = "r1"

SRC_URI += "file://60xXDefaults"

do_install_append() {
    install -d ${D}${sysconfdir}/X11/Xsession.d/
    install -m 0755 ${WORKDIR}/60xXDefaults ${D}${sysconfdir}/X11/Xsession.d/
}
