DESCRIPTION = "Standard Gtk+ icon theme for the Openmoko framework"
SECTION = "openmoko/base"
PV = "0.1.0+svnr${SRCREV}"
PR = "r1"

inherit openmoko2

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/artwork/;module=icons;proto=http"
S = "${WORKDIR}/icons"

pkg_postinst_${PN} () {
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    gtk-update-icon-cache -q /usr/share/icons/openmoko-standard
}

PACKAGE_ARCH = "all"
