SUMMARY = "Tiny versions of many common UNIX utilities in a single small executable."
DESCRIPTION = "BusyBox combines tiny versions of many common UNIX utilities into a single small executable. It provides minimalist replacements for most of the utilities you usually find in GNU fileutils, shellutils, etc. The utilities in BusyBox generally have fewer options than their full-featured GNU cousins; however, the options that are included provide the expected functionality and behave very much like their GNU counterparts. BusyBox provides a fairly complete POSIX environment for any small or embedded system."
HOMEPAGE = "http://www.busybox.net"
BUGTRACKER = "https://bugs.busybox.net/"

DEPENDS += "kern-tools-native"

# bzip2 applet in busybox is based on lightly-modified bzip2 source
# the GPL is version 2 only
LICENSE = "GPLv2 & bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=de10de48642ab74318e893a61105afbb"

SECTION = "base"

export EXTRA_CFLAGS = "${CFLAGS}"
export EXTRA_LDFLAGS = "${LDFLAGS}"

PACKAGES =+ "${PN}-httpd ${PN}-udhcpd ${PN}-udhcpc ${PN}-syslog ${PN}-mdev ${PN}-hwclock"

FILES_${PN}-httpd = "${sysconfdir}/init.d/busybox-httpd /srv/www"
FILES_${PN}-syslog = "${sysconfdir}/init.d/syslog* ${sysconfdir}/syslog-startup.conf* ${systemd_unitdir}/system/syslog.service"
FILES_${PN}-mdev = "${sysconfdir}/init.d/mdev ${sysconfdir}/mdev.conf"
FILES_${PN}-udhcpd = "${sysconfdir}/init.d/busybox-udhcpd"
FILES_${PN}-udhcpc = "${sysconfdir}/udhcpc.d ${datadir}/udhcpc"
FILES_${PN}-hwclock = "${sysconfdir}/init.d/hwclock.sh"

INITSCRIPT_PACKAGES = "${PN}-httpd ${PN}-syslog ${PN}-udhcpd ${PN}-mdev ${PN}-hwclock"

INITSCRIPT_NAME_${PN}-httpd = "busybox-httpd"
INITSCRIPT_NAME_${PN}-hwclock = "hwclock.sh"
INITSCRIPT_NAME_${PN}-mdev = "mdev"
INITSCRIPT_PARAMS_${PN}-mdev = "start 06 S ."
INITSCRIPT_NAME_${PN}-syslog = "syslog"
INITSCRIPT_NAME_${PN}-udhcpd = "busybox-udhcpd" 

SYSTEMD_PACKAGES = "${PN}-syslog"
SYSTEMD_SERVICE_${PN}-syslog = "busybox-syslog.service"

CONFFILES_${PN}-syslog = "${sysconfdir}/syslog-startup.conf.${BPN}"
CONFFILES_${PN}-mdev = "${sysconfdir}/mdev.conf"

RRECOMMENDS_${PN} = "${PN}-syslog ${PN}-udhcpc"

inherit cml1 systemd update-rc.d

# internal helper
def busybox_cfg(feature, features, tokens, cnf, rem):
	if type(tokens) == type(""):
		tokens = [tokens]
	rem.extend(['/^[# ]*' + token + '[ =]/d' for token in tokens])
	if type(features) == type([]) and feature in features:
		cnf.extend([token + '=y' for token in tokens])
	else:
		cnf.extend(['# ' + token + ' is not set' for token in tokens])

# Map distro features to config settings
def features_to_busybox_settings(d):
	cnf, rem = ([], [])
	distro_features = d.getVar('DISTRO_FEATURES', True).split()
	busybox_cfg('ipv6', distro_features, 'CONFIG_FEATURE_IPV6', cnf, rem)
	busybox_cfg('largefile', distro_features, 'CONFIG_LFS', cnf, rem)
	busybox_cfg('largefile', distro_features, 'CONFIG_FDISK_SUPPORT_LARGE_DISKS', cnf, rem)
	busybox_cfg('nls',  distro_features, 'CONFIG_LOCALE_SUPPORT', cnf, rem)
	busybox_cfg('ipv4', distro_features, 'CONFIG_FEATURE_IFUPDOWN_IPV4', cnf, rem)
	busybox_cfg('ipv6', distro_features, 'CONFIG_FEATURE_IFUPDOWN_IPV6', cnf, rem)
	busybox_cfg('wifi', distro_features, 'CONFIG_RFKILL', cnf, rem)
	busybox_cfg('bluetooth', distro_features, 'CONFIG_RFKILL', cnf, rem)
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
DO_IPv4 := "${@base_contains('DISTRO_FEATURES', 'ipv4', 1, 0, d)}"
DO_IPv6 := "${@base_contains('DISTRO_FEATURES', 'ipv6', 1, 0, d)}"

