require python-scons_${PV}.bb
inherit native
DEPENDS = "python-native"
RDEPENDS = ""

do_stage() {
        BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        ${STAGING_BINDIR}/python setup.py install --prefix=${STAGING_LIBDIR}/.. --install-data=${STAGING_DATADIR} || \
        oefatal "python setup.py install execution failed."
}

do_install() {
	:
}
