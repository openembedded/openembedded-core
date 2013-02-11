SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "(LGPLv2.1+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=91146090ae24a0572879d3b48646d404 \
                    file://COPYING.LESSERv2;md5=4fbd65380cdd255951079008b364516c \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYINGv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://lib/idna.h;endline=21;md5=7364f6a250728ffe16170f5e3ab37512 \
                    file://src/idn.c;endline=20;md5=7d7235e7951ac87d9dfac42e1b69d9cb"
PR = "r1"

inherit pkgconfig autotools gettext

SRC_URI = "${GNU_MIRROR}/libidn/${BPN}-${PV}.tar.gz \
           file://libidn_fix_for_automake-1.12.patch \
           file://avoid_AM_PROG_MKDIR_P_warning_error_with_automake_1.12.patch \
           file://dont-depend-on-help2man.patch \
"

SRC_URI[md5sum] = "7533d14fbbb6c026a1a9eaa2179ccb69"
SRC_URI[sha256sum] = "0a2f4c71c80f8f389a99d5a26539a9be4a4ac42cd7f375aa41046660f63cc53c"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+ or LGPLv3
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = " --disable-tld"

do_install_append() {
	rm -rf ${D}${libdir}/Libidn.dll
	rm -rf ${D}${datadir}/emacs
}
