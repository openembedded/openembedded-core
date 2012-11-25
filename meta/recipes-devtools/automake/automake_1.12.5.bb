require automake.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe" 
DEPENDS_class-native = "autoconf-native"

RDEPENDS_${PN} += "\
    autoconf \
    perl \
    perl-module-bytes \
    perl-module-data-dumper \
    perl-module-strict \
    perl-module-text-parsewords \
    perl-module-thread-queue \
    perl-module-threads \
    perl-module-vars "

RDEPENDS_${PN}_class-native = "autoconf-native perl-native-runtime"

PATHFIXPATCH = "file://path_prog_fixes.patch"
PATHFIXPATCH_class-native = ""
PATHFIXPATCH_class-nativesdk = ""

SRC_URI += "${PATHFIXPATCH} \
	    file://prefer-cpio-over-pax-for-ustar-archives.patch \
	    file://python-libdir.patch \
            file://py-compile-compile-only-optimized-byte-code.patch"

SRC_URI[md5sum] = "674f6d28f8723e0f478cb03de30f36f1"
SRC_URI[sha256sum] = "82089e23b384d3e64efa4f09f133a02dadb91c0593d4f1d4e12c29d806be9925"

PR = "r0"

do_install () {
    oe_runmake 'DESTDIR=${D}' install
    install -d ${D}${datadir}

    # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
    # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
    for i in aclocal aclocal-1.12 automake automake-1.12; do
        if [ -f ${D}${bindir}/$i ]; then
            sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/perl,' \
            -e 's,exec .*/bin/perl \(.*\) exec .*/bin/perl \(.*\),exec ${USRBINPATH}/perl \1 exec ${USRBINPATH}/perl \2,' \
            ${D}${bindir}/$i
        fi
    done
}

BBCLASSEXTEND = "native nativesdk"
