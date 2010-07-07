require yum-metadata-parser_${PV}.bb
inherit native
DEPENDS = "python-native sqlite3-native glib-2.0-native libxml2-native"
RDEPENDS = ""
PR = "r0"

#BUILD_CFLAGS += "-I${STAGING_LIBDIR}/glib-2.0"

NATIVE_INSTALL_WORKS = "1"
