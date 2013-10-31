require busybox.inc
PR = "r0"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://run-parts.in.usr-bin.patch \
           file://watch.in.usr-bin.patch \
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
           file://umount.busybox \
           file://defconfig \
           file://stat-usr-bin.patch \
           file://busybox-syslog.service.in \
           file://busybox-klogd.service.in \
           file://testsuite-du-du-k-works-fix-false-positive.patch \
           file://fail_on_no_media.patch \
           file://busybox-sulogin-empty-root-password.patch \
           file://run-ptest \
           file://inetd.conf \
           file://inetd \
           file://login-utilities.cfg \
           file://busybox-list-suid-and-non-suid-app-configs.patch \
           file://busybox-sed-fix-sed-clusternewline-testcase.patch \
"

SRC_URI[tarball.md5sum] = "795394f83903b5eec6567d51eebb417e"
SRC_URI[tarball.sha256sum] = "cd5be0912ec856110ae12c76c3ec9cd5cba1df45b5a9da2b095b8284d1481303"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y"

do_install_ptest () {
        cp -r ${B}/testsuite ${D}${PTEST_PATH}/
        cp ${B}/.config      ${D}${PTEST_PATH}/
        ln -s /bin/busybox   ${D}${PTEST_PATH}/busybox
}
