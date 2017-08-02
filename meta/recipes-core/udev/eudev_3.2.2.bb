SUMMARY = "eudev is a fork of systemd's udev"
HOMEPAGE = "https://wiki.gentoo.org/wiki/Eudev"
LICENSE = "GPLv2.0+ & LGPL-2.1+"
LICENSE_libudev = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0 glib-2.0-native gperf-native kmod libxslt-native util-linux"

PROVIDES = "udev"

SRC_URI = "https://github.com/gentoo/${BPN}/archive/v${PV}.tar.gz;downloadfilename=${BP}.tar.gz \
           file://0014-Revert-rules-remove-firmware-loading-rules.patch \
           file://Revert-udev-remove-userspace-firmware-loading-suppor.patch \
           file://devfs-udev.rules \
           file://init \
           file://links.conf \
           file://local.rules \
           file://permissions.rules \
           file://run.rules \
           file://udev.rules \
"
UPSTREAM_CHECK_URI = "https://github.com/gentoo/eudev/releases"

SRC_URI[md5sum] = "1a0faad3a88ade3475e8c0ddef78e30b"
SRC_URI[sha256sum] = "80628191448886846ab9cc0e2af6782358a0afb81d5b4f8cd9d982d2626cca31"

inherit autotools update-rc.d qemu pkgconfig

EXTRA_OECONF = " \
    --sbindir=${base_sbindir} \
    --with-rootlibdir=${base_libdir} \
    --with-rootprefix= \
"

PACKAGECONFIG ??= "hwdb"
PACKAGECONFIG[hwdb] = "--enable-hwdb,--disable-hwdb"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
	sed -i s%@UDEVD@%${base_sbindir}/udevd% ${D}${sysconfdir}/init.d/udev

	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/local.rules ${D}${sysconfdir}/udev/rules.d/local.rules

	# Use classic network interface naming scheme
	touch ${D}${sysconfdir}/udev/rules.d/80-net-name-slot.rules

	# hid2hci has moved to bluez4. removed in udev as of version 169
	rm -f ${D}${base_libdir}/udev/hid2hci
}

INITSCRIPT_NAME = "udev"
INITSCRIPT_PARAMS = "start 04 S ."

PACKAGES =+ "libudev"
PACKAGES =+ "eudev-hwdb"


FILES_${PN} += "${libexecdir} ${base_libdir}/udev ${bindir}/udevadm"
FILES_${PN}-dev = "${datadir}/pkgconfig/udev.pc \
                   ${includedir}/libudev.h ${libdir}/libudev.so \
                   ${includedir}/udev.h ${libdir}/libudev.la \
                   ${libdir}/libudev.a ${libdir}/pkgconfig/libudev.pc"
FILES_libudev = "${base_libdir}/libudev.so.*"
FILES_eudev-hwdb = "${sysconfdir}/udev/hwdb.d"

RDEPENDS_eudev-hwdb += "eudev"

RPROVIDES_${PN} = "hotplug udev"
RPROVIDES_eudev-hwdb += "udev-hwdb"

python () {
    if bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' in DISTRO_FEATURES")
}

PACKAGE_WRITE_DEPS += "qemu-native"
pkg_postinst_eudev-hwdb () {
    if test -n "$D"; then
        ${@qemu_run_binary(d, '$D', '${bindir}/udevadm')} hwdb --update --root $D
        chown root:root $D${sysconfdir}/udev/hwdb.bin
    else
        udevadm hwdb --update
    fi
}

pkg_prerm_eudev-hwdb () {
        rm -f $D${sysconfdir}/udev/hwdb.bin
}
