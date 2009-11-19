require genext2fs.inc

PR = "r4"

SRC_URI = "${DEBIAN_MIRROR}/main/g/genext2fs/genext2fs_${PV}.orig.tar.gz \
	   file://misc.patch;patch=1"
S = "${WORKDIR}/genext2fs-${PV}.orig"

do_compile () {
	oe_runmake
}

NATIVE_INSTALL_WORKS = "1"
do_install () {
	install -d ${D}${bindir}/
	install -m 755 genext2fs ${D}${bindir}/
	install -d ${D}${mandir}/man8/
	install -m 644 genext2fs.8 ${D}${mandir}/man8/
}

BBCLASSEXTEND = "native"
