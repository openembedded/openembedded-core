require sudo.inc

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[md5sum] = "03773c373082843211c96df2c9187146"
SRC_URI[sha256sum] = "c22089aa1023233bf27808e8f7611b6b4c4fec2d151616179e1898876703cd51"

DEPENDS += " ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS_${PN} += " ${@base_contains('DISTRO_FEATURES', 'pam', 'pam-plugin-limits pam-plugin-keyinit', '', d)}"

EXTRA_OECONF += " ${@base_contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)}"

do_install_append () {
	if [ "${@base_contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}" = "pam" ]; then
		install -D -m 664 ${WORKDIR}/sudo.pam ${D}/${sysconfdir}/pam.d/sudo
	fi

	chmod 4111 ${D}${bindir}/sudo
	chmod 0440 ${D}${sysconfdir}/sudoers
}
