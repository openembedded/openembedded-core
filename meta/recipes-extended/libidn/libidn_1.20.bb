SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "LGPLv2.1+ & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://lib/idna.h;endline=21;md5=dbd4426bbc38846d5a6b94c3570fd756 \
                    file://src/idn.c;endline=20;md5=e7bc77cab53f7367b7e381aaa546e76c"
PR = "r0"

inherit pkgconfig autotools gettext

SRC_URI = "${GNU_MIRROR}/libidn/${P}.tar.gz"

SRC_URI[md5sum] = "03996d474a696e59fdddbee87fc9519c"
SRC_URI[sha256sum] = "e1bff472e8dce11064508ef5acbd6ac72371d0028f15c65f4a91ed157bfca8b1"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = " --disable-tld"
