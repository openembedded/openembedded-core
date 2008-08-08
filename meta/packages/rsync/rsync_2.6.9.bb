LICENSE = "GPL"
DESCRIPTION = "A file-synchronization tool"
SECTION = "console/network"
PRIORITY = "optional"

SRC_URI = "http://rsync.samba.org/ftp/rsync/src/rsync-${PV}.tar.gz \
           file://rsyncd.conf"

inherit autotools

do_install_append() {
	install -d ${D}/etc
	install -m 0644 ${WORKDIR}/rsyncd.conf ${D}/etc
}

EXTRA_OEMAKE='STRIP=""'
