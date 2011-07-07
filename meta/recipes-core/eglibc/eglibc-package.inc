#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a eglibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc*', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

# Set this to zero if you don't want ldconfig in the output package
USE_LDCONFIG ?= "1"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PACKAGES = "${PN}-dbg ${PN} catchsegv${PKGSUFFIX} sln${PKGSUFFIX} nscd${PKGSUFFIX} ldd${PKGSUFFIX} ${PN}-utils eglibc-extra-nss ${PN}-pic ${PN}-dev eglibc-doc libcidn libmemusage libsegfault${PKGSUFFIX} eglibc-thread-db${PKGSUFFIX} eglibc-pcprofile libsotruss${PKGSUFFIX}"

# Create a eglibc-binaries
ALLOW_EMPTY_${PN}-binaries = "1"
PACKAGES += "${PN}-binaries"
RRECOMMENDS_${PN}-binaries =  "${@" ".join([p for p in d.getVar('PACKAGES', True).split() if p.find("eglibc-binary") != -1])}"

# Create a eglibc-charmaps package 
ALLOW_EMPTY_${PN}-charmaps = "1"
PACKAGES += "${PN}-charmaps"
RRECOMMENDS_${PN}-charmaps =  "${@" ".join([p for p in d.getVar('PACKAGES', True).split() if p.find("eglibc-charmap") != -1])}"

# Create a eglibc-gconvs package 
ALLOW_EMPTY_${PN}-gconvs = "1"
PACKAGES += "${PN}-gconvs"
RRECOMMENDS_${PN}-gconvs =  "${@" ".join([p for p in d.getVar('PACKAGES', True).split() if p.find("eglibc-gconv") != -1])}"

# Create a eglibc-localedatas package 
ALLOW_EMPTY_${PN}-localedatas = "1"
PACKAGES += "${PN}-localedatas"
RRECOMMENDS_${PN}-localedatas =  "${@" ".join([p for p in d.getVar('PACKAGES', True).split() if p.find("eglibc-localedata") != -1])}"

RPROVIDES_eglibc = "glibc"
RPROVIDES_eglibc-utils = "glibc-utils"
RPROVIDES_eglibc-pic = "glibc-pic"
RPROVIDES_eglibc-dev = "glibc-dev"
RPROVIDES_eglibc-doc = "glibc-doc"
RPROVIDES_eglibc-extra-nss = "glibc-extra-nss"
RPROVIDES_eglibc-thread-db = "glibc-thread-db"
RPROVIDES_eglibc-pcprofile = "glibc-pcprofile"
RPROVIDES_eglibc-dbg = "glibc-dbg"
libc_baselibs = "${base_libdir}/libcrypt*.so.* ${base_libdir}/libcrypt-*.so ${base_libdir}/libc.so.* ${base_libdir}/libc-*.so ${base_libdir}/libm*.so.* ${base_libdir}/libm-*.so ${base_libdir}/ld*.so.* ${base_libdir}/ld-*.so ${base_libdir}/libpthread*.so.* ${base_libdir}/libpthread-*.so ${base_libdir}/libresolv*.so.* ${base_libdir}/libresolv-*.so ${base_libdir}/librt*.so.* ${base_libdir}/librt-*.so ${base_libdir}/libutil*.so.* ${base_libdir}/libutil-*.so ${base_libdir}/libnsl*.so.* ${base_libdir}/libnsl-*.so ${base_libdir}/libnss_files*.so.* ${base_libdir}/libnss_files-*.so ${base_libdir}/libnss_compat*.so.* ${base_libdir}/libnss_compat-*.so ${base_libdir}/libnss_dns*.so.* ${base_libdir}/libnss_dns-*.so ${base_libdir}/libdl*.so.* ${base_libdir}/libdl-*.so ${base_libdir}/libanl*.so.* ${base_libdir}/libanl-*.so ${base_libdir}/libBrokenLocale*.so.* ${base_libdir}/libBrokenLocale-*.so"

