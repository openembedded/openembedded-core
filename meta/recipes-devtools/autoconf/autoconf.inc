SUMMARY = "A GNU tool that procude shell scripts to automatically configure software."
DESCRIPTION = "Autoconf is an extensible package of M4 macros that produce shell scripts to automatically \ 
configure software source code packages. Autoconf creates a configuration script for a package from a template \
file that lists the operating system features that the package can use, in the form of M4 macro calls."
LICENSE = "GPLv3"
HOMEPAGE = "http://www.gnu.org/software/autoconf/"
SECTION = "devel"
DEPENDS += "m4-native"
DEPENDS_virtclass-native = "m4-native gnu-config-native"
DEPENDS_virtclass-nativesdk = "m4-nativesdk gnu-config-nativesdk"
RDEPENDS_${PN} = "m4 gnu-config"

SRC_URI = "${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.bz2 \
	   file://program_prefix.patch"

inherit autotools
