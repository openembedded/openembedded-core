SUMMARY = "A GNU tool for automatically generating Makefiles"
DESCRIPTION = "Automake is a tool for automatically generating `Makefile.in' files compliant with the GNU Coding \
Standards. Automake requires the use of Autoconf."
LICENSE = "GPL-2.0-only"
HOMEPAGE = "http://www.gnu.org/software/automake/"
SECTION = "devel"

DEPENDS = "autoconf-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "${GNU_MIRROR}/automake/automake-${PV}.tar.gz \
           file://0002-automake-Update-for-python.m4-to-respect-libdir.patch \
           file://0004-Add-a-new-distro-feature-ptest.patch \
           "

SRC_URI[sha256sum] = "79e8b1f7a967e87ce8a9ded76bee7f793d0ce1886ab2002feb1b0510f578b75a"

inherit autotools texinfo

PERL = "${USRBINPATH}/perl"
PERL:class-native = "${USRBINPATH}/env perl"
PERL:class-nativesdk = "${USRBINPATH}/env perl"

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='${PERL}'"

do_configure() {
	# We can end up patching macros, which would then mean autoreconf
	# Cheat by saying everything is up to date.
	touch ${S}/aclocal.m4 ${S}/Makefile.in ${S}/configure
	oe_runconf
}

FILES:${PN} += "${datadir}/automake* ${datadir}/aclocal*"

RDEPENDS:${PN} += "\
    autoconf \
    perl \
    perl-module-bytes \
    perl-module-data-dumper \
    perl-module-strict \
    perl-module-text-parsewords \
    perl-module-thread-queue \
    perl-module-threads \
    perl-module-vars "

RDEPENDS:${PN}:class-native = "autoconf-native hostperl-runtime-native"

BBCLASSEXTEND = "native nativesdk"
