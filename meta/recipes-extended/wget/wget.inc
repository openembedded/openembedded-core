SUMMARY = "Console URL download utility supporting HTTP, FTP, etc"
SECTION = "console/network"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "openssl zlib libpcre"

INC_PR = "r16"

inherit autotools gettext update-alternatives

EXTRA_OECONF = "--enable-ipv6 --with-libssl-prefix=${STAGING_DIR_HOST} \
                --with-ssl=openssl --disable-rpath --disable-iri \
                ac_cv_header_uuid_uuid_h=no"

ALTERNATIVE_${PN} = "wget"
ALTERNATIVE_PRIORITY = "100"
