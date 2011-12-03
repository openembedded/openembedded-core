require busybox.inc
PR = "r1"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://udhcpscript.patch \
           file://udhcpc-fix-nfsroot.patch \
           file://B921600.patch \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://busybox-1.19.3-getty.patch \
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

SRC_URI[tarball.md5sum] = "c3938e1ac59602387009bbf1dd1af7f6"
SRC_URI[tarball.sha256sum] = "a1a9a35732c719ef384f02b6e357c324d8be25bc154af91a48c4264b1e6038f0"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y"
