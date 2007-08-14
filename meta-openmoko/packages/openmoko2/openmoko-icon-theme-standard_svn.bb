DESCRIPTION = "Standard Gtk+ icon theme for the OpenMoko distribution"
SECTION = "openmoko/base"
PV = "0.0+svn${SRCDATE}"
PR = "r1"

inherit openmoko-base autotools

SRC_URI = "${OPENMOKO_MIRROR}/src/target/2007.2/artwork;module=icons;proto=http"
S = "${WORKDIR}/icons"

PACKAGE_ARCH = "all"

pkg_postinst_${PN} () {
        if [ "x$D" != "x" ]; then
                exit 1
        fi
        gtk-update-icon-cache -q /usr/share/icons/openmoko-standard
}

