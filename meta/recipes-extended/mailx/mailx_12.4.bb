SUMMARY = "mailx is the traditional command-line-mode mail user agent."

DESCRIPTION = "Mailx is derived from Berkeley Mail and is intended provide the \
functionality of the POSIX mailx command with additional support \
for MIME, IMAP, POP3, SMTP, and S/MIME."

HOMEPAGE = "http://heirloom.sourceforge.net/mailx.html"
SECTION = "console/network"
PRIORITY = "required"
PR = "r0"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=4202a0a62910cf94f7af8a3436a2a2dd"

SRC_URI = "${DEBIAN_MIRROR}/main/h/heirloom-mailx/heirloom-mailx_${PV}.orig.tar.gz;name=archive \
           ${DEBIAN_MIRROR}/main/h/heirloom-mailx/heirloom-mailx_${PV}-2.diff.gz;name=patch \
          "
SRC_URI[archive.md5sum] = "17b8ff86795a118b199e041b66b7d1aa"
SRC_URI[archive.sha256sum] = "946d822cbff70df2ecf5b78c8347fdd01fdc5873f7a7cf55932b3e07030fa370"
SRC_URI[patch.md5sum] = "3233d1a85fcb4dcde689132a1bba7fc9"
SRC_URI[patch.sha256sum] = "b15fac77973b7d787469e51bbbeae7ddbe922b02a0c23208b9779ce837eba861"

S = "${WORKDIR}/mailx-${PV}"

inherit autotools

CFLAGS_append = " -D_BSD_SOURCE -DDEBIAN -I${S}/EXT"

PARALLEL_MAKE = ""
