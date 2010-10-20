require busybox.inc
PR = "r2"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://udhcpscript.patch \
           file://udhcpc-fix-nfsroot.patch \
           file://B921600.patch \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://run-parts.in.usr-bin.patch \
           file://find-touchscreen.sh \
           file://busybox-cron \
           file://busybox-httpd \
           file://busybox-udhcpd \
           file://default.script \
           file://simple.script \
           file://hwclock.sh \
           file://mount.busybox \
           file://syslog \
           file://syslog.conf \
           file://umount.busybox \
           file://defconfig"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"
