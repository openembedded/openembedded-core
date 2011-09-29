SUMMARY = "System Log Daemons"
DESCRIPTION = "The sysklogd package implements two system log daemons: syslogd, klogd"
HOMEPAGE = "http://www.infodrom.org/projects/sysklogd/"
SECTION = "base"

LICENSE = "GPLv2+ & BSD"
LICENSE_syslogd = "BSD"
LICENSE_klogd = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b \
                    file://syslogd.c;beginline=2;endline=15;md5=77ffb2fec48c46d7ca0abb2d5813e7fd \
                    file://klogd.c;beginline=2;endline=19;md5=7e87ed0ae6142de079bce738c10c899d \
                   "

# syslog initscript is handled explicitly because order of
# update-rc.d and update-alternatives is important (see below)
DEPENDS_append = " update-rc.d update-rc.d-native"
RDEPENDS_${PN}_append = " update-rc.d"

SRC_URI = "http://www.infodrom.org/projects/sysklogd/download/sysklogd-${PV}.tar.gz \
           file://no-strip-install.patch \
           file://sysklogd \
           file://syslog.conf \
           "

SRC_URI_append_e500v2 = " file://no-vectorization.patch"

INITSCRIPT_NAME = "syslog"
CONFFILES_${PN} = "${sysconfdir}/syslog.conf.${PN}"

CFLAGS_append = " -DSYSV"

do_install () {
	install -d ${D}${mandir}/man8 \
			${D}${mandir}/man5 \
			${D}${base_sbindir}
	oe_runmake 'BINDIR=${D}${base_sbindir}' \
			'MANDIR=${D}${mandir}' install
	mv ${D}${base_sbindir}/syslogd ${D}${base_sbindir}/syslogd.${PN}
	mv ${D}${base_sbindir}/klogd ${D}${base_sbindir}/klogd.${PN}
	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/syslog.conf ${D}${sysconfdir}/syslog.conf.${PN}
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/sysklogd ${D}${sysconfdir}/init.d/syslog.${PN}
}

pkg_preinst_${PN} () {
	# all this is needed to avoid sysmlink errors,
	# because update-rc.d runs before pkg_postinst
	if [ -e ${sysconfdir}/init.d/syslog -a ! -L ${sysconfdir}/init.d/syslog ]; then
		echo "WARNING:" "non symlink ${sysconfdir}/init.d/syslog exist -> backup to ${sysconfdir}/init.d/syslog.old"
		mv ${sysconfdir}/init.d/syslog ${sysconfdir}/init.d/syslog.old
	fi
	if [ ! -e ${sysconfdir}/init.d/syslog ]; then
		ln -s dummy ${sysconfdir}/init.d/syslog
	fi
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_sbindir}/syslogd syslogd syslogd.${PN} 100
	update-alternatives --install ${base_sbindir}/klogd klogd klogd.${PN} 100
	update-alternatives --install ${sysconfdir}/init.d/syslog syslog-init syslog.${PN} 100
	update-alternatives --install ${sysconfdir}/syslog.conf syslog-conf syslog.conf.${PN} 100

	if test "x$D" != "x"; then
	OPT="-r $D"
	else
	OPT="-s"
	fi
	update-rc.d $OPT syslog defaults
}

pkg_prerm_${PN} () {
	update-alternatives --remove syslogd syslogd.${PN}
	update-alternatives --remove klogd klogd.${PN}

	if test "x$D" = "x"; then
	if test "$1" = "upgrade" -o "$1" = "remove"; then
		/etc/init.d/syslog stop
	fi
	fi
	update-alternatives --remove syslog-init syslog.${PN}
	update-alternatives --remove syslog-conf syslog.conf.${PN}
}

pkg_postrm_${PN} () {
	if test "x$D" != "x"; then
	OPT="-r $D"
	else
	OPT=""
	fi
	if test "$1" = "remove" -o "$1" = "purge"; then
	if ! test -e "/etc/init.d/syslog"; then
	    update-rc.d $OPT syslog remove
	fi
	fi
}
