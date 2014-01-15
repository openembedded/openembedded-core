SUMMARY = "Enhances systemd compatilibity with existing SysVinit scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

PR = "r18"

DEPENDS = "systemd-systemctl-native"

inherit allarch

SRC_URI = "file://*.service"

do_install() {
	install -d ${D}${systemd_unitdir}/system/basic.target.wants
	install -d ${D}${systemd_unitdir}/system/sysinit.target.wants/
	sed -i -e 's,@POSTINSTALL_INITPOSITION@,${POSTINSTALL_INITPOSITION},g' \
			${WORKDIR}/run-postinsts.service
	install -m 0644 ${WORKDIR}/run-postinsts.service ${D}${systemd_unitdir}/system
	ln -sf ../run-postinsts.service ${D}${systemd_unitdir}/system/basic.target.wants/
	ln -sf ../run-postinsts.service ${D}${systemd_unitdir}/system/sysinit.target.wants/
}

SYSTEMD_DISABLED_SYSV_SERVICES = " \
  busybox-udhcpc \
  hwclock \
  networking \
  syslog.busybox \
  dbus-1 \
"

pkg_postinst_${PN} () {
	cd $D${sysconfdir}/init.d

	echo "Disabling the following sysv scripts: "

	OPTS=""

	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi

	for i in ${SYSTEMD_DISABLED_SYSV_SERVICES} ; do
		if [ \( -e $i -o $i.sh \) -a ! \( -e $D${sysconfdir}/systemd/system/$i.service -o  -e $D${systemd_unitdir}/system/$i.service \) ] ; then
			echo -n "$i: " ; systemctl ${OPTS} mask $i.service
		fi
	done ; echo
}

FILES_${PN} = "${systemd_unitdir}/system ${bindir}"
RDPEPENDS_${PN} = "systemd"

# Define a variable to allow distros to run configure earlier.
# (for example, to enable loading of ethernet kernel modules before networking starts)
# note: modifying name or default value for POSTINSTALL_INITPOSITION requires
# changes in opkg.inc
POSTINSTALL_INITPOSITION ?= "98"
