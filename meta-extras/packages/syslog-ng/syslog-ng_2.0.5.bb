DESCRIPTION = "Alternative system logger daemon"
DEPENDS = "libol flex eventlog"
PR = "r1"

SRC_URI = "http://www.balabit.com/downloads/files/syslog-ng/sources/stable/src/${P}.tar.gz \
          file://syslog-ng.conf \
          file://initscript"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools update-rc.d

EXTRA_OECONF = "--with-libol=${STAGING_BINDIR_CROSS}/ --enable-dynamic-linking"

do_install_append() {
        install -d ${D}/${sysconfdir}/${PN}
        install ${WORKDIR}/syslog-ng.conf ${D}${sysconfdir}/syslog-ng.conf
        install -d ${D}/${sysconfdir}/init.d
        install -m 755 ${WORKDIR}/initscript ${D}/${sysconfdir}/init.d/syslog-ng
}

pkg_postinst() {
        update-rc.d -f syslog remove
}

pkg_postrm() {
        update-rc.d syslog add 5
}

CONFFILES_${PN} = "${sysconfdir}/syslog-ng.conf"

INITSCRIPT_NAME = "syslog-ng"
#INITSCRIPT_PARAMS = "defaults 05"
INITSCRIPT_PARAMS = "remove"
