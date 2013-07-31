SUMMARY = "Tools to change and administer password and group data"
DESCRIPTION = "Tools to change and administer password and group data"
HOMEPAGE = "http://pkg-shadow.alioth.debian.org"
BUGTRACKER = "https://alioth.debian.org/tracker/?group_id=30580"
SECTION = "base utils"
LICENSE = "BSD | Artistic-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=08c553a87d4e51bbed50b20e0adcaede \
                    file://src/passwd.c;beginline=8;endline=30;md5=d83888ea14ae61951982d77125947661"

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)} \
           shadow-native"
RDEPENDS_${PN} = "shadow-securetty ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_PLUGINS}', '', d)} \
                  base-passwd"
PR = "r13"

SRC_URI = "http://pkg-shadow.alioth.debian.org/releases/${BPN}-${PV}.tar.bz2 \
           file://login_defs_pam.sed \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://shadow.automake-1.11.patch \
           file://shadow-4.1.3-dots-in-usernames.patch \
           file://shadow-4.1.4.2-env-reset-keep-locale.patch \
           file://shadow-4.1.4.2-groupmod-pam-check.patch \
           file://shadow-4.1.4.2-su_no_sanitize_env.patch \
           file://shadow-update-pam-conf.patch \
           file://shadow_fix_for_automake-1.12.patch \
           file://slackware_fix_for_glib-2.17_crypt.patch \
           "

SRC_URI[md5sum] = "b8608d8294ac88974f27b20f991c0e79"
SRC_URI[sha256sum] = "633f5bb4ea0c88c55f3642c97f9d25cbef74f82e0b4cf8d54e7ad6f9f9caa778"

inherit autotools gettext

EXTRA_OECONF += "--without-audit \
                 --without-libcrack \
                 ${@base_contains('DISTRO_FEATURES', 'pam', '--with-libpam', '--without-libpam', d)} \
                 --without-selinux"
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

do_install() {
	oe_runmake DESTDIR="${D}" sbindir="${base_sbindir}" usbindir="${sbindir}" install

	# Info dir listing isn't interesting at this point so remove it if it exists.
	if [ -e "${D}${infodir}/dir" ]; then
		rm -f ${D}${infodir}/dir
	fi

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

	# Enable CREATE_HOME by default.
	sed -i 's/#CREATE_HOME/CREATE_HOME/g' ${D}${sysconfdir}/login.defs

	# As we are on an embedded system, ensure the users mailbox is in
	# ~/ not /var/spool/mail by default, as who knows where or how big
	# /var is. The system MDA will set this later anyway.
	sed -i 's/MAIL_DIR/#MAIL_DIR/g' ${D}${sysconfdir}/login.defs
	sed -i 's/#MAIL_FILE/MAIL_FILE/g' ${D}${sysconfdir}/login.defs

	# Disable checking emails.
	sed -i 's/MAIL_CHECK_ENAB/#MAIL_CHECK_ENAB/g' ${D}${sysconfdir}/login.defs

	# Now we don't have a mail system. Disable mail creation for now.
	sed -i 's:/bin/bash:/bin/sh:g' ${D}${sysconfdir}/default/useradd
	sed -i '/^CREATE_MAIL_SPOOL/ s:^:#:' ${D}${sysconfdir}/default/useradd

	# Use users group by default
	sed -i 's,^GROUP=1000,GROUP=100,g' ${D}${sysconfdir}/default/useradd

	install -d ${D}${sbindir} ${D}${base_sbindir} ${D}${base_bindir} 

	# Move binaries to the locations we want
	rm ${D}${sbindir}/vigr
	ln -sf vipw.${BPN} ${D}${base_sbindir}/vigr
	if [ "${sbindir}" != "${base_sbindir}" ]; then
		mv ${D}${sbindir}/vipw ${D}${base_sbindir}/vipw
	fi
	if [ "${bindir}" != "${base_bindir}" ]; then
		mv ${D}${bindir}/login ${D}${base_bindir}/login
	fi

	# Handle link properly after rename, otherwise missing files would
	# lead rpm failed dependencies.
	ln -sf newgrp.${BPN} ${D}${bindir}/sg
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "200"

ALTERNATIVE_${PN} = "passwd chfn newgrp chsh groups chpasswd login vipw vigr"
ALTERNATIVE_LINK_NAME[chpasswd] = "${sbindir}/chpasswd"
ALTERNATIVE_LINK_NAME[login] = "${base_bindir}/login"
ALTERNATIVE_LINK_NAME[vipw] = "${base_sbindir}/vipw"
ALTERNATIVE_LINK_NAME[vigr] = "${base_sbindir}/vigr"

pkg_postinst_${PN} () {
	if [ "x$D" != "x" ]; then
	  rootarg="--root=$D"
	else
	  rootarg=""
	fi

	pwconv $rootarg
	grpconv $rootarg
}
