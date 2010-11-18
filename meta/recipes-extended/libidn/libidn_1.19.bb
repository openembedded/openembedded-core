SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "LGPLv2.1+ & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LIB;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://lib/idna.h;firstline=6;endline=18;md5=643beb30bf98d8c4aed59a51b86fe9ea \
                    file://src/idn.c;firstline=6;endline=18;md5=07a57d46977b38a2557aac446dda35dd"
PR = "r0"

inherit pkgconfig autotools gettext

SRC_URI = "http://ftp.gnu.org/gnu/libidn/${P}.tar.gz"

SRC_URI[md5sum] = "a45142126d28162014c995f969bdb5a8"
SRC_URI[sha256sum] = "8ed2e936b1ae3e30b45b54ca3672deaa83ee0f4d20db5ad83dc1af7222d39f41"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = " --disable-tld"
