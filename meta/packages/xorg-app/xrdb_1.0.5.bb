require xorg-app-common.inc

DESCRIPTION = "X server resource database utility"
LICENSE = "xrdb"
DEPENDS += "libxmu"
PE = "1"
PR = "r2"

SRC_URI += "file://60XDefaults.sh"

do_install_append() {
    install -d ${D}${sysconfdir}/X11/Xsession.d/
    install -m 0755 ${WORKDIR}/60XDefaults.sh ${D}${sysconfdir}/X11/Xsession.d/
}
