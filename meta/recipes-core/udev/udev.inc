SUMMARY = "/dev/ and hotplug management daemon"
DESCRIPTION = "udev is a program which dynamically creates and removes device nodes from \
/dev/. It responds to /sbin/hotplug device events and requires a 2.6 kernel."
LICENSE = "GPLv2.0+ & LGPLv2.1+"

SRC_URI += " \
	   file://udev.rules \
	   file://devfs-udev.rules \
	   file://links.conf \
           file://permissions.rules \
	   file://mount.sh \
	   file://network.sh \
	   file://local.rules \
           file://udev-cache \
           file://init"

UDEV_DEVFS_RULES ?= "0"

PACKAGES =+ "udev-utils libvolume-id udev-cache"
FILES_udev-utils = "${usrbindir}/udevinfo ${usrbindir}/udevtest"
FILES_libvolume-id = "${base_libdir}/libvolume_id.so.*"
FILES_udev-cache = "${sysconfdir}/init.d/udev-cache"

RRECOMMENDS_${PN} += "udev-extraconf udev-cache"

inherit update-rc.d autotools pkgconfig

INITSCRIPT_PACKAGES = "udev udev-cache"
INITSCRIPT_NAME_udev = "udev"
INITSCRIPT_PARAMS_udev = "start 04 S ."
INITSCRIPT_NAME_udev-cache = "udev-cache"
INITSCRIPT_PARAMS_udev-cache = "start 36 S ."

export CROSS = "${TARGET_PREFIX}"
export HOSTCC = "${BUILD_CC}"
export udevdir ?= "/dev"
export usrbindir := "${bindir}"
export usrsbindir := "${sbindir}"
export etcdir = "${sysconfdir}"
LD = "${CC}"
bindir = "/bin"
sbindir = "/sbin"

UDEV_EXTRAS = "<override me>"
FILES_${PN} += "${usrbindir}/* ${usrsbindir}/*"
FILES_${PN}-dbg += "${usrbindir}/.debug ${usrsbindir}/.debug"
EXTRA_OEMAKE = "-e \
		'EXTRAS=${UDEV_EXTRAS}' \
		'STRIP=echo'"

do_install () {
	install -d ${D}${usrsbindir} \
		   ${D}${sbindir}
	oe_runmake 'DESTDIR=${D}' INSTALL=install install
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
	install -m 0755 ${WORKDIR}/udev-cache ${D}${sysconfdir}/init.d/udev-cache

	install -d ${D}${sysconfdir}/udev/rules.d/

	install -m 0644 ${WORKDIR}/local.rules         ${D}${sysconfdir}/udev/rules.d/local.rules
	install -m 0644 ${WORKDIR}/permissions.rules   ${D}${sysconfdir}/udev/rules.d/permissions.rules
	install -m 0644 ${WORKDIR}/udev.rules          ${D}${sysconfdir}/udev/rules.d/udev.rules
	if [ "${UDEV_DEVFS_RULES}" = "1" ]; then
		install -m 0644 ${WORKDIR}/devfs-udev.rules ${D}${sysconfdir}/udev/rules.d/devfs-udev.rules
	fi

	install -d ${D}${sysconfdir}/udev/scripts/

	install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts/mount.sh
	install -m 0755 ${WORKDIR}/network.sh ${D}${sysconfdir}/udev/scripts
}
