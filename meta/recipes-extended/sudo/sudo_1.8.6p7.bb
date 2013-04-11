require sudo.inc

PR = "r0"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           file://libtool.patch \
           file://crypt.patch \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[md5sum] = "126abfa2e841139e774d4c67d80f0e5b"
SRC_URI[sha256sum] = "301089edb22356f59d097f6abbe1303f03927a38691b02959d618546c2125036"

DEPENDS += " ${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS_${PN} += " ${@base_contains('DISTRO_FEATURES', 'pam', 'pam-plugin-limits pam-plugin-keyinit', '', d)}"

EXTRA_OECONF += " ${@base_contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)}"

do_install_append () {
	for feature in ${DISTRO_FEATURES}; do
		if [ "$feature" = "pam" ]; then
			install -D -m 664 ${WORKDIR}/sudo.pam ${D}/${sysconfdir}/pam.d/sudo
			break
		fi
	done

	chmod 4111 ${D}${bindir}/sudo
	chmod 0440 ${D}${sysconfdir}/sudoers
}
