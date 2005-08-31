SRC_URI = "http://kernel.org/pub/linux/utils/kernel/hotplug/udev-${PV}.tar.gz \
           file://tmpfs.patch;patch=1 \
           file://noasmlinkage.patch;patch=1 \
           file://flags.patch;patch=1 \
           file://tty-symlinks.patch;patch=1 \
           file://init"

include udev.inc

PR = "r0"
UDEV_EXTRAS = "extras/scsi_id/ extras/volume_id/ extras/run_directory/"

#FIXME UDEV MIGRATION PLAN:
#FIXME      a) udevd is now a netlink daemon and needs to be started by the init script (ours is way too old)
#FIXME      b) sbin/hotplug should no longer be called by the kernel, i.e. echo "" >/proc/sys/kernel/hotplug
#FIXME done c) until d) happens, udev will emulate hotplugd behaviour (see do_install_append()
#FIXME      d) eventually hotplug should no longer be used at all, all agents shall be converted to udev rules

do_install_append() {
	install -m 0755 extras/run_directory/udev_run_hotplugd ${D}${sbindir}/
	echo RUN+="/sbin/udev_run_hotplugd" >>${D}${sysconfdir}/udev/rules.d/50-udev.rules
}
