include genext2fs.inc

PR = "r3"

SRC_URI = "${DEBIAN_MIRROR}/main/g/genext2fs/genext2fs_${PV}.orig.tar.gz \
	   file://misc.patch;patch=1"
S = "${WORKDIR}/genext2fs-${PV}.orig"

do_compile () {
	oe_runmake
}	

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}
