require automake.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe" 
DEPENDS_class-native = "autoconf-native"

NAMEVER = "${@oe.utils.trim_version("${PV}", 2)}"

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
            file://py-compile-compile-only-optimized-byte-code.patch \
            file://buildtest.patch"

SRC_URI[md5sum] = "9199e266993a5bbdc914923349d51e3e"
SRC_URI[sha256sum] = "4c93abc0bff54b296f41f92dd3aa1e73e554265a6f719df465574983ef6f878c"

do_install_append () {
    install -d ${D}${datadir}

    # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
    # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
    for i in aclocal aclocal-${NAMEVER} automake automake-${NAMEVER}; do
        if [ -f ${D}${bindir}/$i ]; then
            sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/perl,' \
            -e 's,exec .*/bin/perl \(.*\) exec .*/bin/perl \(.*\),exec ${USRBINPATH}/perl \1 exec ${USRBINPATH}/perl \2,' \
            ${D}${bindir}/$i
        fi
    done
}

BBCLASSEXTEND = "native nativesdk"
