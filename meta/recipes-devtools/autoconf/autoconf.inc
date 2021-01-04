SUMMARY = "A GNU tool that produce shell scripts to automatically configure software"
DESCRIPTION = "Autoconf is an extensible package of M4 macros that produce shell scripts to automatically \ 
configure software source code packages. Autoconf creates a configuration script for a package from a template \
file that lists the operating system features that the package can use, in the form of M4 macro calls."
LICENSE = "GPLv3"
HOMEPAGE = "http://www.gnu.org/software/autoconf/"
SECTION = "devel"

DEPENDS = "m4-native autoconf-native automake-native gnu-config-native help2man-native"
DEPENDS_remove_class-native = "autoconf-native automake-native help2man-native"

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

do_configure_class-native() {
	# To bootstrap we need to do things manually
	install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.guess ${S}/build-aux
	install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.sub ${S}/build-aux

	oe_runconf
}

do_install_append() {
    rm -rf ${D}${datadir}/emacs
}
