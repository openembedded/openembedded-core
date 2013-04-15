require busybox.inc
PR = "r7"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://B921600.patch \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://run-parts.in.usr-bin.patch \
           file://watch.in.usr-bin.patch \
           file://busybox-udhcpc-no_deconfig.patch \
           file://sys_resource.patch \
           file://wget_dl_dir_fix.patch \
           file://find-touchscreen.sh \
           file://busybox-cron \
           file://busybox-httpd \
           file://busybox-udhcpd \
           file://default.script \
           file://simple.script \
           file://hwclock.sh \
           file://mount.busybox \
           file://syslog \
           file://syslog-startup.conf \
           file://mdev \
           file://mdev.conf \
           file://umount.busybox \
           file://defconfig \
           file://busybox-mkfs-minix-tests_bigendian.patch \
           file://fix-for-spurious-testsuite-failure.patch \
           file://busybox-1.20.2-kernel_ver.patch \
           file://stat-usr-bin.patch \
           file://busybox-syslog.service.in \
           file://busybox-klogd.service.in \
           file://testsuite-du-du-k-works-fix-false-positive.patch \
           file://strict-atime.patch \
           file://fail_on_no_media.patch \
           file://inetd.conf \
           file://inetd"

SRC_URI[tarball.md5sum] = "e025414bc6cd79579cc7a32a45d3ae1c"
SRC_URI[tarball.sha256sum] = "eb13ff01dae5618ead2ef6f92ba879e9e0390f9583bd545d8789d27cf39b6882"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y"
