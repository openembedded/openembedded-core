require yum-metadata-parser_${PV}.bb
inherit native
DEPENDS = "python-native sqlite3-native"
RDEPENDS = ""

#BUILD_CFLAGS += "-I${STAGING_LIBDIR}/glib-2.0"

do_stage() {
	BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        ${STAGING_BINDIR}/python setup.py install --prefix=${STAGING_BINDIR}/.. --install-data=${STAGING_DATADIR}
}
