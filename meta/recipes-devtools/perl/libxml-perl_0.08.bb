DESCRIPTION = "libxml-perl is a collection of smaller Perl modules, scripts, and \
documents for working with XML in Perl.  libxml-perl software \
works in combination with XML::Parser, PerlSAX, XML::DOM, \
XML::Grove and others."
SUMMARY = "Collection of Perl modules for working with XML."
SECTION = "libs"
LICENSE = "Artistic | GPLv1+"
PR = "r0"

LIC_FILES_CHKSUM = "file://README;beginline=33;endline=35;md5=1705549eef7577a3d6ba71123a1f0ce8"

DEPENDS += "libxml-parser-perl"

SRC_URI = "http://www.cpan.org/modules/by-module/XML/${BPN}-${PV}.tar.gz"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

