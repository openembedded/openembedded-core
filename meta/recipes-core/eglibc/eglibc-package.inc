#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a eglibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc*', d.getVar('TARGET_OS', True)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   d.getVar('TARGET_OS', True))
}

# Set this to zero if you don't want ldconfig in the output package
USE_LDCONFIG ?= "1"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PACKAGES = "${PN}-dbg ${PN} catchsegv${PKGSUFFIX} sln${PKGSUFFIX} nscd${PKGSUFFIX} ldd${PKGSUFFIX} ${PN}-mtrace ${PN}-utils eglibc-extra-nss${PKGSUFFIX} eglibc-thread-db${PKGSUFFIX} ${PN}-pic ${PN}-dev ${PN}-staticdev ${PN}-doc libcidn${PKGSUFFIX} libmemusage${PKGSUFFIX} libsegfault${PKGSUFFIX} ${PN}-pcprofile libsotruss${PKGSUFFIX}"

# The ld.so in this eglibc supports the GNU_HASH
RPROVIDES_${PN} = "glibc${PKGSUFFIX} rtld(GNU_HASH)"
RPROVIDES_${PN}-utils = "glibc${PKGSUFFIX}-utils"
RPROVIDES_${PN}-mtrace = "glibc${PKGSUFFIX}-mtrace libc-mtrace"
RPROVIDES_${PN}-pic = "glibc${PKGSUFFIX}-pic"
RPROVIDES_${PN}-dev = "glibc${PKGSUFFIX}-dev"
RPROVIDES_${PN}-staticdev = "glibc${PKGSUFFIX}-staticdev"
RPROVIDES_${PN}-doc = "glibc${PKGSUFFIX}-doc"
RPROVIDES_eglibc-extra-nss${PKGSUFFIX} = "glibc-extra-nss${PKGSUFFIX}"
RPROVIDES_eglibc-thread-db${PKGSUFFIX} = "glibc-thread-db${PKGSUFFIX}"
RPROVIDES_${PN}-pcprofile = "glibc${PKGSUFFIX}-pcprofile"
RPROVIDES_${PN}-dbg = "glibc-dbg${PKGSUFFIX}"
libc_baselibs = "${base_libdir}/libcrypt*.so.* ${base_libdir}/libcrypt-*.so ${base_libdir}/libc.so.* ${base_libdir}/libc-*.so ${base_libdir}/libm*.so.* ${base_libdir}/libm-*.so ${base_libdir}/ld*.so.* ${base_libdir}/ld-*.so ${base_libdir}/libpthread*.so.* ${base_libdir}/libpthread-*.so ${base_libdir}/libresolv*.so.* ${base_libdir}/libresolv-*.so ${base_libdir}/librt*.so.* ${base_libdir}/librt-*.so ${base_libdir}/libutil*.so.* ${base_libdir}/libutil-*.so ${base_libdir}/libnsl*.so.* ${base_libdir}/libnsl-*.so ${base_libdir}/libnss_files*.so.* ${base_libdir}/libnss_files-*.so ${base_libdir}/libnss_compat*.so.* ${base_libdir}/libnss_compat-*.so ${base_libdir}/libnss_dns*.so.* ${base_libdir}/libnss_dns-*.so ${base_libdir}/libdl*.so.* ${base_libdir}/libdl-*.so ${base_libdir}/libanl*.so.* ${base_libdir}/libanl-*.so ${base_libdir}/libBrokenLocale*.so.* ${base_libdir}/libBrokenLocale-*.so"

FILES_${PN} = "${libc_baselibs} ${libexecdir}/* ${@base_conditional('USE_LDCONFIG', '1', '${base_sbindir}/ldconfig ${sysconfdir}/ld.so.conf', '', d)}"
FILES_ldd${PKGSUFFIX} = "${bindir}/ldd"
FILES_libsegfault${PKGSUFFIX} = "${base_libdir}/libSegFault*"
FILES_libcidn${PKGSUFFIX} = "${base_libdir}/libcidn-*.so ${base_libdir}/libcidn.so.*"
FILES_libmemusage${PKGSUFFIX} = "${base_libdir}/libmemusage.so"
FILES_eglibc-extra-nss${PKGSUFFIX} = "${base_libdir}/libnss_*-*.so ${base_libdir}/libnss_*.so.*"
FILES_sln${PKGSUFFIX} = "/sbin/sln"
FILES_${PN}-pic = "${libdir}/*_pic.a ${libdir}/*_pic.map ${libdir}/libc_pic/*.o"
FILES_libsotruss${PKGSUFFIX} = "${libdir}/audit/sotruss-lib.so"
FILES_${PN}-dev_append += "${bindir}/rpcgen ${libdir}/*_nonshared.a \
	${base_libdir}/*_nonshared.a ${base_libdir}/*.o ${datadir}/aclocal"
