SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "(LGPLv2.1+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3231f57e8fce0f62f81b03107e788888 \
                    file://COPYING.LESSERv2;md5=4fbd65380cdd255951079008b364516c \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYINGv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://lib/idna.h;endline=21;md5=f73089b7f52dd2cb3540e274bd76106d \
                    file://src/idn.c;endline=20;md5=d7797e6cc3a7b48e6050fc0d27104595"

inherit pkgconfig autotools gettext

SRC_URI = "${GNU_MIRROR}/libidn/${BPN}-${PV}.tar.gz \
           file://libidn_fix_for_automake-1.12.patch \
           file://avoid_AM_PROG_MKDIR_P_warning_error_with_automake_1.12.patch \
           file://dont-depend-on-help2man.patch \
"

SRC_URI[md5sum] = "43a6f14b16559e10a492acc65c4b0acc"
SRC_URI[sha256sum] = "dd357a968449abc97c7e5fa088a4a384de57cb36564f9d4e0d898ecc6373abfb"

# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+ or LGPLv3
# so package command into a separate package
PACKAGES =+ "idn"
FILES_idn = "${bindir}/*"

EXTRA_OECONF = "--disable-csharp"

do_install_append() {
	rm -rf ${D}${datadir}/emacs
}
