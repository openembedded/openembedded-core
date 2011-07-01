SUMMARY = "msmtp is an SMTP client."
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "http://msmtp.sourceforge.net/"
SECTION = "console/network"

LICENSE = "GPLv3"
DEPENDS = "zlib gnutls"
PR = "r2"


#COPYING or Licence
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "http://sourceforge.net/projects/msmtp/files/msmtp/${PV}/${BPN}-${PV}.tar.bz2 \
          "

SRC_URI[md5sum] = "3ed704fbd3e7419cab5c65bb7928d9ba"
SRC_URI[sha256sum] = "f19f3fcc67da448420b3adbd5add09f8fe110664dd64f3c2cd693ef0cb736887"

inherit gettext autotools update-alternatives

EXTRA_OECONF += "--without-gnome-keyring"

ALTERNATIVE_NAME = "sendmail"
ALTERNATIVE_PATH = "${bindir}/msmtp"
ALTERNATIVE_LINK = "${sbindir}/sendmail"
ALTERNATIVE_PRIORITY = "100"
