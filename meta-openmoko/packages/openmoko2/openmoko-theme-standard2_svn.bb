DESCRIPTION = "Standard Gtk+ theme for the Openmoko framework"
SECTION = "openmoko/base"
RCONFLICTS = "openmoko-theme-standard"
PV = "0.1.1+svnr${SRCREV}"
PR = "r5"

inherit openmoko2

SRC_URI = "svn://svn.openmoko.org/trunk/src/target/OM-2007.2/artwork/themes;module=openmoko-standard-2;proto=http"
S = "${WORKDIR}/openmoko-standard-2"

do_install() {
	find ${WORKDIR} -name ".svn" | xargs rm -rf
	install -d ${D}${datadir}/themes/openmoko-standard-2/gtk-2.0
	cp -fpPR ${S}/* ${D}${datadir}/themes/openmoko-standard-2/
	rm -rf ${D}${datadir}/themes/openmoko-standard-2/patches/
	
}

PACKAGE_ARCH = "all"
FILES_${PN} = "${datadir}"
