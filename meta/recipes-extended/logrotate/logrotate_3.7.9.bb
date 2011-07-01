DESCRIPTION = "Rotates, compresses, removes and mails system log files"
SECTION = "console/utils"
HOMEPAGE = "https://fedorahosted.org/releases/l/o/logrotate"
LICENSE = "GPLv2"
PR = "r0"

DEPENDS="coreutils"

LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

SRC_URI = "https://fedorahosted.org/releases/l/o/logrotate/logrotate-${PV}.tar.gz"

SRC_URI[md5sum] = "eeba9dbca62a9210236f4b83195e4ea5"
SRC_URI[sha256sum] = "080caf904e70e04da16b8dfa95a5a787ec7d722ee1af18ccea437d3ffdd6fec0"


EXTRA_OEMAKE = "CC='${CC}'"

do_install(){
    oe_runmake install DESTDIR=${D} PREFIX=${D} MANDIR=${mandir}
}

do_install_append(){
    mkdir -p ${D}${sysconfdir}/logrotate.d
    mkdir -p ${D}${sysconfdir}/cron.daily
    mkdir -p ${D}${localstatedir}/lib
    install -p -m 644 examples/logrotate-default ${D}${sysconfdir}/logrotate.conf
    install -p -m 755 examples/logrotate.cron ${D}${sysconfdir}/cron.daily/logrotate
    touch ${D}${localstatedir}/lib/logrotate.status
}
