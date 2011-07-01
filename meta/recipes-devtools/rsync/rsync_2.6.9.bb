DESCRIPTION = "A file-synchronization tool"
HOMEPAGE = "http://rsync.samba.org/"
BUGTRACKER = "http://rsync.samba.org/bugzilla.html"
SECTION = "console/network"

# needs to add acl and attr
DEPENDS = "popt"

SRC_URI = "http://rsync.samba.org/ftp/rsync/src/rsync-${PV}.tar.gz \
           file://rsync-2.6.9-fname-obo.patch \
           file://rsyncd.conf"

inherit autotools

do_install_append() {
	install -d ${D}/etc
	install -m 0644 ${WORKDIR}/rsyncd.conf ${D}/etc
}

EXTRA_OEMAKE='STRIP=""'

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=6d5a9d4c4d3af25cd68fd83e8a8cb09c"

PR = "r3"
