require autoconf_${PV}.bb

DEPENDS = "m4-native gnu-config-native"
RDEPENDS_${PN} = "m4-native gnu-config-native"

SRC_URI += "file://fix_path_xtra.patch;patch=1"

inherit native
