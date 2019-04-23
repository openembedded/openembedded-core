SUMMARY = "msmtp is an SMTP client"
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "https://marlam.de/msmtp/"
SECTION = "console/network"

LICENSE = "GPLv3"
DEPENDS = "zlib gnutls"

#COPYING or Licence
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "https://marlam.de/${BPN}/releases/${BP}.tar.xz"

UPSTREAM_CHECK_URI = "https://marlam.de/msmtp/download/"

SRC_URI[md5sum] = "0c0be51e6872e818f78bd6924636327b"
SRC_URI[sha256sum] = "3cb2eefd33d048f0f82de100ef39a494e44fd1485e376ead31f733d2f36b92b4"

inherit gettext autotools update-alternatives pkgconfig

EXTRA_OECONF += "--without-libsecret --without-libgsasl --without-libidn"

ALTERNATIVE_${PN} = "sendmail"
ALTERNATIVE_TARGET[sendmail] = "${bindir}/msmtp"
ALTERNATIVE_LINK_NAME[sendmail] = "${sbindir}/sendmail"
ALTERNATIVE_PRIORITY = "100"

pkg_postinst_${PN}_linuxstdbase () {
	# /usr/lib/sendmial is required by LSB core test
	[ ! -L $D/usr/lib/sendmail ] && ln -sf ${sbindir}/sendmail $D/usr/lib/
}
