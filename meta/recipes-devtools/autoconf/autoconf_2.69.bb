SUMMARY = "A GNU tool that produce shell scripts to automatically configure software"
DESCRIPTION = "Autoconf is an extensible package of M4 macros that produce shell scripts to automatically \ 
configure software source code packages. Autoconf creates a configuration script for a package from a template \
file that lists the operating system features that the package can use, in the form of M4 macro calls."
LICENSE = "GPLv3+"
HOMEPAGE = "http://www.gnu.org/software/autoconf/"
SECTION = "devel"
DEPENDS = "m4-native gnu-config-native"

PR = "r11"

LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
		    file://COPYINGv3;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.gz \
           file://program_prefix.patch \
           file://check-automake-cross-warning.patch \
           file://autoreconf-exclude.patch \
           file://autoreconf-gnuconfigize.patch \
           file://config_site.patch \
           file://remove-usr-local-lib-from-m4.patch \
           file://preferbash.patch \
           file://autotest-automake-result-format.patch \
           file://add_musl_config.patch \
           file://performance.patch \
           file://AC_HEADER_MAJOR-port-to-glibc-2.25.patch \
           file://autoconf-replace-w-option-in-shebangs-with-modern-use-warnings.patch \
           "

SRC_URI[md5sum] = "82d05e03b93e45f5a39b828dc9c6c29b"
SRC_URI[sha256sum] = "954bd69b391edc12d6a4a51a2dd1476543da5c6bbf05a95b59dc0dd6fd4c2969"

SRC_URI_append_class-native = " file://fix_path_xtra.patch"

RDEPENDS_${PN} = "m4 gnu-config \
		  perl \
		  perl-module-bytes \
		  perl-module-carp \
		  perl-module-constant \
		  perl-module-data-dumper \
		  perl-module-errno \
		  perl-module-exporter \
		  perl-module-file-basename \
		  perl-module-file-compare \
		  perl-module-file-copy \
		  perl-module-file-find \
		  perl-module-file-glob \
		  perl-module-file-path \
		  perl-module-file-spec \
		  perl-module-file-spec-unix \
		  perl-module-file-stat \
		  perl-module-getopt-long \
		  perl-module-io-file \
		  perl-module-overloading \
		  perl-module-posix \
		  perl-module-symbol \
		  perl-module-thread-queue \
		  perl-module-threads \
		 "
RDEPENDS_${PN}_class-native = "m4-native gnu-config-native hostperl-runtime-native"

inherit autotools texinfo

PERL = "${USRBINPATH}/perl"
PERL_class-native = "/usr/bin/env perl"
PERL_class-nativesdk = "/usr/bin/env perl"

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='${PERL}'"

EXTRA_OECONF += "ac_cv_path_M4=m4 ac_cv_prog_TEST_EMACS=no"

do_configure() {
	# manually install a newer config.guess/.sub
	install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.guess ${S}/build-aux
	install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.sub ${S}/build-aux

	oe_runconf
}

do_install_append() {
    rm -rf ${D}${datadir}/emacs
}

BBCLASSEXTEND = "native nativesdk"
