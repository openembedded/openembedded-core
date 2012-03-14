DESCRIPTION = "Rotates, compresses, removes and mails system log files"
SECTION = "console/utils"
HOMEPAGE = "https://fedorahosted.org/releases/l/o/logrotate"
LICENSE = "GPLv2"
PR = "r0"

DEPENDS="coreutils popt"

LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

SRC_URI = "https://fedorahosted.org/releases/l/o/logrotate/logrotate-${PV}.tar.gz \
           file://act-as-mv-when-rotate.patch \
           file://disable-check-different-filesystems.patch \
           file://update-the-manual.patch \
            "

SRC_URI[md5sum] = "bd2e20d8dc644291b08f9215397d28a5"
SRC_URI[sha256sum] = "c12471e70ae8bc923bd5c4f25e8fd6483b68c6301f3cd79f7cfe37bc5b370169"


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