python () {
  if "${OE_DEL}":
    d.setVar('configmangle_append', "${OE_DEL}" + "\n")
  if "${OE_FEATURES}":
    d.setVar('configmangle_append',
                   "/^### DISTRO FEATURES$/a\\\n%s\n\n" %
                   ("\\n".join((d.expand("${OE_FEATURES}").split("\n")))))
  d.setVar('configmangle_append',
                 "/^### CROSS$/a\\\n%s\n" %
                  ("\\n".join(["CONFIG_CROSS_COMPILER_PREFIX=\"${TARGET_PREFIX}\"",
			       "CONFIG_EXTRA_CFLAGS=\"${CFLAGS}\" \"${HOST_CC_ARCH}\""
                        ])
                  ))
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

# returns all the elements from the src uri that are .cfg files
def find_cfgs(d):
    sources=src_patches(d, True)
    sources_list=[]
    for s in sources:
        if s.endswith('.cfg'):
            sources_list.append(s)

    return sources_list

do_configure () {
	do_prepare_config
	merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
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

	if ! grep -q "CONFIG_FEATURE_INDIVIDUAL=y" ${B}/.config; then
		# Install /bin/busybox, and the /bin/sh link so the postinst script
		# can run. Let update-alternatives handle the rest.
		install -d ${D}${base_bindir}
		if grep -q "CONFIG_FEATURE_SUID=y" ${B}/.config; then
			install -m 4755 ${B}/busybox ${D}${base_bindir}
		else
			install -m 0755 ${B}/busybox ${D}${base_bindir}
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

	if grep -q "CONFIG_SYSLOGD=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/syslog ${D}${sysconfdir}/init.d/syslog.${BPN}
		install -m 644 ${WORKDIR}/syslog-startup.conf ${D}${sysconfdir}/syslog-startup.conf.${BPN}
	fi
	if grep "CONFIG_CROND=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/busybox-cron ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HTTPD=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/busybox-httpd ${D}${sysconfdir}/init.d/
		install -d ${D}/srv/www
	fi
	if grep "CONFIG_UDHCPD=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/busybox-udhcpd ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_HWCLOCK=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/hwclock.sh ${D}${sysconfdir}/init.d/
	fi
	if grep "CONFIG_UDHCPC=y" ${B}/.config; then
		install -d ${D}${sysconfdir}/udhcpc.d
		install -d ${D}${datadir}/udhcpc
                install -m 0755 ${WORKDIR}/simple.script ${D}${sysconfdir}/udhcpc.d/50default
		install -m 0755 ${WORKDIR}/default.script ${D}${datadir}/udhcpc/default.script
	fi
	if grep "CONFIG_INETD=y" ${B}/.config; then
		install -m 0755 ${WORKDIR}/inetd ${D}${sysconfdir}/init.d/inetd.${BPN}
		sed -i "s:/usr/sbin/:${sbindir}/:" ${D}${sysconfdir}/init.d/inetd.${BPN}
		install -m 0644 ${WORKDIR}/inetd.conf ${D}${sysconfdir}/
	fi
        if grep "CONFIG_MDEV=y" ${B}/.config; then
               install -m 0755 ${WORKDIR}/mdev ${D}${sysconfdir}/init.d/mdev
               if grep "CONFIG_FEATURE_MDEV_CONF=y" ${B}/.config; then
                       install -m 644 ${WORKDIR}/mdev.conf ${D}${sysconfdir}/mdev.conf
               fi
	fi
	install -m 0644 ${S}/busybox.links ${D}${sysconfdir}

    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        sed 's,@base_sbindir@,${base_sbindir},g' < ${WORKDIR}/busybox-syslog.service.in \
            > ${D}${systemd_unitdir}/system/busybox-syslog.service
        sed 's,@base_sbindir@,${base_sbindir},g' < ${WORKDIR}/busybox-klogd.service.in \
            > ${D}${systemd_unitdir}/system/busybox-klogd.service

        ln -sf /dev/null ${D}${systemd_unitdir}/system/syslog.service
    fi
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "50"

ALTERNATIVE_${PN}-syslog = "syslog-init syslog-startup-conf"

ALTERNATIVE_LINK_NAME[syslog-init] = "${sysconfdir}/init.d/syslog"
ALTERNATIVE_TARGET[syslog-init] = "${sysconfdir}/init.d/syslog.${BPN}"

ALTERNATIVE_LINK_NAME[syslog-startup-conf] = "${sysconfdir}/syslog-startup.conf"
ALTERNATIVE_TARGET[syslog-startup-conf] = "${sysconfdir}/syslog-startup.conf.${BPN}"

ALTERNATIVE_TARGET = "/bin/busybox"

python do_package_prepend () {
    # We need to load the full set of busybox provides from the /etc/busybox.links
    # Use this to see the update-alternatives with the right information

    dvar = d.getVar('D', True)
    pn = d.getVar('PN', True)
    f = open('%s/etc/busybox.links' % (dvar), 'r')

    for alt_link_name in f:
        alt_link_name = alt_link_name.strip()
        alt_name = os.path.basename(alt_link_name)

        # Match coreutils
        if alt_name == '[':
            alt_name = 'lbracket'

        d.appendVar('ALTERNATIVE_%s' % (pn), ' ' + alt_name)
        d.setVarFlag('ALTERNATIVE_LINK_NAME', alt_name, alt_link_name)
}

pkg_postinst_${PN} () {
	# If we are not making an image we create links for the utilities that doesn't exist
	# so the update-alternatives script will get the utilities it needs
	# (update-alternatives have no problem replacing links later anyway)
	test -n 2> /dev/null || alias test='busybox test'
	if test "x$D" = "x"; then while read link; do if test ! -h "$link"; then case "$link" in /*/*/*) to="../../bin/busybox";; /bin/*) to="busybox";; /*/*) to="../bin/busybox";; esac; busybox ln -s $to $link; fi; done </etc/busybox.links; fi
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
	ln -s /bin/busybox $tmpdir/grep
	export PATH=$PATH:$tmpdir
}

pkg_prerm_${PN}-syslog () {
	# remove syslog
	if test "x$D" = "x"; then
		if test "$1" = "upgrade" -o "$1" = "remove"; then
			/etc/init.d/syslog stop
		fi
	fi
}
