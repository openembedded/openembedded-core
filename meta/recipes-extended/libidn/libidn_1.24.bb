SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "(LGPLv2.1+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c9d9d207a3468a696a03368913d360b \
                    file://COPYING.LESSERv2;md5=4fbd65380cdd255951079008b364516c \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYINGv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://lib/idna.h;endline=21;md5=7364f6a250728ffe16170f5e3ab37512 \
                    file://src/idn.c;endline=20;md5=7d7235e7951ac87d9dfac42e1b69d9cb"
PR = "r0"

inherit pkgconfig autotools gettext

SRC_URI = "${GNU_MIRROR}/libidn/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "764d7258eeb273941680499fab2c7367"
SRC_URI[sha256sum] = "da1708c6063ecb7951a0908e67df3eacdfe128b18edaa6cf6867d7b73b5f35ff"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+ or LGPLv3
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = " --disable-tld"

do_install_append() {
	rm -rf ${D}${datadir}/emacs
}
