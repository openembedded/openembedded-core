SUMMARY = "eudev is a fork of systemd's udev"
HOMEPAGE = "https://github.com/eudev-project/eudev"
DESCRIPTION = "eudev is Gentoo's fork of udev, systemd's device file manager for the Linux kernel. It manages device nodes in /dev and handles all user space actions when adding or removing devices."
LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later"
LICENSE:libudev = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0 glib-2.0-native gperf-native kmod util-linux"

PROVIDES = "udev"

SRC_URI = "https://github.com/eudev-project/${BPN}/releases/download/v${PV}/${BP}.tar.gz \
           file://init \
           file://local.rules \
"

SRC_URI[sha256sum] = "19847cafec67897da855fde56f9dc7d92e21c50e450aa79068a7e704ed44558b"

UPSTREAM_CHECK_URI = "https://github.com/eudev-project/eudev/releases"
UPSTREAM_CHECK_REGEX = "eudev-(?P<pver>\d+(\.\d+)+)\.tar"

inherit autotools update-rc.d qemu pkgconfig features_check manpages

CONFLICT_DISTRO_FEATURES = "systemd"

EXTRA_OECONF = " \
    --sbindir=${base_sbindir} \
    --with-rootlibdir=${base_libdir} \
    --with-rootlibexecdir=${nonarch_base_libdir}/udev \
    --with-rootprefix= \
"

PACKAGECONFIG ?= "hwdb \
                  ${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)} \
"
PACKAGECONFIG[hwdb] = "--enable-hwdb,--disable-hwdb"
PACKAGECONFIG[manpages] = "--enable-manpages,--disable-manpages"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux"

do_install:append() {
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

do_install:prepend:class-target () {
	# Remove references to buildmachine
	sed -i -e 's:${RECIPE_SYSROOT_NATIVE}::g' \
		${B}/src/udev/keyboard-keys-from-name.h
}

INITSCRIPT_NAME = "udev"
INITSCRIPT_PARAMS = "start 04 S ."

PACKAGES =+ "libudev"
PACKAGES =+ "eudev-hwdb"

FILES:${PN} += "${libexecdir} ${nonarch_base_libdir}/udev ${bindir}/udevadm"
FILES:${PN}-dev = "${datadir}/pkgconfig/udev.pc \
                   ${includedir}/libudev.h ${libdir}/libudev.so \
                   ${includedir}/udev.h ${libdir}/libudev.la \
                   ${libdir}/libudev.a ${libdir}/pkgconfig/libudev.pc"
FILES:libudev = "${base_libdir}/libudev.so.*"
FILES:eudev-hwdb = "${sysconfdir}/udev/hwdb.d"

RDEPENDS:eudev-hwdb += "eudev"

RPROVIDES:${PN} = "hotplug udev"
RPROVIDES:eudev-hwdb += "udev-hwdb"

PACKAGE_WRITE_DEPS += "qemu-native"
pkg_postinst:eudev-hwdb () {
    if test -n "$D"; then
        $INTERCEPT_DIR/postinst_intercept update_udev_hwdb ${PKG} mlprefix=${MLPREFIX} binprefix=${MLPREFIX}
    else
        udevadm hwdb --update
    fi
}

pkg_prerm:eudev-hwdb () {
        rm -f $D${sysconfdir}/udev/hwdb.bin
}
