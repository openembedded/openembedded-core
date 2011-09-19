SUMMARY = "Tiny versions of many common UNIX utilities in a single small executable."
DESCRIPTION = "BusyBox combines tiny versions of many common UNIX utilities into a single small executable. It provides minimalist replacements for most of the utilities you usually find in GNU fileutils, shellutils, etc. The utilities in BusyBox generally have fewer options than their full-featured GNU cousins; however, the options that are included provide the expected functionality and behave very much like their GNU counterparts. BusyBox provides a fairly complete POSIX environment for any small or embedded system."
HOMEPAGE = "http://www.busybox.net"
BUGTRACKER = "https://bugs.busybox.net/"

# bzip2 applet in busybox is based on lightly-modified bzip2 source
# the GPL is version 2 only
LICENSE = "GPLv2 & bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=de10de48642ab74318e893a61105afbb"

SECTION = "base"

export EXTRA_CFLAGS = "${CFLAGS}"
export EXTRA_LDFLAGS = "${LDFLAGS}"

PACKAGES =+ "${PN}-httpd ${PN}-udhcpd ${PN}-udhcpc ${PN}-syslog ${PN}-mdev"

FILES_${PN}-httpd = "${sysconfdir}/init.d/busybox-httpd /srv/www"
FILES_${PN}-syslog = "${sysconfdir}/init.d/syslog* ${sysconfdir}/syslog.conf*"
FILES_${PN}-mdev = "${sysconfdir}/init.d/mdev ${sysconfdir}/mdev.conf"
FILES_${PN}-udhcpd = "${sysconfdir}/init.d/busybox-udhcpd"
FILES_${PN}-udhcpc = "${sysconfdir}/udhcpc.d ${datadir}/udhcpc ${sysconfdir}/init.d/busybox-udhcpc"

INITSCRIPT_PACKAGES = "${PN}-httpd ${PN}-syslog ${PN}-udhcpd ${PN}-udhcpc ${PN}-mdev"

INITSCRIPT_NAME_${PN}-httpd = "busybox-httpd"
INITSCRIPT_NAME_${PN}-syslog = "syslog"
INITSCRIPT_NAME_${PN}-mdev = "mdev"
INITSCRIPT_PARAMS_${PN}-mdev = "start 06 S ."
INITSCRIPT_NAME_${PN}-udhcpd = "busybox-udhcpd" 
INITSCRIPT_NAME_${PN}-udhcpc = "busybox-udhcpc" 
CONFFILES_${PN}-syslog = "${sysconfdir}/syslog.conf.${BPN}"
CONFFILES_${PN}-mdev = "${sysconfdir}/mdev.conf"

RRECOMMENDS_${PN} = "${PN}-syslog ${PN}-udhcpc"

inherit cml1 update-rc.d

# internal helper
def busybox_cfg(feature, features, tokens, cnf, rem):
	if type(tokens) == type(""):
		tokens = [tokens]
	rem.extend(['/^[# ]*' + token + '[ =]/d' for token in tokens])
	if type(features) == type([]) and feature in features:
		cnf.extend([token + '=y' for token in tokens])
	else:
		cnf.extend(['# ' + token + ' is not set' for token in tokens])

# Map distro and machine features to config settings
def features_to_busybox_settings(d):
	cnf, rem = ([], [])
	distro_features = bb.data.getVar('DISTRO_FEATURES', d, True).split()
	machine_features = bb.data.getVar('MACHINE_FEATURES', d, True).split()
	busybox_cfg('ipv6', distro_features, 'CONFIG_FEATURE_IPV6', cnf, rem)
	busybox_cfg('largefile', distro_features, 'CONFIG_LFS', cnf, rem)
	busybox_cfg('largefile', distro_features, 'CONFIG_FDISK_SUPPORT_LARGE_DISKS', cnf, rem)
	busybox_cfg('nls',  distro_features, 'CONFIG_LOCALE_SUPPORT', cnf, rem)
	busybox_cfg('ipv4', distro_features, 'CONFIG_FEATURE_IFUPDOWN_IPV4', cnf, rem)
	busybox_cfg('ipv6', distro_features, 'CONFIG_FEATURE_IFUPDOWN_IPV6', cnf, rem)
	busybox_cfg('kernel24', machine_features, 'CONFIG_FEATURE_2_4_MODULES', cnf, rem)
	return "\n".join(cnf), "\n".join(rem)

# X, Y = ${@features_to_uclibc_settings(d)}
# unfortunately doesn't seem to work with bitbake, workaround:
def features_to_busybox_conf(d):
	cnf, rem = features_to_busybox_settings(d)
	return cnf
