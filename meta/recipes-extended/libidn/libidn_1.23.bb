SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "LGPLv2.1+ & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
                    file://lib/idna.h;endline=21;md5=dbd4426bbc38846d5a6b94c3570fd756 \
                    file://src/idn.c;endline=20;md5=e7bc77cab53f7367b7e381aaa546e76c"
PR = "r0"

inherit pkgconfig autotools gettext

SRC_URI = "${GNU_MIRROR}/libidn/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "f9a417270cc9b6bf763ae1f88e60191c"
SRC_URI[sha256sum] = "25b42d75851ebae52e1c969353b74eefd3d6817f41c8d2a6db258f5ec60c5e6a"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = " --disable-tld"
