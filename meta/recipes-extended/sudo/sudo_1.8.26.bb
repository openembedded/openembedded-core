require sudo.inc

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://0001-Include-sys-types.h-for-id_t-definition.patch \
           "

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[md5sum] = "833084947d98e7745b94845f4b7a8a9a"
SRC_URI[sha256sum] = "40da219a6f0341ccb22d04a98988e27f09b831d2561b14c6154067a49ef3fee2"

DEPENDS += " virtual/crypt ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS_${PN} += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam-plugin-limits pam-plugin-keyinit', '', d)}"

EXTRA_OECONF += " \
             ac_cv_type_rsize_t=no \
             ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-tmpfiles.d=${libdir}/tmpfiles.d', '--disable-tmpfiles.d', d)} \
             "

do_install_append () {
	if [ "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}" ]; then
		install -D -m 644 ${WORKDIR}/sudo.pam ${D}/${sysconfdir}/pam.d/sudo
		if ${@bb.utils.contains('PACKAGECONFIG', 'pam-wheel', 'true', 'false', d)} ; then
			echo 'auth       required     pam_wheel.so use_uid' >>${D}${sysconfdir}/pam.d/sudo
			sed -i 's/# \(%wheel ALL=(ALL) ALL\)/\1/' ${D}${sysconfdir}/sudoers
		fi
	fi

	chmod 4111 ${D}${bindir}/sudo
	chmod 0440 ${D}${sysconfdir}/sudoers

	# Explicitly remove the /run directory to avoid QA error
	rmdir -p --ignore-fail-on-non-empty ${D}/run/sudo
}

FILES_${PN} += "${libdir}/tmpfiles.d"
FILES_${PN}-dev += "${libexecdir}/${BPN}/lib*${SOLIBSDEV} ${libexecdir}/${BPN}/*.la \
                    ${libexecdir}/lib*${SOLIBSDEV} ${libexecdir}/*.la"
