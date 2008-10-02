require yum-metadata-parser_${PV}.bb
inherit native
DEPENDS = "python-native sqlite3-native glib-2.0-native libxml2-native"
RDEPENDS = ""

#BUILD_CFLAGS += "-I${STAGING_LIBDIR}/glib-2.0"

do_stage() {
	BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
	STAGING_LIBDIR=${STAGING_LIBDIR} STAGING_INCDIR=${STAGING_INCDIR} \
        ${STAGING_BINDIR}/python setup.py install --prefix=${STAGING_BINDIR}/.. --install-data=${STAGING_DATADIR}
}
