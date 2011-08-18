SUMMARY = "mailx is the traditional command-line-mode mail user agent."

DESCRIPTION = "Mailx is derived from Berkeley Mail and is intended provide the \
functionality of the POSIX mailx command with additional support \
for MIME, IMAP, POP3, SMTP, and S/MIME."

HOMEPAGE = "http://heirloom.sourceforge.net/mailx.html"
SECTION = "console/network"
PR = "r1"
LICENSE = "BSD & MPL-1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4202a0a62910cf94f7af8a3436a2a2dd"

SRC_URI = "${DEBIAN_MIRROR}/main/h/heirloom-mailx/heirloom-mailx_${PV}.orig.tar.gz;name=archive \
           ${DEBIAN_MIRROR}/main/h/heirloom-mailx/heirloom-mailx_${PV}-1.diff.gz;name=patch \
          "

SRC_URI[archive.md5sum] = "29a6033ef1412824d02eb9d9213cb1f2"
SRC_URI[archive.sha256sum] = "015ba4209135867f37a0245d22235a392b8bbed956913286b887c2e2a9a421ad"
SRC_URI[patch.md5sum] = "f466281336183be673bf136dd7096662"
SRC_URI[patch.sha256sum] = "aaf2a4bbf145e5ca9cdeb0843091ec8cc01df6c9568c997207a5e97b4dc5ba43"

S = "${WORKDIR}/heirloom-mailx-${PV}"

inherit autotools

CFLAGS_append = " -D_BSD_SOURCE -DDEBIAN -I${S}/EXT"

PARALLEL_MAKE = ""
