require autoconf.inc

DEPENDS += "m4-native"
RDEPENDS_${PN} = "m4 gnu-config"

SRC_URI += "file://autoreconf-include.patch;patch=1 \
	   file://autoreconf-exclude.patch;patch=1 \
	   file://autoreconf-foreign.patch;patch=1 \
	   file://autoreconf-gnuconfigize.patch;patch=1 \
	   file://autoheader-nonfatal-warnings.patch;patch=1 \
	   file://config-site.patch;patch=1 \
	   ${@['file://path_prog_fixes.patch;patch=1', ''][bb.data.inherits_class('native', d)]}"

#
# without it build break:
# | make[1]: *** No rule to make target `../bin/autom4te', needed by `autoconf.in'.  Stop.
#
PARALLEL_MAKE = ""
