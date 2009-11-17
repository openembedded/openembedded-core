LICENSE = "GPL"
SECTION = "base"
PR = "r1"

SRC_URI = "ftp://ftp.suse.com/pub/projects/init/${BPN}-${PV}.tar.gz \
        file://40_segfault_virtprov.dpatch;patch=1 \
        file://42_loopnochangemsg.dpatch;patch=1 \
        file://make.patch;patch=1 \
        file://crosscompile_fix.patch;patch=1 \
        file://insserv.conf"

NATIVE_INSTALL_WORKS = "1"
do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -m0644 ${WORKDIR}/insserv.conf ${D}${sysconfdir}/insserv.conf
}

BBCLASSEXTEND = "native"
