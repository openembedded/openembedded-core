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

SRC_URI += "${PATHFIXPATCH} \
	    file://prefer-cpio-over-pax-for-ustar-archives.patch \
	    file://python-libdir.patch"

SRC_URI[md5sum] = "c2972c4d9b3e29c03d5f2af86249876f"
SRC_URI[sha256sum] = "5b159d3c0e0a1f87de71b68bcb9f1a1c49e9e71749c9b723f17e2e1e0295c7ae"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${datadir}
}

BBCLASSEXTEND = "native"
