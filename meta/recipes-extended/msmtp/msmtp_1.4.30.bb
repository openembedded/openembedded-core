SUMMARY = "msmtp is an SMTP client."
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "http://msmtp.sourceforge.net/"
SECTION = "console/network"

LICENSE = "GPLv3"
DEPENDS = "zlib gnutls"
PR = "r0"


#COPYING or Licence
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "http://sourceforge.net/projects/msmtp/files/msmtp/${PV}/${BPN}-${PV}.tar.bz2 \
          "

SRC_URI[md5sum] = "4d32724a2b03f07aa6d4ea9d49367ad3"
SRC_URI[sha256sum] = "f152b9296e36e340eb049c7ee4d6980fcdb29d948e654bdc74bea7ee97409886"

inherit gettext autotools update-alternatives

EXTRA_OECONF += "--without-gnome-keyring --without-libidn"

ALTERNATIVE_${PN} = "sendmail"
ALTERNATIVE_TARGET[sendmail] = "${bindir}/msmtp"
ALTERNATIVE_LINK_NAME[sendmail] = "${sbindir}/sendmail"
ALTERNATIVE_PRIORITY = "100"
