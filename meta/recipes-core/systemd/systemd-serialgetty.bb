DESCRIPTION = "Systemd serial config"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "r3"

SERIAL_CONSOLE ?= "115200 ttyS0"

SRC_URI = "file://serial-getty@.service"

def get_serial_console_value(d, index):
    c = d.getVar('SERIAL_CONSOLE', True)

    if len(c):
        return c.split()[index]

    return ""

def get_baudrate(d):
    return get_serial_console_value(d, 0)

def get_console(d):
    return get_serial_console_value(d, 1)

do_install() {
	if [ ! ${@get_baudrate(d)} = "" ]; then
		sed -i -e s/\@BAUDRATE\@/${@get_baudrate(d)}/g ${WORKDIR}/serial-getty@.service
		install -d ${D}${systemd_unitdir}/system/
		install -d ${D}${sysconfdir}/systemd/system/getty.target.wants/
		install ${WORKDIR}/serial-getty@.service ${D}${systemd_unitdir}/system/

		# enable the service
		ln -sf ${systemd_unitdir}/system/serial-getty@.service \
			${D}${sysconfdir}/systemd/system/getty.target.wants/serial-getty@${@get_console(d)}.service
	fi
}

RDEPENDS_${PN} = "systemd"

# This is a machine specific file
FILES_${PN} = "${systemd_unitdir}/system/serial-getty@.service ${sysconfdir}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# As this package is tied to systemd, only build it when we're also building systemd.
python () {
    if not oe.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' not in DISTRO_FEATURES")
}
