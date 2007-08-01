DESCRIPTION = "A package of M4 macros to produce scripts to \
automatically configure sourcecode."
LICENSE = "GPL"
HOMEPAGE = "http://www.gnu.org/software/autoconf/"
SECTION = "devel"
DEPENDS += "m4-native"
RDEPENDS_${PN} = "m4 gnu-config"

SRC_URI = "${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.bz2 \
	   file://program_prefix.patch;patch=1"

inherit autotools
