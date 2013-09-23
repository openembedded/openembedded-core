require shadow.inc

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)} \
           shadow-native"
RDEPENDS_${PN} = "shadow-securetty ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_PLUGINS}', '', d)} \
                  base-passwd"
PR = "r13"

SRC_URI += " \
           file://login_defs_pam.sed \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://shadow-4.1.4.2-groupmod-pam-check.patch \
           file://shadow-4.1.4.2-su_no_sanitize_env.patch \
           file://shadow-update-pam-conf.patch \
           file://slackware_fix_for_glib-2.17_crypt.patch \
           file://fix-etc-gshadow-reading.patch \
           "

EXTRA_OECONF += "${@base_contains('DISTRO_FEATURES', 'pam', '--with-libpam', '--without-libpam', d)}"
EXTRA_OECONF_libc-uclibc += "--with-nscd=no"

# Build falsely assumes that if --enable-libpam is set, we don't need to link against
# libcrypt. This breaks chsh.
BUILD_LDFLAGS += "${@base_contains('DISTRO_FEATURES', 'pam', base_contains('DISTRO_FEATURES', 'libc-crypt',  '-lcrypt', '', d), '', d)}"

PAM_PLUGINS = "libpam-runtime \
               pam-plugin-faildelay \
               pam-plugin-securetty \
               pam-plugin-nologin \
               pam-plugin-env \
               pam-plugin-group \
               pam-plugin-limits \
               pam-plugin-lastlog \
               pam-plugin-motd \
               pam-plugin-mail \
               pam-plugin-shells \
               pam-plugin-rootok"

# Additional Policy files for PAM
PAM_SRC_URI = "file://pam.d/chfn \
               file://pam.d/chpasswd \
               file://pam.d/chsh \
               file://pam.d/login \
               file://pam.d/newusers \
               file://pam.d/passwd \
               file://pam.d/su"

do_install_append() {
	# Ensure that the image has as a /var/spool/mail dir so shadow can
	# put mailboxes there if the user reconfigures shadow to its
	# defaults (see sed below).
	install -d ${D}${localstatedir}/spool/mail

	if [ -e ${WORKDIR}/pam.d ]; then
		install -d ${D}${sysconfdir}/pam.d/
		install -m 0644 ${WORKDIR}/pam.d/* ${D}${sysconfdir}/pam.d/
		# Remove defaults that are not used when supporting PAM.
		sed -i -f ${WORKDIR}/login_defs_pam.sed ${D}${sysconfdir}/login.defs
	fi

	install -d ${D}${sbindir} ${D}${base_sbindir} ${D}${base_bindir} 

	# Move binaries to the locations we want
	rm ${D}${sbindir}/vigr
	ln -sf vipw.${BPN} ${D}${base_sbindir}/vigr
	if [ "${sbindir}" != "${base_sbindir}" ]; then
		mv ${D}${sbindir}/vipw ${D}${base_sbindir}/vipw
	fi
	if [ "${bindir}" != "${base_bindir}" ]; then
		mv ${D}${bindir}/login ${D}${base_bindir}/login
		mv ${D}${bindir}/su ${D}${base_bindir}/su
	fi

	# Handle link properly after rename, otherwise missing files would
	# lead rpm failed dependencies.
	ln -sf newgrp.${BPN} ${D}${bindir}/sg
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "200"

ALTERNATIVE_${PN} = "passwd chfn newgrp chsh groups chpasswd login vipw vigr su"
ALTERNATIVE_LINK_NAME[chpasswd] = "${sbindir}/chpasswd"
ALTERNATIVE_LINK_NAME[login] = "${base_bindir}/login"
ALTERNATIVE_LINK_NAME[vipw] = "${base_sbindir}/vipw"
ALTERNATIVE_LINK_NAME[vigr] = "${base_sbindir}/vigr"
ALTERNATIVE_LINK_NAME[su] = "${base_bindir}/su"

pkg_postinst_${PN} () {
	if [ "x$D" != "x" ]; then
	  rootarg="--root=$D"
	else
	  rootarg=""
	fi

	pwconv $rootarg
	grpconv $rootarg
}
