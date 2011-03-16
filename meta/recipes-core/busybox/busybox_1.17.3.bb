require busybox.inc
PR = "r2"

SRC_URI = "http://www.busybox.net/downloads/busybox-${PV}.tar.bz2;name=tarball \
           file://udhcpscript.patch \
           file://udhcpc-fix-nfsroot.patch \
           file://B921600.patch \
           file://get_header_tar.patch \
           file://busybox-appletlib-dependency.patch \
           file://run-parts.in.usr-bin.patch \
           file://make-382-fix.patch \
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
           file://umount.busybox \
           file://defconfig"

SRC_URI[tarball.md5sum] = "a2ce1a951571da8c6e0eaf75b1acef60"
SRC_URI[tarball.sha256sum] = "de2f0274f61a068d75ad33861e0982e99c6b625681460ce420222371c3511ff2"

EXTRA_OEMAKE += "V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"
