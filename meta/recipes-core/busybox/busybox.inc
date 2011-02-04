SUMMARY = "Tiny versions of many common UNIX utilities in a single small executable."
DESCRIPTION = "BusyBox combines tiny versions of many common UNIX utilities into a single small executable. It provides minimalist replacements for most of the utilities you usually find in GNU fileutils, shellutils, etc. The utilities in BusyBox generally have fewer options than their full-featured GNU cousins; however, the options that are included provide the expected functionality and behave very much like their GNU counterparts. BusyBox provides a fairly complete POSIX environment for any small or embedded system."
HOMEPAGE = "http://www.busybox.net"
BUGTRACKER = "https://bugs.busybox.net/"

# bzip2 applet in busybox is based on lightly-modified bzip2 source
# the GPL is version 2 only
LICENSE = "GPLv2 & bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=de10de48642ab74318e893a61105afbb"

SECTION = "base"
PRIORITY = "required"

export EXTRA_CFLAGS = "${CFLAGS}"
PACKAGES =+ "${PN}-httpd ${PN}-udhcpd"

FILES_${PN}-httpd = "${sysconfdir}/init.d/busybox-httpd /srv/www"
FILES_${PN}-udhcpd = "${sysconfdir}/init.d/busybox-udhcpd"

FILES_${PN} += "${datadir}/udhcpc"
FILES_${PN} += "${sysconfdir}/init.d/busybox-udhcpc"

INITSCRIPT_PACKAGES = "${PN} ${PN}-httpd ${PN}-udhcpd"
INITSCRIPT_NAME_${PN}-httpd = "busybox-httpd"
INITSCRIPT_NAME_${PN}-udhcpd = "busybox-udhcpd" 
INITSCRIPT_NAME_${PN} = "syslog busybox-udhcpc"
CONFFILES_${PN} = "${sysconfdir}/syslog.conf.${PN}"

# This disables the syslog startup links in slugos (see slugos-init)
INITSCRIPT_PARAMS_${PN}_slugos = "start 20 ."

inherit cml1 update-rc.d

do_configure () {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config
	cml1_do_configure
}

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	base_do_compile
}

