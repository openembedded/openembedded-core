SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "LGPLv2.1+ & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://COPYING.LIB;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://lib/idna.h;firstline=6;endline=18;md5=6d9d5532eb28b99e860262281c540d02 \
                    file://src/idn.c;firstline=6;endline=18;md5=41b6aec531cc6a9d54a6c4deee251bf5"
PR = "r0"

inherit pkgconfig autotools gettext

SRC_URI = "http://alpha.gnu.org/gnu/libidn/${BPN}-${PV}.tar.gz"

EXTRA_OECONF = " --disable-tld"

do_configure_prepend() {
	# this version of libidn copies AC_USE_SYSTEM_EXTENSIONS from 
	# autoconf CVS because atm the autoconf it uses is a bit old
	# now with cross autotool, that macro is already there and this
	# local definition causes circular dependency. Actually AC_GNU_SOURCE
	# is identical to AC_USE_SYSTEM_EXTENSIONS. So remove all local
	# references to the latter here.
	sed -i -e "/AC_REQUIRE(\[gl_USE_SYSTEM_EXTENSIONS/d" ${S}/lib/gl/m4/gnulib-comp.m4
	rm -f ${S}/lib/gl/m4/extensions.m4
}
