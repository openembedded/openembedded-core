DESCRIPTION = "udev is a program which dynamically creates and removes device nodes from \
/dev/. It responds to /sbin/hotplug device events and requires a 2.6 kernel."
LICENSE = "GPL"

UDEV_DEVFS_RULES ?= "0"

PACKAGES =+ "udev-utils"
FILES_udev-utils = "${usrbindir}/udevinfo ${usrbindir}/udevtest"

inherit update-rc.d

INITSCRIPT_NAME = "udev"
INITSCRIPT_PARAMS = "start 04 S ."

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
FILES_${PN} += "${usrbindir} ${usrsbindir}"
EXTRA_OEMAKE = "-e \
		'EXTRAS=${UDEV_EXTRAS}' \
		'STRIP=echo'"

do_install () {
	install -d ${D}${usrsbindir} \
		   ${D}${sbindir}
	oe_runmake 'DESTDIR=${D}' INSTALL=install install
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/udev
	if [ "${UDEV_DEVFS_RULES}" = "1" ]; then
		install -m 0644 ${S}/etc/udev/udev.rules.devfs ${D}${sysconfdir}/udev/rules.d/50-udev.rules
	fi
	install -d ${D}${sysconfdir}/udev/rules.d/
	install -m 0644 ${S}/etc/udev/debian/permissions.rules ${D}${sysconfdir}/udev/rules.d/
}
