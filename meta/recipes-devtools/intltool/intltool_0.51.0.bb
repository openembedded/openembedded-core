SUMMARY = "Utility scripts for internationalizing XML"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://launchpad.net/${BPN}/trunk/${PV}/+download/${BP}.tar.gz \
           file://intltool-nowarn.patch \
           file://perl-522-deprecations.patch \
           ${NATIVEPATCHES} \
           "
SRC_URI[md5sum] = "12e517cac2b57a0121cda351570f1e63"
SRC_URI[sha256sum] = "67c74d94196b153b774ab9f89b2fa6c6ba79352407037c8c14d5aeb334e959cd"

# All of the intltool scripts have the correct paths to perl already
# embedded into them and can find perl fine, so we add the remove xml-check
# in the intltool.m4 via the remove-xml-check.patch
NATIVEPATCHES = "file://noperlcheck.patch \
                 file://remove-xml-check.patch"
NATIVEPATCHES_class-native = "file://use-nativeperl.patch"

UPSTREAM_CHECK_URI = "https://launchpad.net/intltool/trunk/"

DEPENDS = "libxml-parser-perl-native"
RDEPENDS_${PN} = "gettext-dev libxml-parser-perl"
DEPENDS_class-native = "libxml-parser-perl-native"
# gettext is assumed to exist on the host
RDEPENDS_${PN}_class-native = "libxml-parser-perl-native"
RRECOMMENDS_${PN} = "perl-modules"
RRECOMMENDS_${PN}_class-native = ""

FILES_${PN}-dev = ""
FILES_${PN} += "${datadir}/aclocal"

INSANE_SKIP_${PN} += "dev-deps"

inherit autotools pkgconfig perlnative

export PERL = "${bindir}/env perl"
PERL_class-native = "/usr/bin/env nativeperl"

BBCLASSEXTEND = "native"
