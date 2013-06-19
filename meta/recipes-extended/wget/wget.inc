DESCRIPTION = "A console URL download utility featuring HTTP, FTP, and more."
SECTION = "console/network"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "openssl zlib"

INC_PR = "r16"

inherit autotools gettext update-alternatives

EXTRA_OECONF = "--with-libc --enable-ipv6 --with-libssl-prefix=${STAGING_DIR_HOST} \
                --with-ssl=openssl --disable-rpath --disable-iri"

ALTERNATIVE_${PN} = "wget"
ALTERNATIVE_PRIORITY = "100"
