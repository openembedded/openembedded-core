SUMMARY = "msmtp is an SMTP client"
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "http://msmtp.sourceforge.net/"
SECTION = "console/network"

LICENSE = "GPLv3"
DEPENDS = "zlib gnutls"

#COPYING or Licence
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "http://sourceforge.net/projects/msmtp/files/msmtp/${PV}/${BPN}-${PV}.tar.bz2 \
          "

SRC_URI[md5sum] = "792ac0ffa30dc95ea1889a548128186d"
SRC_URI[sha256sum] = "247af9a95fa22f506e85109fc4268a5d91ca03af9b17eebcc1e46b7cd64c225f"

inherit gettext autotools update-alternatives

EXTRA_OECONF += "--without-gnome-keyring --without-libidn"

ALTERNATIVE_${PN} = "sendmail"
ALTERNATIVE_TARGET[sendmail] = "${bindir}/msmtp"
ALTERNATIVE_LINK_NAME[sendmail] = "${sbindir}/sendmail"
ALTERNATIVE_PRIORITY = "100"
