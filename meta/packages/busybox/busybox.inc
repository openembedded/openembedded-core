DESCRIPTION = "BusyBox combines tiny versions of many common UNIX utilities into a single \
small executable. It provides minimalist replacements for most of the \
utilities you usually find in GNU fileutils, shellutils, etc. The utilities \
in BusyBox generally have fewer options than their full-featured GNU \
cousins; however, the options that are included provide the expected \
functionality and behave very much like their GNU counterparts. BusyBox \
provides a fairly complete POSIX environment for any small or embedded \
system."
HOMEPAGE = "http://www.busybox.net"
LICENSE = "GPL"
SECTION = "base"
PRIORITY = "required"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.gz \
           file://busybox-cron \
	   file://busybox-httpd \
	   file://busybox-udhcpd \
	   file://default.script \
	   file://dhcp-hostname.patch;patch=1 \
           file://hwclock.sh \
	   file://ifupdown-spurious-environ.patch;patch=1 \
	   file://mount.busybox \
	   file://syslog \
	   file://syslog.conf \
	   file://udhcpscript.patch;patch=1 \
	   file://umount.busybox"

SRC_URI_append_nylon = " file://xargs-double-size.patch;patch=1"

export EXTRA_CFLAGS = "${CFLAGS}"
EXTRA_OEMAKE_append = " CROSS=${HOST_PREFIX}"
PACKAGES =+ "${PN}-httpd ${PN}-udhcpd"

FILES_${PN}-httpd = "${sysconfdir}/init.d/busybox-httpd /srv/www"
FILES_${PN}-udhcpd = "${sysconfdir}/init.d/busybox-udhcpd"

FILES_${PN} += " ${datadir}/udhcpc"

INITSCRIPT_PACKAGES = "${PN} ${PN}-httpd ${PN}-udhcpd"
INITSCRIPT_NAME_${PN}-httpd = "busybox-httpd"
INITSCRIPT_NAME_${PN}-udhcpd = "busybox-udhcpd" 
INITSCRIPT_NAME_${PN} = "syslog"
CONFFILES_${PN} = "${sysconfdir}/syslog.conf"

# This disables the syslog startup links in slugos (see slugos-init)
INITSCRIPT_PARAMS_${PN}_slugos = "start 20 ."

inherit cml1 update-rc.d

do_compile () {
	unset CFLAGS
	base_do_compile
}

pkg_postinst_${PN} () {
	# If we are not making an image we create links for the utilities that doesn't exist
	# so the update-alternatives script will get the utilities it needs
	# (update-alternatives have no problem replacing links later anyway)
	test -n 2> /dev/null || alias test='busybox test'
	if test "x$D" = "x"; then while read link; do if test ! -h "$link"; then case "$link" in /*/*/*) to="../../bin/busybox";; /bin/*) to="busybox";; /*/*) to="../bin/busybox";; esac; busybox ln -s $to $link; fi; done </etc/busybox.links; fi
	
	# This adds the links, remember that this has to work when building an image too, hence the $D
	while read link; do case "$link" in /*/*/*) to="../../bin/busybox";; /bin/*) to="busybox";; /*/*) to="../bin/busybox";; esac; bn=`basename $link`; update-alternatives --install $link $bn $to 50; done <$D/etc/busybox.links
}