def features_to_busybox_del(d):
	cnf, rem = features_to_busybox_settings(d)
	return rem

configmangle = '/CROSS_COMPILER_PREFIX/d; \
		/CONFIG_EXTRA_CFLAGS/d; \
		'
OE_FEATURES := "${@features_to_busybox_conf(d)}"
OE_DEL      := "${@features_to_busybox_del(d)}"
DO_IPv4 := ${@base_contains('DISTRO_FEATURES', 'ipv4', 1, 0, d)}
DO_IPv6 := ${@base_contains('DISTRO_FEATURES', 'ipv6', 1, 0, d)}

python () {
  if "${OE_DEL}":
    bb.data.setVar('configmangle_append', "${OE_DEL}" + "\n", d)
  if "${OE_FEATURES}":
    bb.data.setVar('configmangle_append',
                   "/^### DISTRO FEATURES$/a\\\n%s\n\n" %
                   ("\\n".join((bb.data.expand("${OE_FEATURES}", d).split("\n")))),
                   d)
  bb.data.setVar('configmangle_append',
                 "/^### CROSS$/a\\\n%s\n" %
                  ("\\n".join(["CONFIG_CROSS_COMPILER_PREFIX=\"${TARGET_PREFIX}\"",
			       "CONFIG_EXTRA_CFLAGS=\"${CFLAGS}\""
                        ])
                  ),
                 d)
}

