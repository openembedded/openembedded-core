require busybox.inc

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://busybox-udhcpc-no_deconfig.patch \
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
           file://syslog.conf \
           file://busybox-syslog.default \
           file://mdev \
           file://mdev.conf \
           file://mdev-mount.sh \
           file://umount.busybox \
           file://defconfig \
           file://busybox-syslog.service.in \
           file://busybox-klogd.service.in \
           file://fail_on_no_media.patch \
           file://run-ptest \
           file://inetd.conf \
           file://inetd \
           file://login-utilities.cfg \
           file://recognize_connmand.patch \
           file://busybox-cross-menuconfig.patch \
"

SRC_URI[tarball.md5sum] = "5c94d6301a964cd91619bd4d74605245"
SRC_URI[tarball.sha256sum] = "300f1db0a7ca4ecee8f8d8027aba250b903372e8339b7d9123d37c1e900473bf"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y"

do_install_ptest () {
        cp -r ${B}/testsuite ${D}${PTEST_PATH}/
        cp ${B}/.config      ${D}${PTEST_PATH}/
        ln -s /bin/busybox   ${D}${PTEST_PATH}/busybox
}
