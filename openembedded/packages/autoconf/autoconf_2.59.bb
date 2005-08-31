DESCRIPTION = "A package of M4 macros to produce scripts to \
automatically configure sourcecode."
LICENSE = "GPL"
HOMEPAGE = "http://www.gnu.org/software/autoconf/"
SECTION = "devel"
DEPENDS += "m4-native"
RDEPENDS_${PN} = "m4 gnu-config"
RRECOMMENDS_${PN} = "automake"
PR = "r3"

SRC_URI = "${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.bz2 \
	   file://program_prefix.patch;patch=1 \
	   file://autoreconf-include.patch;patch=1 \
	   file://autoreconf-exclude.patch;patch=1 \
	   file://autoreconf-foreign.patch;patch=1 \
	   file://autoreconf-gnuconfigize.patch;patch=1 \
	   file://autoconf259-update-configscripts.patch;patch=1 \
	   file://autoheader-nonfatal-warnings.patch;patch=1 \
	   file://sizeof_types.patch;patch=1 \
	   ${@['file://path_prog_fixes.patch;patch=1', ''][bb.data.inherits_class('native', d)]}"
inherit autotools
