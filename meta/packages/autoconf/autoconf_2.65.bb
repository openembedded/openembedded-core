require autoconf.inc

PR = "r2"

PARALLEL_MAKE = ""

DEPENDS += "m4-native"
RDEPENDS_${PN} = "m4 gnu-config"

SRC_URI += "file://autoreconf-include.patch;patch=1 \
	   file://autoreconf-exclude.patch;patch=1 \
	   file://autoreconf-foreign.patch;patch=1 \
	   file://autoreconf-gnuconfigize.patch;patch=1 \
	   file://autoheader-nonfatal-warnings.patch;patch=1 \
	   ${@['file://path_prog_fixes.patch;patch=1', ''][bb.data.inherits_class('native', d)]} \
           file://config_site.patch;patch=1"

DEFAULT_PREFERENCE = "-1"

DEPENDS_virtclass-native = "m4-native gnu-config-native"
RDEPENDS_${PN}_virtclass-native = "m4-native gnu-config-native"

SRC_URI_append_virtclass-native = " file://fix_path_xtra.patch;patch=1"

BBCLASSEXTEND = "native"
