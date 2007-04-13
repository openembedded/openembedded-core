SECTION = "x11"
DESCRIPTION = "Sato Icon Theme"
LICENSE = "LGPL"
DEPENDS = ""
PV = "0.0+svn${SRCDATE}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=sato-icon-theme;proto=http"
S = "${WORKDIR}/sato-icon-theme"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}"

pkg_postinst_${PN} () {
        if [ "x$D" != "x" ]; then
                exit 1
        fi
        gtk-update-icon-cache -q /usr/share/icons/Sato
}
