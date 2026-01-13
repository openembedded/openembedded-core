SUMMARY = "Enhances systemd compatilibity with existing SysVinit scripts"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/systemd"
LICENSE = "MIT"

DEPENDS = "systemd-systemctl-native"
INHIBIT_DEFAULT_DEPS = "1"

S = "${UNPACKDIR}"

inherit features_check

REQUIRED_DISTRO_FEATURES = "systemd"

SYSTEMD_DISABLED_SYSV_SERVICES = " \
  busybox-udhcpc \
  hwclock \
  networking \
  nfsserver \
  nfscommon \
  syslog.busybox \
"

do_install() {
	for unit in ${SYSTEMD_DISABLED_SYSV_SERVICES} ; do
		systemctl --root ${D} mask $unit
	done
}
