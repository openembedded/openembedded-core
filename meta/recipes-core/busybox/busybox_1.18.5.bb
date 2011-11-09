require busybox.inc
PR = "r2"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://udhcpscript.patch \
           file://udhcpc-fix-nfsroot.patch \
           file://B921600.patch \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://run-parts.in.usr-bin.patch \
           file://busybox-udhcpc-no_deconfig.patch \
           file://find-touchscreen.sh \
           file://busybox-cron \
           file://busybox-httpd \
           file://busybox-udhcpd \
           file://busybox-udhcpc \
           file://default.script \
           file://simple.script \
           file://hwclock.sh \
           file://mount.busybox \
           file://syslog \
           file://syslog.conf \
           file://mdev \
           file://mdev.conf \
           file://umount.busybox \
           file://defconfig"

SRC_URI[tarball.md5sum] = "96dd43cc7cee4017a6bf31b7da82a1f5"
SRC_URI[tarball.sha256sum] = "10954fcd5c48d8a262a3497b16227bf983a05658bf2bf661af2fdeca773f2fc0"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y"
