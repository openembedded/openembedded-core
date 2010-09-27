SUMMARY = "Extra machine specific configuration files"
DESCRIPTION = "Extra machine specific configuration files for udev, specifically blacklist information."
LICENSE = "GPLv2"

PR = "r0"

SRC_URI = "file://mount.blacklist"

do_install () {
	install -d ${D}${sysconfdir}/udev/

	install -m 0644 ${WORKDIR}/mount.blacklist     ${D}${sysconfdir}/udev/
}
