SUMMARY = "Serial terminal support for systemd (using SERIAL_CONSOLES)"
HOMEPAGE = "https://www.freedesktop.org/wiki/Software/systemd/"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

# Note that this recipe explicitly creates a serial-getty@ service for every tty
# in SERIAL_CONSOLES. This is typically not always needed with systemd as it
# will probe at boot and generate getty instances for any active consoles as
# required.  This recipe (enabled via disabling serial-getty-generator in systemd)
# should only be used if the generator is not appropriate.
# Its gettys ignore SERIAL_CONSOLES baud rates and keep the rate the kernel set.

# As this package is tied to systemd, only build it when we're also building systemd.
inherit features_check
REQUIRED_DISTRO_FEATURES += "systemd"
REQUIRED_DISTRO_FEATURES += "usrmerge"

do_install() {
	if [ ! -z "${SERIAL_CONSOLES}" ] ; then
		install -d ${D}${sysconfdir}/systemd/system/getty.target.wants/

		tmp="${SERIAL_CONSOLES}"
		for entry in $tmp ; do
			ttydev=`echo $entry | sed -e 's/^[0-9]*\;//' -e 's/\;.*//'`
			ln -sf ${systemd_system_unitdir}/serial-getty@.service \
				${D}${sysconfdir}/systemd/system/getty.target.wants/serial-getty@$ttydev.service
		done
	fi
}

# This is a machine specific file
PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES:${PN} = "${sysconfdir}"

ALLOW_EMPTY:${PN} = "1"
