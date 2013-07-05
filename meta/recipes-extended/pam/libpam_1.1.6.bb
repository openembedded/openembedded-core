SUMMARY = "Linux-PAM (Pluggable Authentication Modules)"
DESCRIPTION = "Linux-PAM (Pluggable Authentication Modules for Linux), a flexible mechanism for authenticating users"
HOMEPAGE = "https://fedorahosted.org/linux-pam/"
BUGTRACKER = "https://fedorahosted.org/linux-pam/newticket"
SECTION = "base"
# PAM is dual licensed under GPL and BSD.
# /etc/pam.d comes from Debian libpam-runtime in 2009-11 (at that time 
# libpam-runtime-1.0.1 is GPLv2+), by openembedded
LICENSE = "GPLv2+ | BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=7eb5c1bf854e8881005d673599ee74d3"

SRC_URI = "http://linux-pam.org/library/Linux-PAM-${PV}.tar.bz2 \
           file://99_pam \
           file://pam.d/* \
           file://libpam-xtests.patch \
           file://destdirfix.patch \
           file://fixsepbuild.patch \
           file://reflect-the-enforce_for_root-semantics-change-in-pam.patch \
           file://add-checks-for-crypt-returning-NULL.patch \
           file://libpam-fix-for-CVE-2010-4708.patch \
          "
SRC_URI[md5sum] = "7b73e58b7ce79ffa321d408de06db2c4"
SRC_URI[sha256sum] = "bab887d6280f47fc3963df3b95735a27a16f0f663636163ddf3acab5f1149fc2"

SRC_URI_append_libc-uclibc = " file://pam-no-innetgr.patch"

DEPENDS = "bison flex flex-native cracklib"

EXTRA_OECONF = "--with-db-uniquename=_pam \
                --includedir=${includedir}/security \
                --libdir=${base_libdir} \
                --disable-nis \
                --disable-regenerate-docu"

CFLAGS_append = " -fPIC "

PR = "r2"

S = "${WORKDIR}/Linux-PAM-${PV}"

inherit autotools gettext pkgconfig

PACKAGES += "${PN}-runtime ${PN}-xtests"
FILES_${PN} = "${base_libdir}/lib*${SOLIBS}"
FILES_${PN}-dbg += "${base_libdir}/security/.debug \
                    ${base_libdir}/security/pam_filter/.debug \
                    ${datadir}/Linux-PAM/xtests/.debug"

FILES_${PN}-dev += "${base_libdir}/security/*.la ${base_libdir}/*.la ${base_libdir}/lib*${SOLIBSDEV}"
FILES_${PN}-runtime = "${sysconfdir}"
FILES_${PN}-xtests = "${datadir}/Linux-PAM/xtests"

PACKAGES_DYNAMIC += "^pam-plugin-.*"

RDEPENDS_${PN}-runtime = "libpam pam-plugin-deny pam-plugin-permit pam-plugin-warn pam-plugin-unix"
RDEPENDS_${PN}-xtests = "libpam pam-plugin-access pam-plugin-debug pam-plugin-cracklib pam-plugin-pwhistory pam-plugin-succeed-if pam-plugin-time coreutils"
RRECOMMENDS_${PN} = "libpam-runtime"

python populate_packages_prepend () {
    def pam_plugin_append_file(pn, dir, file):
        nf = os.path.join(dir, file)
        of = d.getVar('FILES_' + pn, True)
        if of:
            nf = of + " " + nf
        d.setVar('FILES_' + pn, nf)

    dvar = bb.data.expand('${WORKDIR}/package', d, True)
    pam_libdir = d.expand('${base_libdir}/security')
    pam_sbindir = d.expand('${sbindir}')
    pam_filterdir = d.expand('${base_libdir}/security/pam_filter')

    do_split_packages(d, pam_libdir, '^pam(.*)\.so$', 'pam-plugin%s', 'PAM plugin for %s', extra_depends='')
    mlprefix = d.getVar('MLPREFIX', True) or ''
    pam_plugin_append_file('%spam-plugin-unix' % mlprefix, pam_sbindir, 'unix_chkpwd')
    pam_plugin_append_file('%spam-plugin-unix' % mlprefix, pam_sbindir, 'unix_update')
    pam_plugin_append_file('%spam-plugin-tally' % mlprefix, pam_sbindir, 'pam_tally')
    pam_plugin_append_file('%spam-plugin-tally2' % mlprefix, pam_sbindir, 'pam_tally2')
    pam_plugin_append_file('%spam-plugin-timestamp' % mlprefix, pam_sbindir, 'pam_timestamp_check')
    pam_plugin_append_file('%spam-plugin-mkhomedir' % mlprefix, pam_sbindir, 'mkhomedir_helper')
    do_split_packages(d, pam_filterdir, '^(.*)$', 'pam-filter-%s', 'PAM filter for %s', extra_depends='')
}

do_install() {
	autotools_do_install

	# don't install /var/run when populating rootfs. Do it through volatile
	rm -rf ${D}${localstatedir}
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/99_pam ${D}${sysconfdir}/default/volatiles

	install -d ${D}${sysconfdir}/pam.d/     
	install -m 0644 ${WORKDIR}/pam.d/* ${D}${sysconfdir}/pam.d/

    # The lsb requires unix_chkpwd has setuid permission
    chmod 4755 ${D}${sbindir}/unix_chkpwd
}
