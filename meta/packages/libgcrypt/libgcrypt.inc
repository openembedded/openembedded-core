DESCRIPTION = "A general purpose cryptographic library based on the code from GnuPG"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL LGPL FDL"
DEPENDS = "libgpg-error"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/libgcrypt/libgcrypt-${PV}.tar.gz \
           file://add-pkgconfig-support.patch;patch=1"

inherit autotools_stage binconfig pkgconfig

EXTRA_OECONF = "--without-pth --disable-asm --with-capabilities"

ARM_INSTRUCTION_SET = "arm"

# move libgcrypt-config into -dev package
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir} ${libdir}/pkgconfig/*.pc"
