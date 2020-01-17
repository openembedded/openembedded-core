SUMMARY = "System Log Daemons"
DESCRIPTION = "The sysklogd package implements two system log daemons: syslogd, klogd"
HOMEPAGE = "http://www.infodrom.org/projects/sysklogd/"
SECTION = "base"

LICENSE = "GPLv2+ & BSD"
LICENSE_syslogd = "BSD"
LICENSE_klogd = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://src/syslogd.c;beginline=2;endline=15;md5=a880fecbc04503f071c494a9c0dd4f97 \
                    file://src/klogd.c;beginline=2;endline=19;md5=4f5591d04cccbeb0352758ed4a9d7213 \
                   "

inherit update-rc.d update-alternatives systemd autotools

SRC_URI = "git://github.com/troglobit/sysklogd.git;nobranch=1 \
           file://0001-Remove-__BEGIN_DECLS-__END_DECLS.patch \
           file://0002-include-sys-types.h-for-off_t.patch \
           file://0001-Drop-libcompat-to-simplify-build-deps-and-really-fix.patch \
           file://sysklogd \
           "
S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-systemd=${systemd_system_unitdir} --with-klogd --without-logger"

do_install_append () {
       install -d ${D}${sysconfdir}
       install -m 644 ${S}/syslog.conf ${D}${sysconfdir}/syslog.conf
       install -d ${D}${sysconfdir}/init.d
       install -m 755 ${WORKDIR}/sysklogd ${D}${sysconfdir}/init.d/syslog
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "syslogd.service klogd.service"
SYSTEMD_AUTO_ENABLE = "enable"

INITSCRIPT_NAME = "syslog"
CONFFILES_${PN} = "${sysconfdir}/syslog.conf"
RCONFLICTS_${PN} = "rsyslog busybox-syslog syslog-ng"

FILES_${PN} += "${@bb.utils.contains('DISTRO_FEATURES','systemd','${exec_prefix}/lib/tmpfiles.d/sysklogd.conf', '', d)}"

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN}-doc = "syslogd.8"
ALTERNATIVE_LINK_NAME[syslogd.8] = "${mandir}/man8/syslogd.8"

pkg_prerm_${PN} () {
	if test "x$D" = "x"; then
	if test "$1" = "upgrade" -o "$1" = "remove"; then
		/etc/init.d/syslog stop || :
	fi
	fi
}

python () {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}
