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
CONFFILES_${PN} = "${sysconfdir}/syslog.conf.${BPN}"

CFLAGS_append = " -DSYSV"

do_install () {
	install -d ${D}${mandir}/man8 \
			${D}${mandir}/man5 \
			${D}${base_sbindir}
	oe_runmake 'BINDIR=${D}${base_sbindir}' \
			'MANDIR=${D}${mandir}' install
	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/syslog.conf ${D}${sysconfdir}/syslog.conf
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/sysklogd ${D}${sysconfdir}/init.d/syslog
}

pkg_preinst_${PN} () {
	# all this is needed to avoid sysmlink errors,
	# because update-rc.d runs before pkg_postinst
	ETC=$D${sysconfdir}

	if [ -e $ETC/init.d/syslog -a ! -L $ETC/init.d/syslog ]; then
		echo "WARNING:" "non symlink ${sysconfdir}/init.d/syslog exist -> backup to ${sysconfdir}/init.d/syslog.old"
		mv $ETC/init.d/syslog $ETC/init.d/syslog.old
	fi
	if [ ! -e $ETC/init.d/syslog ]; then
		ln -s dummy $ETC/init.d/syslog
	fi
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN} = "syslogd klogd syslog-init syslog-conf"

ALTERNATIVE_LINK_NAME[syslogd] = "${base_sbindir}/syslogd"
ALTERNATIVE_LINK_NAME[klogd] = "${base_sbindir}/klogd"
ALTERNATIVE_LINK_NAME[syslog-init] = "${sysconfdir}/init.d/syslog"
ALTERNATIVE_LINK_NAME[syslog-conf] = "${sysconfdir}/syslog.conf"

pkg_postinst_${PN} () {
	if test "x$D" != "x"; then
	OPT="-r $D"
	else
	OPT="-s"
	fi
	update-rc.d $OPT syslog defaults
}

pkg_prerm_${PN} () {
	if test "x$D" = "x"; then
	if test "$1" = "upgrade" -o "$1" = "remove"; then
		/etc/init.d/syslog stop
	fi
	fi
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
