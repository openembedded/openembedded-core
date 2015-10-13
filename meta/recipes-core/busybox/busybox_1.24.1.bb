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
           file://0001-Use-CC-when-linking-instead-of-LD-and-use-CFLAGS-and.patch \
           file://0002-Passthrough-r-to-linker.patch \
           file://mount-via-label.cfg \
           file://sha1sum.cfg \
           file://sha256sum.cfg \
           file://getopts.cfg \
"

SRC_URI[tarball.md5sum] = "be98a40cadf84ce2d6b05fa41a275c6a"
SRC_URI[tarball.sha256sum] = "37d03132cc078937360b392170b7a1d0e5b322eee9f57c0b82292a8b1f0afe3d"
