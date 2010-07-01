DESCRIPTION = "A file-synchronization tool"
HOMEPAGE = "http://rsync.samba.org/"
BUGTRACKER = "http://rsync.samba.org/bugzilla.html"
SECTION = "console/network"
PRIORITY = "optional"

# GPLv2+ (<< 3.0.0), GPLv3+ (>= 3.0.0)
LICENSE = "GPLv2+"

SRC_URI = "http://rsync.samba.org/ftp/rsync/src/rsync-${PV}.tar.gz \
           file://rsyncd.conf"

inherit autotools

do_install_append() {
	install -d ${D}/etc
	install -m 0644 ${WORKDIR}/rsyncd.conf ${D}/etc
}

EXTRA_OEMAKE='STRIP=""'
