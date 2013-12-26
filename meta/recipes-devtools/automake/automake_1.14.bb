require automake.inc
LICENSE = "GPLv2"
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

SRC_URI += " file://python-libdir.patch \
            file://py-compile-compile-only-optimized-byte-code.patch \
            file://buildtest.patch"

SRC_URI[md5sum] = "a3c0d9298c6112f5f2c26c639ccaaed7"
SRC_URI[sha256sum] = "7847424d4204d1627c129e9c15b81e145836afa2a1bf9003ffe10aa26ea75755"

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
