SUMMARY = "General purpose cryptographic library based on the code from GnuPG"
HOMEPAGE = "http://directory.fsf.org/project/libgcrypt/"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"
SECTION = "libs"

# helper program gcryptrnd and getrandom are under GPL, rest LGPL
LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff"

DEPENDS = "libgpg-error libcap"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgcrypt/libgcrypt-${PV}.tar.gz \
           file://add-pkgconfig-support.patch \
           file://libgcrypt-fix-building-error-with-O2-in-sysroot-path.patch \
           file://fix-ICE-failure-on-mips-with-option-O-and-g.patch \
"

BINCONFIG = "${bindir}/libgcrypt-config"

inherit autotools-brokensep texinfo binconfig-disabled pkgconfig

EXTRA_OECONF = "--disable-asm --with-capabilities"

do_configure_prepend () {
	# Else this could be used in preference to the one in aclocal-copy
	rm -f ${S}/m4/gpg-error.m4
}

# libgcrypt.pc is added locally and thus installed here
do_install_append() {
	install -d ${D}/${libdir}/pkgconfig
	install -m 0644 ${B}/src/libgcrypt.pc ${D}/${libdir}/pkgconfig/
}

FILES_${PN}-dev += "${bindir}/dumpsexp ${bindir}/hmac256"

ARM_INSTRUCTION_SET = "arm"

BBCLASSEXTEND = "native"
