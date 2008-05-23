DESCRIPTION = "Common files for the Openmoko framework"
SECTION = "openmoko/base"
PV = "0.1.0+svnr${SRCREV}"
PR = "r0"

inherit openmoko2

SRC_URI = "${OPENMOKO_MIRROR}/src/target/${OPENMOKO_RELEASE}/artwork;module=pixmaps;proto=http"
S = "${WORKDIR}"

ALLOW_EMPTY = "1"

dirs = "pixmaps"

do_install() {
	find . -name .svn | xargs rm -rf
	install -d ${D}${datadir}
	for i in ${dirs}; do
		cp -fR $i ${D}${datadir}/$i;
	done
	# moved to xserver-kdrive-common
	rm -f ${D}${datadir}/pixmaps/xsplash*
}

PACKAGE_ARCH = "all"
FILES_${PN} = "${datadir}"