FILES_${PN}-staticdev_append += "${libdir}/*.a ${base_libdir}/*.a"
FILES_nscd${PKGSUFFIX} = "${sbindir}/nscd*"
FILES_${PN}-mtrace = "${bindir}/mtrace"
FILES_${PN}-utils = "${bindir}/* ${sbindir}/*"
FILES_${PN}-dbg += "${libexecdir}/*/.debug ${libdir}/audit/.debug"
FILES_catchsegv${PKGSUFFIX} = "${bindir}/catchsegv"
RDEPENDS_catchsegv${PKGSUFFIX} = "libsegfault"
RDEPENDS_${PN}-utils += "bash"
RDEPENDS_${PN}-mtrace += "perl"
FILES_${PN}-pcprofile = "${base_libdir}/libpcprofile.so"
FILES_eglibc-thread-db${PKGSUFFIX} = "${base_libdir}/libthread_db.so.* ${base_libdir}/libthread_db-*.so"
RPROVIDES_${PN}-dev += "libc-dev"
RPROVIDES_${PN}-staticdev += "libc-staticdev"

SUMMARY_sln = "The static ln."
DESCRIPTION_sln = "Similar to the 'ln' utility, but statically linked.  sln is useful to make symbolic links to dynamic libraries if the dynamic linking system, for some reason, is not functional."
SUMMARY_nscd = "Name service cache daemon"
DESCRIPTION_nscd = "nscd, name service cache daemon, caches name service lookups for the passwd, group and hosts information.  It can damatically improvide performance with remote, such as NIS or NIS+, name services."
SUMMARY_eglibc-extra-nss${PKGSUFFIX} = "hesiod, NIS and NIS+ nss libraries"
DESCRIPTION_eglibc-extra-nss${PKGSUFFIX} = "eglibc: nis, nisplus and hesiod search services."
SUMMARY_ldd = "print shared library dependencies"
DESCRIPTION_ldd = "/usr/bin/ldd prints shared library dependencies for each program or shared library specified on the command line."
SUMMARY_${PN}-utils = "Miscellaneous utilities provided by eglibc"
DESCRIPTION_${PN}-utils = "Miscellaneous utilities including getconf, iconf, locale, gencat, tzselect, zic, rpcinfo, ..."
SUMMARY_${PN}-mtrace = "mtrace utility provided by eglibc"
DESCRIPTION_${PN}-mtrace = "mtrace utility provided by eglibc"
DESCRIPTION_libsotruss = "Library to support sotruss which traces calls through PLTs"

inherit libc-common multilib_header

PERLPATH = "${bindir}/env perl"
PERLPATH_virtclass-nativesdk = "/usr/bin/env perl"

do_install_append () {
	rm -f ${D}${sysconfdir}/localtime

	oe_multilib_header bits/syscall.h

	if [ -f ${D}${bindir}/mtrace ]; then
		sed -i -e '1s,#!.*perl,#! ${PERLPATH},' -e '2s,exec.*perl,exec ${PERLPATH},' ${D}${bindir}/mtrace
	fi
}

do_install_locale () {
	dest=${D}/${includedir}/eglibc-locale-internal-${MULTIMACH_TARGET_SYS}
	install -d ${dest}${base_libdir} ${dest}${bindir} ${dest}${libdir} ${dest}${datadir}
	if [ "${base_libdir}" != "${libdir}" ]; then
		cp -fpPR ${D}${base_libdir}/* ${dest}${base_libdir}
	fi
	if [ -e ${D}${bindir}/localedef ]; then
		mv -f ${D}${bindir}/localedef ${dest}${bindir}
	fi
	if [ -e ${D}${libdir}/gconv ]; then
		mv -f ${D}${libdir}/gconv ${dest}${libdir}
	fi
	cp -fpPR ${D}${libdir}/* ${dest}${libdir}
	if [ -e ${D}${datadir}/i18n ]; then
		mv ${D}${datadir}/i18n ${dest}${datadir}
	fi
	cp -fpPR ${D}${datadir}/* ${dest}${datadir}
	cp -fpPR ${WORKDIR}/SUPPORTED ${dest}
}

addtask do_install_locale after do_install before do_populate_sysroot do_package

PACKAGE_PREPROCESS_FUNCS += "eglibc_package_preprocess"

eglibc_package_preprocess () {
	rm -rf ${PKGD}/${includedir}/eglibc-locale-internal-${MULTIMACH_TARGET_SYS}
	rm -rf ${PKGD}/${libdir}/locale
}

