SUMMARY = "Tools to change and administer password and group data"
HOMEPAGE = "http://pkg-shadow.alioth.debian.org"
BUGTRACKER = "https://alioth.debian.org/tracker/?group_id=30580"
SECTION = "base utils"
LICENSE = "BSD | Artistic-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=08c553a87d4e51bbed50b20e0adcaede \
                    file://src/passwd.c;beginline=8;endline=30;md5=d83888ea14ae61951982d77125947661"

DEPENDS = "shadow-native"
DEPENDS_class-native = ""
DEPENDS_class-nativesdk = ""

SRC_URI = "http://pkg-shadow.alioth.debian.org/releases/${BPN}-${PV}.tar.bz2 \
           file://shadow.automake-1.11.patch \
           file://shadow_fix_for_automake-1.12.patch \
           file://shadow-4.1.3-dots-in-usernames.patch \
           file://shadow-4.1.4.2-env-reset-keep-locale.patch \
           ${@base_contains('PACKAGECONFIG', 'pam', '${PAM_SRC_URI}', '', d)} \
           "

SRC_URI_append_class-target = " \
           file://login_defs_pam.sed \
           file://shadow-4.1.4.2-groupmod-pam-check.patch \
           file://shadow-4.1.4.2-su_no_sanitize_env.patch \
           file://shadow-update-pam-conf.patch \
           file://slackware_fix_for_glib-2.17_crypt.patch \
           file://fix-etc-gshadow-reading.patch \
           "

SRC_URI_append_class-native = " \
           file://add_root_cmd_options.patch \
           file://disable-syslog.patch \
           file://useradd.patch \
           file://add_root_cmd_groupmems.patch \
           file://allow-for-setting-password-in-clear-text.patch \
           "
SRC_URI_append_class-nativesdk = " \
           file://add_root_cmd_options.patch \
           file://disable-syslog.patch \
           file://useradd.patch \
           file://add_root_cmd_groupmems.patch \
           "

SRC_URI[md5sum] = "b8608d8294ac88974f27b20f991c0e79"
SRC_URI[sha256sum] = "633f5bb4ea0c88c55f3642c97f9d25cbef74f82e0b4cf8d54e7ad6f9f9caa778"

PR = "r14"

# Additional Policy files for PAM
PAM_SRC_URI = "file://pam.d/chfn \
               file://pam.d/chpasswd \
               file://pam.d/chsh \
               file://pam.d/login \
               file://pam.d/newusers \
               file://pam.d/passwd \
               file://pam.d/su"

inherit autotools gettext

EXTRA_OECONF += "--without-audit \
                 --without-libcrack \
                 --without-selinux \
                 --with-group-name-max-length=24 \
                 ${NSCDOPT}"

NSCDOPT = ""
NSCDOPT_class-native = "--without-nscd"
NSCDOPT_class-nativesdk = "--without-nscd"
NSCDOPT_libc-uclibc = " --without-nscd"
          
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

PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}"
PACKAGECONFIG_class-native = ""
PACKAGECONFIG_class-nativesdk = ""
PACKAGECONFIG[pam] = "--with-libpam,--without-libpam,libpam,${PAM_PLUGINS}"

RDEPENDS_${PN} = "shadow-securetty \
                  base-passwd"
RDEPENDS_${PN}_class-native = ""
RDEPENDS_${PN}_class-nativesdk = ""

do_install() {
	oe_runmake DESTDIR="${D}" sbindir="${base_sbindir}" usbindir="${sbindir}" install

	# Info dir listing isn't interesting at this point so remove it if it exists.
	if [ -e "${D}${infodir}/dir" ]; then
		rm -f ${D}${infodir}/dir
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

	# Use proper encryption for passwords
	sed -i 's/^#ENCRYPT_METHOD.*$/ENCRYPT_METHOD SHA512/' ${D}${sysconfdir}/login.defs

	# Now we don't have a mail system. Disable mail creation for now.
	sed -i 's:/bin/bash:/bin/sh:g' ${D}${sysconfdir}/default/useradd
	sed -i '/^CREATE_MAIL_SPOOL/ s:^:#:' ${D}${sysconfdir}/default/useradd

	# Use users group by default
	sed -i 's,^GROUP=1000,GROUP=100,g' ${D}${sysconfdir}/default/useradd
}

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
