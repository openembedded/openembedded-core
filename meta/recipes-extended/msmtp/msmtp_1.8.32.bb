SUMMARY = "msmtp is an SMTP client"
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "https://marlam.de/msmtp/"
SECTION = "console/network"

LICENSE = "GPL-3.0-only"
DEPENDS = "zlib gnutls"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

UPSTREAM_CHECK_URI = "https://marlam.de/msmtp/download/"

SRC_URI = "https://marlam.de/${BPN}/releases/${BP}.tar.xz"
SRC_URI[sha256sum] = "20cd58b58dd007acf7b937fa1a1e21f3afb3e9ef5bbcfb8b4f5650deadc64db4"

inherit gettext autotools update-alternatives pkgconfig

EXTRA_OECONF += "--without-libsecret --without-libgsasl --without-libidn"

ALTERNATIVE:${PN} = "sendmail"
ALTERNATIVE_TARGET[sendmail] = "${bindir}/msmtp"
ALTERNATIVE_LINK_NAME[sendmail] = "${sbindir}/sendmail"
ALTERNATIVE_PRIORITY = "100"