do_install () {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	install -d ${D}${sysconfdir}/init.d
	oe_runmake "PREFIX=${D}" install
	cp -pPR ${S}/_install/* ${D}/

	# Move everything to /busybox (not supposed to end up in any package)
	install -d ${D}/busybox
	ls ${D} -R

	cp -dPr ${D}${base_bindir} ${D}${base_sbindir} ${D}${prefix} ${D}/busybox/
	# Move the busybox binary back to /bin
	install -d ${D}${base_bindir}
	mv ${D}/busybox${base_bindir}/busybox ${D}${base_bindir}/
	# Move back the sh symlink
	test -h ${D}/busybox${base_bindir}/sh && mv ${D}/busybox${base_bindir}/sh ${D}${base_bindir}/

	install -m 0755 ${WORKDIR}/syslog ${D}${sysconfdir}/init.d/syslog.${PN}
	install -m 644 ${WORKDIR}/syslog.conf ${D}${sysconfdir}/syslog.conf.${PN}
	if grep "CONFIG_CROND=y" ${WORKDIR}/defconfig; then
		# Move crond back to /usr/sbin/crond
		install -d ${D}${sbindir}
		mv ${D}/busybox${sbindir}/crond ${D}${sbindir}/

		install -m 0755 ${WORKDIR}/busybox-cron ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HTTPD=y" ${WORKDIR}/defconfig; then
		# Move httpd back to /usr/sbin/httpd
		install -d ${D}${sbindir}
		mv ${D}/busybox${sbindir}/httpd ${D}${sbindir}/

		install -m 0755 ${WORKDIR}/busybox-httpd ${D}${sysconfdir}/init.d/
		install -d ${D}/srv/www
	fi
	if grep "CONFIG_UDHCPD=y" ${WORKDIR}/defconfig; then
		# Move udhcpd back to /usr/sbin/udhcpd
		install -d ${D}${sbindir}
		mv ${D}/busybox${sbindir}/udhcpd ${D}${sbindir}/

		install -m 0755 ${WORKDIR}/busybox-udhcpd ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HWCLOCK=y" ${WORKDIR}/defconfig; then
		# Move hwclock back to /sbin/hwclock
		install -d ${D}${base_sbindir}
		mv ${D}/busybox${base_sbindir}/hwclock ${D}${base_sbindir}/

		install -m 0755 ${WORKDIR}/hwclock.sh ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_UDHCPC=y" ${WORKDIR}/defconfig; then
		# Move dhcpc back to /usr/sbin/udhcpc
		install -d ${D}${base_sbindir}
		mv ${D}/busybox${base_sbindir}/udhcpc ${D}${base_sbindir}/

		install -d ${D}${sysconfdir}/udhcpc.d
		install -d ${D}${datadir}/udhcpc
		install -m 0755 ${S}/examples/udhcp/simple.script ${D}${sysconfdir}/udhcpc.d/50default
		install -m 0755 ${WORKDIR}/default.script ${D}${datadir}/udhcpc/default.script
		install -m 0755 ${WORKDIR}/busybox-udhcpc ${D}${sysconfdir}/init.d/
	fi

	install -m 0644 ${S}/busybox.links ${D}${sysconfdir}

	# remove the temp dir to suppress warning, we are done with it
	rm -rf ${D}/busybox
}

pkg_postinst_${PN} () {
	# If we are not making an image we create links for the utilities that doesn't exist
	# so the update-alternatives script will get the utilities it needs
	# (update-alternatives have no problem replacing links later anyway)
	test -n 2> /dev/null || alias test='busybox test'
	if test "x$D" = "x"; then while read link; do if test ! -h "$link"; then case "$link" in /*/*/*) to="../../bin/busybox";; /bin/*) to="busybox";; /*/*) to="../bin/busybox";; esac; busybox ln -s $to $link; fi; done </etc/busybox.links; fi
	
	# This adds the links, remember that this has to work when building an image too, hence the $D
	while read link; do case "$link" in /*/*/*) to="../../bin/busybox";; /bin/*) to="busybox";; /*/*) to="../bin/busybox";; esac; bn=`basename $link`; update-alternatives --install $link $bn $to 50; done <$D/etc/busybox.links

	update-alternatives --install ${sysconfdir}/init.d/syslog syslog-init syslog.${PN} 50
	update-alternatives --install ${sysconfdir}/syslog.conf syslog-conf syslog.conf.${PN} 50
}

pkg_prerm_${PN} () {
	# This is so you can make busybox commit suicide - removing busybox with no other packages
	# providing its files, this will make update-alternatives work, but the update-rc.d part
	# for syslog, httpd and/or udhcpd will fail if there is no other package providing sh
	tmpdir=`mktemp -d /tmp/busyboxrm-XXXXXX`
	ln -s /bin/busybox $tmpdir/[
	ln -s /bin/busybox $tmpdir/test
	ln -s /bin/busybox $tmpdir/head
	ln -s /bin/busybox $tmpdir/sh
	ln -s /bin/busybox $tmpdir/basename
	ln -s /bin/busybox $tmpdir/echo
	ln -s /bin/busybox $tmpdir/mv
	ln -s /bin/busybox $tmpdir/ln
	ln -s /bin/busybox $tmpdir/dirname
	ln -s /bin/busybox $tmpdir/rm
	ln -s /bin/busybox $tmpdir/sed
	ln -s /bin/busybox $tmpdir/sort
	export PATH=$PATH:$tmpdir

	while read link
	do
		case "$link" in
			/*/*/*) to="../../bin/busybox";;
			/bin/*) to="busybox";;
			/*/*) to="../bin/busybox";;
		esac
		bn=`basename $link`
		sh /usr/bin/update-alternatives --remove $bn $to
	done </etc/busybox.links

	# remove syslog
	if test "x$D" = "x"; then
		if test "$1" = "upgrade" -o "$1" = "remove"; then
			/etc/init.d/syslog stop
		fi
	fi

	update-alternatives --remove syslog-init syslog.${PN}
	update-alternatives --remove syslog-conf syslog.conf.${PN}
}

