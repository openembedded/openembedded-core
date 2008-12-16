DESCRIPTION = "Standard Gtk+ icon theme for the Openmoko framework"
SECTION = "openmoko/base"
PV = "0.1.0+svnr${SRCREV}"
PR = "r2"

PACKAGE_ARCH = "all"

inherit openmoko2

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/artwork/;module=icons;proto=http"
S = "${WORKDIR}/icons"

PACKAGES += "${PN}-compat"

FILES_${PN} = "${datadir}/icons/openmoko-standard"
FILES_${PN}-compat = "${datadir}/icons/hicolor"

do_install_append () {
	mkdir --parents "${D}/${datadir}/icons/hicolor"
	cd "${D}/${datadir}/icons/openmoko-standard"
	FILES=`find . -type f -name moko\* -o -name openmoko*`
	cd ../hicolor
	for F in $FILES; do
		mkdir --parents `dirname $F`
		ln -s ${datadir}/icons/openmoko-standard/$F $F
	done
}

pkg_postinst_${PN} () {
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    gtk-update-icon-cache -q /usr/share/icons/openmoko-standard
}

pkg_postinst_${PN}-compat () {
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    gtk-update-icon-cache -q /usr/share/icons/hicolor
}