do_prepare_config () {
	sed -e 's#@DATADIR@#${datadir}#g' \
		< ${WORKDIR}/defconfig > ${S}/.config
	sed -i -e '/CONFIG_STATIC/d' .config
	echo "# CONFIG_STATIC is not set" >> .config
	for i in 'CROSS' 'DISTRO FEATURES'; do echo "### $i"; done >> \
		${S}/.config
	sed -i -e '${configmangle}' ${S}/.config
	if test ${DO_IPv4} -eq 0 && test ${DO_IPv6} -eq 0; then
	  # disable networking applets
	  mv ${S}/.config ${S}/.config.oe-tmp
	  awk 'BEGIN{net=0}
	  /^# Networking Utilities/{net=1}
	  /^#$/{if(net){net=net+1}}
	  {if(net==2&&$0 !~ /^#/&&$1){print("# "$1" is not set")}else{print}}' \
		  ${S}/.config.oe-tmp > ${S}/.config
	fi
}

do_configure () {
	do_prepare_config
	cml1_do_configure
}

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake busybox_unstripped
	cp busybox_unstripped busybox
}

do_install () {
	oe_runmake busybox.links
	if [ "${prefix}" != "/usr" ]; then
		sed "s:^/usr/:${prefix}/:" busybox.links > busybox.links.new
		mv busybox.links.new busybox.links
	fi
	if [ "${base_sbindir}" != "/sbin" ]; then
		sed "s:^/sbin/:${base_sbindir}/:" busybox.links > busybox.links.new
		mv busybox.links.new busybox.links
	fi

	install -d ${D}${sysconfdir}/init.d

	if ! grep -q "CONFIG_FEATURE_INDIVIDUAL=y" ${WORKDIR}/defconfig; then
		# Install /bin/busybox, and the /bin/sh link so the postinst script
		# can run. Let update-alternatives handle the rest.
		install -d ${D}${base_bindir}
		if grep -q "CONFIG_FEATURE_SUID=y" ${WORKDIR}/defconfig; then
			install -m 4755 ${S}/busybox ${D}${base_bindir}
		else
			install -m 0755 ${S}/busybox ${D}${base_bindir}
		fi
		ln -sf busybox ${D}${base_bindir}/sh
	else
		install -d ${D}${base_bindir} ${D}${base_sbindir}
		install -d ${D}${libdir} ${D}${bindir} ${D}${sbindir}
		cat busybox.links | while read FILE; do
			NAME=`basename "$FILE"`
			install -m 0755 "0_lib/$NAME" "${D}$FILE.${BPN}"
		done
		# add suid bit where needed
		for i in `grep -E "APPLET.*_BB_SUID_((MAYBE|REQUIRE))" include/applets.h | grep -v _BB_SUID_DROP | cut -f 3 -d '(' | cut -f 1 -d ','`; do
			find ${D} -name $i.${BPN} -exec chmod a+s {} \;
		done
		install -m 0755 0_lib/libbusybox.so.${PV} ${D}${libdir}/libbusybox.so.${PV}
		ln -sf sh.${BPN} ${D}${base_bindir}/sh
		ln -sf ln.${BPN} ${D}${base_bindir}/ln
		ln -sf test.${BPN} ${D}${bindir}/test
		if [ -f ${D}/linuxrc.${BPN} ]; then
			mv ${D}/linuxrc.${BPN} ${D}/linuxrc
		fi
	fi

	if grep -q "CONFIG_SYSLOGD=y" ${WORKDIR}/defconfig; then
		install -m 0755 ${WORKDIR}/syslog ${D}${sysconfdir}/init.d/syslog.${BPN}
		install -m 644 ${WORKDIR}/syslog.conf ${D}${sysconfdir}/syslog.conf.${BPN}
	fi
	if grep "CONFIG_CROND=y" ${WORKDIR}/defconfig; then
		install -m 0755 ${WORKDIR}/busybox-cron ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HTTPD=y" ${WORKDIR}/defconfig; then
		install -m 0755 ${WORKDIR}/busybox-httpd ${D}${sysconfdir}/init.d/
		install -d ${D}/srv/www
	fi
	if grep "CONFIG_UDHCPD=y" ${WORKDIR}/defconfig; then
		install -m 0755 ${WORKDIR}/busybox-udhcpd ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HWCLOCK=y" ${WORKDIR}/defconfig; then
		install -d ${D}${sysconfdir}/default
		install -m 0755 ${WORKDIR}/hwclock.sh ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_UDHCPC=y" ${WORKDIR}/defconfig; then
		install -d ${D}${sysconfdir}/udhcpc.d
		install -d ${D}${datadir}/udhcpc
                install -m 0755 ${WORKDIR}/simple.script ${D}${sysconfdir}/udhcpc.d/50default
		install -m 0755 ${WORKDIR}/default.script ${D}${datadir}/udhcpc/default.script
		install -m 0755 ${WORKDIR}/busybox-udhcpc ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_INETD=y" ${WORKDIR}/defconfig; then
		install -m 0755 ${WORKDIR}/inetd ${D}${sysconfdir}/init.d/inetd.${BPN}
		install -m 0644 ${WORKDIR}/inetd.conf ${D}${sysconfdir}/
	fi
        if grep "CONFIG_MDEV=y" ${WORKDIR}/defconfig; then
               install -m 0755 ${WORKDIR}/mdev ${D}${sysconfdir}/init.d/mdev
               if grep "CONFIG_FEATURE_MDEV_CONF=y" ${WORKDIR}/defconfig; then
                       install -m 644 ${WORKDIR}/mdev.conf ${D}${sysconfdir}/mdev.conf
               fi
	fi
	install -m 0644 ${S}/busybox.links ${D}${sysconfdir}
}

python package_do_filedeps_append () {
	# We need to load the full set of busybox provides from the /etc/busybox.links
	# The pkg_postinst_ is what creates the actual links

	pkg = d.getVar('PN', True)
	f_busybox = "/bin/busybox"
	f_busybox_links = "/etc/busybox.links"

	requires_files = []
	provides_files = []

	# Load/backup original set
	filerprovides = d.getVar('FILERPROVIDES_%s_%s' % (f_busybox, pkg), True) or ""

	dep_pipe = os.popen('sed -e "s,^,Provides: ," %s/%s%s' % (pkgdest, pkg, f_busybox_links))

	process_deps(dep_pipe, pkg, "%s/%s%s" % (pkgdest, pkg, f_busybox), provides_files, requires_files)

	# Add the new set
	filerprovides += d.getVar('FILERPROVIDES_%s_%s' % (f_busybox, pkg), True) or ""

	# Make sure there is an entry for this item in the FILERPROVIDESFLIST...
	filerprovidesflist = (d.getVar('FILERPROVIDESFLIST_%s' % pkg, True) or "").split()
	for file in provides_files:
		if file not in filerprovidesflist:
			filerprovidesflist.append(file)
	d.setVar('FILERPROVIDESFLIST_%s' % pkg, " ".join(filerprovidesflist))

	# Store the new provides
	d.setVar('FILERPROVIDES_%s_%s' % (f_busybox, pkg), filerprovides)
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

pkg_postinst_${PN}-syslog () {
	update-alternatives --install ${sysconfdir}/init.d/syslog syslog-init syslog.${BPN} 50
	update-alternatives --install ${sysconfdir}/syslog.conf syslog-conf syslog.conf.${BPN} 50
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
}

pkg_prerm_${PN}-syslog () {
	# remove syslog
	if test "x$D" = "x"; then
		if test "$1" = "upgrade" -o "$1" = "remove"; then
			/etc/init.d/syslog stop
		fi
	fi

	update-alternatives --remove syslog-init syslog.${BPN}
	update-alternatives --remove syslog-conf syslog.conf.${BPN}
}
