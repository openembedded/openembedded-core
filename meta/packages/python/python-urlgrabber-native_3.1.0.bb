require python-urlgrabber_${PV}.bb
inherit native
DEPENDS = "python-native"
RDEPENDS = ""

do_stage() {
	BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        ${STAGING_BINDIR}/python setup.py install --prefix=${STAGING_BINDIR}/.. --install-data=${STAGING_DATADIR}
}
