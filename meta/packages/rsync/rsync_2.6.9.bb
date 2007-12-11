LICENSE = "GPL"
DESCRIPTION = "A file-synchronization tool"
SECTION = "console/network"
PRIORITY = "optional"
PR = "r0"

SRC_URI = "http://rsync.samba.org/ftp/rsync/rsync-${PV}.tar.gz \
           file://rsyncd.conf"

inherit autotools

do_install_append() {
	install -d ${D}/etc
	install -m 0644 ${WORKDIR}/rsyncd.conf ${D}/etc
}

EXTRA_OEMAKE='STRIP=""'
