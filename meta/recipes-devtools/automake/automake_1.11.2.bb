require automake.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe" 
DEPENDS_virtclass-native = "autoconf-native"

RDEPENDS_${PN} += "\
    autoconf \
    perl \
    perl-module-bytes \
    perl-module-constant \
    perl-module-cwd \
    perl-module-data-dumper \
    perl-module-dynaloader \
    perl-module-errno \
    perl-module-exporter-heavy \
    perl-module-file-basename \
    perl-module-file-compare \
    perl-module-file-copy \
    perl-module-file-glob \
    perl-module-file-spec-unix \
    perl-module-file-stat \
    perl-module-getopt-long \
    perl-module-io \
    perl-module-io-file \
    perl-module-posix \
    perl-module-strict \
    perl-module-text-parsewords \
    perl-module-vars "

RDEPENDS_${PN}_virtclass-native = "autoconf-native perl-native-runtime"
RDEPENDS_${PN}_virtclass-nativesdk = "autoconf-nativesdk"

PATHFIXPATCH = "file://path_prog_fixes.patch"
PATHFIXPATCH_virtclass-native = ""
PATHFIXPATCH_virtclass-nativesdk = ""

PERLPATH = "${bindir}/perl"
PERLPATH_virtclass-native = "/usr/bin/perl"
PERLPATH_virtclass-nativesdk = "/usr/bin/perl"

SRC_URI += "${PATHFIXPATCH} \
	    file://prefer-cpio-over-pax-for-ustar-archives.patch \
	    file://python-libdir.patch \
            file://automake_1.11.2_fix_for_pkglibexec_SCRIPTS.patch \
            file://py-compile-compile-only-optimized-byte-code.patch"

PR = "r3"
SRC_URI[md5sum] = "18194e804d415767bae8f703c963d456"
SRC_URI[sha256sum] = "4f46d1f9380c8a3506280750f630e9fc915cb1a435b724be56b499d016368718"

do_install () {
    oe_runmake 'DESTDIR=${D}' install
    install -d ${D}${datadir}

    # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
    # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
    for i in aclocal aclocal-1.11 automake automake-1.11; do
        if [ -f ${D}${bindir}/$i ]; then
            sed -i -e '1s,#!.*perl,#! ${PERLPATH},' \
            -e 's,exec .*/bin/perl \(.*\) exec .*/bin/perl \(.*\),exec ${PERLPATH} \1 exec ${PERLPATH} \2,' \
            ${D}${bindir}/$i
        fi
    done
}

BBCLASSEXTEND = "native nativesdk"
