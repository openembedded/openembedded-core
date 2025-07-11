# FIXME: the LIC_FILES_CHKSUM values have been updated by 'devtool upgrade'.
# The following is the difference between the old and the new license text.
# Please update the LICENSE value if needed, and summarize the changes in
# the commit message via 'License-Update:' tag.
# (example: 'License-Update: copyright years updated.')
#
# The changes:
#
# --- LICENSE.md
# +++ LICENSE.md
# @@ -1,6 +1,6 @@
#  Sudo is distributed under the following license:
#
# -    Copyright (c) 1994-1996, 1998-2023
# +    Copyright (c) 1994-1996, 1998-2025
#          Todd C. Miller <Todd.Miller@sudo.ws>
#
#      Permission to use, copy, modify, and distribute this software for any
# @@ -247,9 +247,9 @@
#
#  The file arc4random.c bears the following license:
#
# -    Copyright (c) 1996, David Mazieres <dm@uun.org>
# -    Copyright (c) 2008, Damien Miller <djm@openbsd.org>
# -    Copyright (c) 2013, Markus Friedl <markus@openbsd.org>
# +    Copyright (c) 1996, David Mazieres <dm@uun.org>
# +    Copyright (c) 2008, Damien Miller <djm@openbsd.org>
# +    Copyright (c) 2013, Markus Friedl <markus@openbsd.org>
#      Copyright (c) 2014, Theo de Raadt <deraadt@openbsd.org>
#
#      Permission to use, copy, modify, and distribute this software for any
# @@ -282,7 +282,7 @@
#
#  The file getentropy.c bears the following license:
#
# -    Copyright (c) 2014 Theo de Raadt <deraadt@openbsd.org>
# +    Copyright (c) 2014 Theo de Raadt <deraadt@openbsd.org>
#      Copyright (c) 2014 Bob Beck <beck@obtuse.com>
#
#      Permission to use, copy, modify, and distribute this software for any
# @@ -299,7 +299,7 @@
#
#  The embedded copy of zlib bears the following license:
#
# -    Copyright (C) 1995-2022 Jean-loup Gailly and Mark Adler
# +    Copyright (C) 1995-2024 Jean-loup Gailly and Mark Adler
#
#      This software is provided 'as-is', without any express or implied
#      warranty.  In no event will the authors be held liable for any damages
#
#

require sudo.inc

SRC_URI = "https://www.sudo.ws/dist/sudo-${PV}.tar.gz \
           ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://0001-sudo.conf.in-fix-conflict-with-multilib.patch \
           "

PAM_SRC_URI = "file://sudo.pam"

SRC_URI[sha256sum] = "ff607ea717072197738a78f778692cd6df9a7e3e404565f51de063ca27455d32"

DEPENDS += " virtual/crypt ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS:${PN} += " ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam-plugin-limits pam-plugin-keyinit', '', d)}"

CACHED_CONFIGUREVARS = " \
        ac_cv_type_rsize_t=no \
        ac_cv_path_MVPROG=${base_bindir}/mv \
        ac_cv_path_BSHELLPROG=${base_bindir}/sh \
        ac_cv_path_SENDMAILPROG=${sbindir}/sendmail \
        ac_cv_path_VIPROG=${base_bindir}/vi \
        "

EXTRA_OECONF += " \
             ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '--with-pam', '--without-pam', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-tmpfiles.d=${nonarch_libdir}/tmpfiles.d', '--disable-tmpfiles.d', d)} \
             --with-rundir=/run/sudo \
             --with-vardir=/var/lib/sudo \
             --libexecdir=${libdir} \
             "

do_install:append () {
	if [ "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}" ]; then
		install -D -m 644 ${WORKDIR}/sudo.pam ${D}/${sysconfdir}/pam.d/sudo
		if ${@bb.utils.contains('PACKAGECONFIG', 'pam-wheel', 'true', 'false', d)} ; then
			echo 'auth       required     pam_wheel.so use_uid' >>${D}${sysconfdir}/pam.d/sudo
			sed -i 's/# \(%wheel ALL=(ALL) ALL\)/\1/' ${D}${sysconfdir}/sudoers
		fi
	fi

	chmod 4111 ${D}${bindir}/sudo
	chmod 0440 ${D}${sysconfdir}/sudoers

	# Explicitly remove the /sudo directory to avoid QA error
	rmdir -p --ignore-fail-on-non-empty ${D}/run/sudo
}

FILES:${PN}-dev += "${libdir}/${BPN}/lib*${SOLIBSDEV} ${libdir}/${BPN}/*.la \
                    ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la"

CONFFILES:${PN}-lib = "${sysconfdir}/sudoers"

SUDO_PACKAGES = "${PN}-sudo\
                 ${PN}-lib"

PACKAGE_BEFORE_PN = "${SUDO_PACKAGES}"

RDEPENDS:${PN}-sudo = "${PN}-lib"
RDEPENDS:${PN} += "${SUDO_PACKAGES}"

FILES:${PN}-sudo = "${bindir}/sudo ${bindir}/sudoedit"
FILES:${PN}-lib = "${localstatedir} ${libexecdir} ${sysconfdir} ${libdir} ${nonarch_libdir}"
