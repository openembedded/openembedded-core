SUMMARY = "Extra machine specific configuration files"
DESCRIPTION = "Extra machine specific configuration files for udev, specifically blacklist information."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PR = "r2"

SRC_URI = "file://mount.blacklist \
           file://COPYING.GPL"

do_install () {
	install -d ${D}${sysconfdir}/udev/

	install -m 0644 ${WORKDIR}/mount.blacklist     ${D}${sysconfdir}/udev/
}