FILES_${PN} = "${libc_baselibs} ${libexecdir}/* ${@base_conditional('USE_LDCONFIG', '1', '${base_sbindir}/ldconfig ${sysconfdir}/ld.so.conf', '', d)}"
FILES_ldd${PKGSUFFIX} = "${bindir}/ldd"
FILES_libsegfault${PKGSUFFIX} = "${base_libdir}/libSegFault*"
FILES_libcidn = "${base_libdir}/libcidn-*.so ${base_libdir}/libcidn.so.*"
FILES_libmemusage = "${base_libdir}/libmemusage.so"
FILES_eglibc-extra-nss = "${base_libdir}/libnss_*-*.so ${base_libdir}/libnss_*.so.*"
FILES_sln = "/sbin/sln"
FILES_eglibc-pic = "${libdir}/*_pic.a ${libdir}/*_pic.map ${libdir}/libc_pic/"
FILES_libsotruss${PKGSUFFIX} = "${libdir}/audit/sotruss-lib.so"
FILES_eglibc-dev_append += "${bindir}/rpcgen ${libdir}/*.a \
	${base_libdir}/*.a ${base_libdir}/*.o ${datadir}/aclocal"
FILES_nscd${PKGSUFFIX} = "${sbindir}/nscd*"
FILES_eglibc-utils = "${bindir}/* ${sbindir}/*"
FILES_${PN}-dbg += "${libexecdir}/*/.debug ${libdir}/audit/.debug"
FILES_catchsegv${PKGSUFFIX} = "${bindir}/catchsegv"
RDEPENDS_catchsegv${PKGSUFFIX} = "libsegfault"
RDEPENDS_eglibc-utils += "bash"
FILES_eglibc-pcprofile = "${base_libdir}/libpcprofile.so"
FILES_eglibc-thread-db${PKGSUFFIX} = "${base_libdir}/libthread_db*"
RPROVIDES_eglibc-dev += "libc-dev"

SUMMARY_sln = "The static ln."
DESCRIPTION_sln = "Similar to the 'ln' utility, but statically linked.  sln is useful to make symbolic links to dynamic libraries if the dynamic linking system, for some reason, is not functional."
SUMMARY_nscd = "Name service cache daemon"
DESCRIPTION_nscd = "nscd, name service cache daemon, caches name service lookups for the passwd, group and hosts information.  It can damatically improvide performance with remote, such as NIS or NIS+, name services."
SUMMARY_eglibc-extra-nss = "hesiod, NIS and NIS+ nss libraries"
DESCRIPTION_eglibc-extra-nss = "eglibc: nis, nisplus and hesiod search services."
SUMMARY_ldd = "print shared library dependencies"
DESCRIPTION_ldd = "/usr/bin/ldd prints shared library dependencies for each program or shared library specified on the command line."
SUMMARY_eglibc-utils = "Miscellaneous utilities provided by eglibc"
DESCRIPTION_eglibc-utils = "Miscellaneous utilities including getconf, iconf, locale, gencat, tzselect, zic, rpcinfo, ..."
DESCRIPTION_libsotruss = "Library to support sotruss which traces calls through PLTs"

inherit libc-common

do_install_append () {
	rm -f ${D}${sysconfdir}/localtime
}

do_install_locale () {
	dest=${D}/${includedir}/eglibc-locale-internal-${MULTIMACH_TARGET_SYS}
	install -d ${dest}${base_libdir} ${dest}${bindir} ${dest}${libdir} ${dest}${datadir}
	if [ "${base_libdir}" != "${libdir}" ]; then
		cp -fpPR ${D}${base_libdir}/* ${dest}${base_libdir}
	fi
	mv ${D}${bindir}/localedef ${dest}${bindir}
	mv ${D}${libdir}/gconv ${dest}${libdir}
	cp -fpPR ${D}${libdir}/* ${dest}${libdir}
        mv ${D}${datadir}/i18n ${dest}${datadir}
	cp -fpPR ${D}${datadir}/* ${dest}${datadir}
	cp -fpPR ${WORKDIR}/SUPPORTED ${dest}
}

addtask do_install_locale after do_install before do_populate_sysroot do_package

PACKAGE_PREPROCESS_FUNCS += "eglibc_package_preprocess"

eglibc_package_preprocess () {
	rm -rf ${PKGD}/${includedir}/eglibc-locale-internal-${MULTIMACH_TARGET_SYS}
}

