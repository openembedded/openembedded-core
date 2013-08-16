require sudo.inc

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[md5sum] = "a02367090e1dac8d0c1747de1127b6bf"
SRC_URI[sha256sum] = "39626cf3d48c4fd5a9139a2627d42bfefac7ce47f470bdba3aeb4e3d7c49566a"

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
