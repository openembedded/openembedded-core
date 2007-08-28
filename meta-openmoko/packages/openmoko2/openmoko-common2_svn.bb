DESCRIPTION = "Common files for the OpenMoko distribution"
SECTION = "openmoko/base"
PV = "0.0+svn${SRCDATE}"
PR = "r3"

inherit openmoko2

SRC_URI = "${OPENMOKO_MIRROR}/src/target/${OPENMOKO_RELEASE}/artwork;module=pixmaps;proto=http"
S = "${WORKDIR}"

dirs = "pixmaps"

do_install() {
	find ${WORKDIR} -name ".svn" | xargs rm -rf
	install -d ${D}${datadir}
	for i in ${dirs}; do
		cp -fpPR ${S}/$i ${D}${datadir}
	done
}

PACKAGE_ARCH = "all"
FILES_${PN} = "${datadir}"
