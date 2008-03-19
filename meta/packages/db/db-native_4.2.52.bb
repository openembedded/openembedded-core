# This wrapper builds a native version of the SleepyCat
# Berkeley DB for those packages which need it (e.g.
# perl).
SECTION = "libs"
VIRTUAL_NAME = "virtual/db-native"
CONFLICTS = "db3-native"
#PR tracks the non-native package

require db_${PV}.bb

inherit native

do_stage() {
        # The .h files get installed read-only, the autostage
        # function just uses cp -pPR, so do this by hand
        rm -rf ${STAGE_TEMP}
        mkdir -p ${STAGE_TEMP}
        oe_runmake DESTDIR="${STAGE_TEMP}" install_include
        cp -pPRf ${STAGE_TEMP}/${includedir}/* ${STAGING_INCDIR}/.
        rm -rf ${STAGE_TEMP}
        oe_libinstall -so -C .libs libdb-4.2 ${STAGING_LIBDIR}
        ln -sf libdb-4.2.so ${STAGING_LIBDIR}/libdb.so
        ln -sf libdb-4.2.a ${STAGING_LIBDIR}/libdb.a
}
