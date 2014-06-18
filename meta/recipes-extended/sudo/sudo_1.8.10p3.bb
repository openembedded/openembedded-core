require sudo.inc

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://volatiles.99_sudo"

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[md5sum] = "fcd8d0d9f9f0397d076ee901e242ed39"
SRC_URI[sha256sum] = "6eda135fa68163108f1c24de6975de5ddb09d75730bb62d6390bda7b04345400"

DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS_${PN} += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam-plugin-limits pam-plugin-keyinit', '', d)}"

EXTRA_OECONF += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)}"

do_install_append () {
	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}" = "pam" ]; then
		install -D -m 664 ${WORKDIR}/sudo.pam ${D}/${sysconfdir}/pam.d/sudo
	fi

	chmod 4111 ${D}${bindir}/sudo
	chmod 0440 ${D}${sysconfdir}/sudoers

	# Explicitly remove the ${localstatedir}/run directory as we can
	# manage it by a configuration file under ${sysconfdir}/default/volatiles/
	rmdir -p --ignore-fail-on-non-empty ${D}${localstatedir}/run/sudo
	install -d ${D}/${sysconfdir}/default/volatiles
	install -m 644 ${WORKDIR}/volatiles.99_sudo ${D}/${sysconfdir}/default/volatiles/99_sudo
}
