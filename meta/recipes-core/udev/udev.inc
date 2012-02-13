SUMMARY = "/dev/ and hotplug management daemon"
DESCRIPTION = "udev is a daemon which dynamically creates and removes device nodes from \
/dev/, handles hotplug events and loads drivers at boot time."
HOMEPAGE = "http://www.kernel.org/pub/linux/utils/kernel/hotplug/udev/udev.html"
LICENSE = "GPLv2.0+ & LGPLv2.1+"
LICENSE_${PN} = "GPLv2.0+"
LICENSE_libudev = "LGPLv2.1+"
LICENSE_libgudev = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://libudev/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://extras/gudev/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

DEPENDS = "acl glib-2.0 libusb usbutils pciutils gperf-native libxslt-native"
RPROVIDES_${PN} = "hotplug"
RRECOMMENDS_${PN} += "udev-extraconf usbutils-ids pciutils-ids"
RDEPENDS_libudev = "${PN} (= ${EXTENDPKGV})"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/hotplug/udev-${PV}.tar.gz \
           file://run.rules \
           file://udev.rules \
           file://devfs-udev.rules \
           file://links.conf \
           file://permissions.rules \
           file://mount.sh \
           file://network.sh \
           file://local.rules \
           file://udev-cache \
           file://udev-cache.default \
           file://init"

inherit autotools pkgconfig update-rc.d

# udevd/udevadm -> /sbin/, libudev.so.* -> /lib/
sbindir = "${base_sbindir}"
libexecdir = "${base_libdir}/udev"
EXTRA_OECONF = "--disable-introspection --with-rootlibdir=${base_libdir} \
                --with-pci-ids-path=${datadir}/pci.ids"

PACKAGES =+ "udev-acl udev-consolekit udev-utils"
PACKAGES =+ "udev-cache libudev libudev-dev libudev-dbg"
PACKAGES =+ "libgudev libgudev-dev libgudev-dbg"

INITSCRIPT_PACKAGES = "udev udev-cache"
INITSCRIPT_NAME_udev = "udev"
INITSCRIPT_PARAMS_udev = "start 03 S ."
INITSCRIPT_NAME_udev-cache = "udev-cache"
INITSCRIPT_PARAMS_udev-cache = "start 36 S ."

FILES_${PN} += "${libexecdir} ${libdir}/ConsoleKit"
RRECOMMENDS_${PN} += "udev-utils"

FILES_${PN}-dbg += "${libexecdir}/.debug"
FILES_${PN}-dev = "${datadir}/pkgconfig/udev.pc"
FILES_libudev = "${base_libdir}/libudev.so.*"
FILES_libudev-dbg = "${base_libdir}/.debug/libudev.so.*"
FILES_libudev-dev = "${includedir}/libudev.h ${libdir}/libudev.so ${libdir}/libudev.la \
                     ${libdir}/libudev.a ${libdir}/pkgconfig/libudev.pc"
FILES_libgudev = "${base_libdir}/libgudev*.so.* ${libdir}/libgudev*.so.*"
FILES_libgudev-dbg = "${base_libdir}/.debug/libgudev*.so.* ${libdir}/.debug/libgudev*.so.*"
FILES_libgudev-dev = "${includedir}/gudev* ${libdir}/libgudev*.so ${libdir}/libgudev*.la \
                     ${libdir}/libgudev*.a ${libdir}/pkgconfig/gudev*.pc"
FILES_udev-cache = "${sysconfdir}/init.d/udev-cache ${sysconfdir}/default/udev-cache"

FILES_udev-acl = "${base_libdir}/udev/udev-acl ${base_libdir}/udev/rules.d/70-acl.rules"

FILES_udev-utils = "${bindir}/udevinfo ${bindir}/udevtest ${base_sbindir}/udevadm"

RDEPENDS_udev-consolekit += "udev-acl"
FILES_udev-consolekit = "${libdir}/ConsoleKit"

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
	install -m 0755 ${WORKDIR}/udev-cache ${D}${sysconfdir}/init.d/udev-cache

	install -d ${D}${sysconfdir}/default
	install -m 0755 ${WORKDIR}/udev-cache.default ${D}${sysconfdir}/default/udev-cache

	touch ${D}${sysconfdir}/udev/cache.data

	install -d ${D}${sysconfdir}/udev/rules.d/

	install -m 0644 ${WORKDIR}/local.rules         ${D}${sysconfdir}/udev/rules.d/local.rules

	install -d ${D}${sysconfdir}/udev/scripts/

	install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts/mount.sh
	install -m 0755 ${WORKDIR}/network.sh ${D}${sysconfdir}/udev/scripts
}
