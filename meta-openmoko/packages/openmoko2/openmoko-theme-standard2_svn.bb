require openmoko.inc

DESCRIPTION = "Standard Gtk+ theme for the OpenMoko distribution"
SECTION = "openmoko/base"
RCONFLICTS = "openmoko-theme-standard"
PACKAGE_ARCH = "all"
PR = "r1"

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/artwork/themes;module=openmoko-standard-2;proto=http"

S = "${WORKDIR}/openmoko-standard-2"

do_install() {
	find ${WORKDIR} -name ".svn" | xargs rm -rf
	install -d ${D}${datadir}/themes/openmoko-standard-2/gtk-2.0
	cp -fpPR ${S}/* ${D}${datadir}/themes/openmoko-standard-2/
	rm -rf ${D}${datadir}/themes/openmoko-standard-2/patches/
	
	install -d ${D}${sysconfdir}/gtk-2.0
	echo 'include "${datadir}/themes/openmoko-standard-2/gtk-2.0/gtkrc"' >> ${D}${sysconfdir}/gtk-2.0/gtkrc
}

FILES_${PN} = "${datadir} ${sysconfdir}"
