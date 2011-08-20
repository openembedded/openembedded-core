SUMMARY = "Linux-PAM (Pluggable Authentication Modules)"
DESCRIPTION = "Linux-PAM (Pluggable Authentication Modules for Linux), Basically, it is a flexible mechanism for authenticating users"
HOMEPAGE = "http://www.kernel.org/pub/linux/libs/pam/"
BUGTRACKER = "http://sourceforge.net/projects/pam/support"
SECTION = "base"
# PAM is dual licensed under GPL and BSD.
# /etc/pam.d comes from Debian libpam-runtime in 2009-11 (at that time 
# libpam-runtime-1.0.1 is GPLv2+), by openembedded
LICENSE = "GPLv2+ | BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ca0395de9a86191a078b8b79302e3083"

PR = "r2"

DEPENDS = "bison flex cracklib"
RDEPENDS_${PN}-runtime = "libpam pam-plugin-deny pam-plugin-permit pam-plugin-warn pam-plugin-unix"
RDEPENDS_${PN}-xtests = "libpam pam-plugin-access pam-plugin-debug pam-plugin-cracklib pam-plugin-pwhistory \
                        pam-plugin-succeed-if pam-plugin-time coreutils"
RRECOMMENDS_${PN} = "libpam-runtime"

SRC_URI = "${KERNELORG_MIRROR}/linux/libs/pam/library/Linux-PAM-${PV}.tar.bz2 \
           file://99_pam \
           file://pam.d/* \
           file://libpam-xtests.patch"

SRC_URI_append_libc-uclibc = " file://pam-no-innetgr.patch"

SRC_URI[md5sum] = "e9af5fb27bb22edb55d077e2888b3ebc"
SRC_URI[sha256sum] = "ccd89331914390b1e9e99c954471d65f19b660d81e15a46eeb96cee125d44056"

EXTRA_OECONF = "--with-db-uniquename=_pam \
                --includedir=${includedir}/security \
                --libdir=${base_libdir} \
                --disable-regenerate-docu"
CFLAGS_append = " -fPIC "

S = "${WORKDIR}/Linux-PAM-${PV}"

inherit autotools gettext

PACKAGES += "${PN}-runtime ${PN}-xtests"
FILES_${PN} = "${base_libdir}/lib*${SOLIBS}"
FILES_${PN}-dbg += "${base_libdir}/security/.debug \
                    ${base_libdir}/security/pam_filter/.debug \
                    ${datadir}/Linux-PAM/xtests/.debug"

FILES_${PN}-dev += "${base_libdir}/security/*.la ${base_libdir}/*.la ${base_libdir}/lib*${SOLIBSDEV}"
FILES_${PN}-runtime = "${sysconfdir}"
FILES_${PN}-xtests = "${datadir}/Linux-PAM/xtests"

PACKAGES_DYNAMIC += " pam-plugin-*"

python populate_packages_prepend () {
	import os.path

	def pam_plugin_append_file(pn, dir, file):
		nf = os.path.join(dir, file)
		of = bb.data.getVar('FILES_' + pn, d, True)
		if of:
			nf = of + " " + nf
		bb.data.setVar('FILES_' + pn, nf, d)

	dvar = bb.data.expand('${WORKDIR}/package', d, True)
	pam_libdir = bb.data.expand('${base_libdir}/security', d)
	pam_sbindir = bb.data.expand('${sbindir}', d)
	pam_filterdir = bb.data.expand('${base_libdir}/security/pam_filter', d)

	do_split_packages(d, pam_libdir, '^pam(.*)\.so$', 'pam-plugin%s', 'PAM plugin for %s', extra_depends='')
	pam_plugin_append_file('pam-plugin-unix', pam_sbindir, 'unix_chkpwd')
	pam_plugin_append_file('pam-plugin-unix', pam_sbindir, 'unix_update')
	pam_plugin_append_file('pam-plugin-tally', pam_sbindir, 'pam_tally')
	pam_plugin_append_file('pam-plugin-tally2', pam_sbindir, 'pam_tally2')
	pam_plugin_append_file('pam-plugin-timestamp', pam_sbindir, 'pam_timestamp_check')
	pam_plugin_append_file('pam-plugin-mkhomedir', pam_sbindir, 'mkhomedir_helper')
	do_split_packages(d, pam_filterdir, '^(.*)$', 'pam-filter-%s', 'PAM filter for %s', extra_depends='')
}

do_install() {
	autotools_do_install

	# don't install /var/run when populating rootfs. Do it through volatile
	rm -rf ${D}/var
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/99_pam ${D}/etc/default/volatiles

	install -d ${D}${sysconfdir}/pam.d/     
	install -m 0644 ${WORKDIR}/pam.d/* ${D}${sysconfdir}/pam.d/
}

pkg_postinst_pam-plugin-unix () {
    # below is necessary to allow unix_chkpwd get user info from shadow file
    # on lsb images
    chmod 4755 ${sbindir}/unix_chkpwd
}
