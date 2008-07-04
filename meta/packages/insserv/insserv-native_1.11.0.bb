LICENSE = "GPL"
SECTION = "base"

SRC_URI = "ftp://ftp.suse.com/pub/projects/init/insserv-${PV}.tar.gz \
        file://40_segfault_virtprov.dpatch;patch=1 \
        file://42_loopnochangemsg.dpatch;patch=1 \
        file://make.patch;patch=1 \
        file://insserv.conf"

S = "${WORKDIR}/insserv-${PV}"

inherit native

do_stage () {
	oe_runmake 'DESTDIR=${STAGING_DIR_NATIVE}' install
        install -m0644 ${WORKDIR}/insserv.conf ${STAGING_ETCDIR_NATIVE}/insserv.conf
}
