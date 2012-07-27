DESCRIPTION = "A console URL download utility featuring HTTP, FTP, and more."
SECTION = "console/network"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "openssl"

INC_PR = "r15"

inherit autotools gettext update-alternatives

EXTRA_OECONF = "--with-libc --enable-ipv6 --with-libssl-prefix=${STAGING_DIR_HOST} --with-ssl=openssl --disable-rpath"

ALTERNATIVE_${PN} = "wget"
ALTERNATIVE_PRIORITY = "100"
