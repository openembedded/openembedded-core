require automake.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe" 
DEPENDS_virtclass-native = "autoconf-native"

RDEPENDS_automake += "\
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

RDEPENDS_automake-native = "autoconf-native perl-native-runtime"

PATHFIXPATCH = "file://path_prog_fixes.patch"
PATHFIXPATCH_virtclass-native = ""
PATHFIXPATCH_virtclass-nativesdk = ""

SRC_URI += "${PATHFIXPATCH} \
	    file://prefer-cpio-over-pax-for-ustar-archives.patch \
	    file://python-libdir.patch"

PR = "r0"
SRC_URI[md5sum] = "18194e804d415767bae8f703c963d456"
SRC_URI[sha256sum] = "4f46d1f9380c8a3506280750f630e9fc915cb1a435b724be56b499d016368718"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${datadir}
}

BBCLASSEXTEND = "native nativesdk"
