require busybox.inc
PR = "2"

SRC_URI = "\
  http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
  file://udhcpscript.patch;patch=1 \
  file://udhcpc-fix-nfsroot.patch;patch=1 \
  file://B921600.patch;patch=1 \
  file://get_header_tar.patch;patch=1 \
  file://busybox-appletlib-dependency.patch;patch=1 \
  file://0000-wget-no-check-certificate.patch;patch=1 \
  file://run-parts.in.usr-bin.patch;patch=1 \
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
  file://defconfig \
"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"