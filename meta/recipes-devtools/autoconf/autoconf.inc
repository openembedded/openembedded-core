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
RDEPENDS_${PN}_virtclass-native = "m4-native gnu-config-native"
RDEPENDS_${PN}_virtclass-nativesdk = "m4-nativesdk gnu-config-nativesdk"

SRC_URI = "${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.bz2 \
	   file://program_prefix.patch"

inherit autotools

PERLPATH = "${bindir}/perl"
PERLPATH_virtclass-native = "/usr/bin/perl"
PERLPATH_virtclass-nativesdk = "/usr/bin/perl"

do_install_append() {
    rm -rf ${D}${datadir}/emacs

    # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
    # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
    for i in autoheader autom4te autoreconf autoscan autoupdate ifnames; do
        if [ -f ${D}${bindir}/$i ]; then
            sed -i -e '1s,#!.*perl,#! ${PERLPATH},' \
            -e 's,exec .*/bin/perl \(.*\) exec .*/bin/perl \(.*\),exec ${PERLPATH} \1 exec ${PERLPATH} \2,' \
            ${D}${bindir}/$i
        fi
    done
} 
