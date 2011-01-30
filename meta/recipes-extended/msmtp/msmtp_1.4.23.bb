SUMMARY = "msmtp is an SMTP client."
DESCRIPTION = "A sendmail replacement for use in MTAs like mutt"
HOMEPAGE = "http://msmtp.sourceforge.net/"
SECTION = "console/network"

PRIORITY = "required"
LICENSE = "GPLv3"
DEPENDS = "zlib gnutls"
PR = "r0"


#COPYING or Licence
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "http://sourceforge.net/projects/msmtp/files/msmtp/${PV}/${PN}-${PV}.tar.bz2 \
          "

SRC_URI[md5sum] = "5fb7ae88186624cdb125d3efad3fdc16"
SRC_URI[sha256sum] = "269cd30eeb867167c6a599e23399f4fc24196fcdef3bac5b120d806b3b421810"

inherit gettext autotools
