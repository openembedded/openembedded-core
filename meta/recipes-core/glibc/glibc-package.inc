#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
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


# Binary locales are generated at build time if ENABLE_BINARY_LOCALE_GENERATION
# is set. The idea is to avoid running localedef on the target (at first boot)
# to decrease initial boot time and avoid localedef being killed by the OOM
# killer which used to effectively break i18n on machines with < 128MB RAM.

# default to disabled until qemu works for everyone
ENABLE_BINARY_LOCALE_GENERATION ?= "0"
ENABLE_BINARY_LOCALE_GENERATION_pn-glibc-nativesdk = "0"

# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.* i[3-6]86 x86_64 powerpc mips"

# set "1" to use cross-localedef for locale generation
# set "0" for qemu emulation of native localedef for locale generation
LOCALE_GENERATION_WITH_CROSS-LOCALEDEF = "1"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PACKAGES = "${PN}-dbg ${PN} catchsegv${PKGSUFFIX} sln${PKGSUFFIX} nscd${PKGSUFFIX} \
	ldd${PKGSUFFIX} localedef${PKGSUFFIX} ${PN}-utils ${PN}-dev ${PN}-doc \
	${PN}-locale libsegfault${PKGSUFFIX} ${PN}-extra-nss ${PN}-thread-db \
	${PN}-pcprofile"

PACKAGES_DYNAMIC = "glibc-gconv-*${PKGSUFFIX} glibc-charmap-* glibc-localedata-* locale-base-* \
	glibc-binary-localedata-*"

libc_baselibs = "${base_libdir}/libc* ${base_libdir}/libm* ${base_libdir}/ld* \
	${base_libdir}/libpthread* ${base_libdir}/libresolv* ${base_libdir}/librt* \
	${base_libdir}/libutil* ${base_libdir}/libnsl* ${base_libdir}/libnss_files* \
	${base_libdir}/libnss_compat* ${base_libdir}/libnss_dns* ${base_libdir}/libdl* \
	${base_libdir}/libanl* ${base_libdir}/libBrokenLocale*"

# The problem is that if PN = "glibc", FILES_${PN} will overwrite FILES_glibc
# Solution: Make them both the same thing, then it doesn't matter

glibcfiles = "${sysconfdir} ${libc_baselibs} ${base_sbindir}/ldconfig \
	${libexecdir}/* ${datadir}/zoneinfo"
glibcdbgfiles = "${bindir}/.debug ${sbindir}/.debug ${libdir}/.debug \
                  ${base_bindir}/.debug ${base_sbindir}/.debug ${base_libdir}/.debug \
                  ${libdir}/gconv/.debug ${libexecdir}/*/.debug"
glibcdevfiles = "${bindir}/rpcgen ${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la \
                ${libdir}/*.a ${libdir}/*.o ${libdir}/pkgconfig \
                ${base_libdir}/*.a ${base_libdir}/*.o ${datadir}/aclocal"

FILES_glibc = "${glibcfiles}"
FILES_${PN} = "${glibcfiles}"
FILES_ldd${PKGSUFFIX} = "${bindir}/ldd"
FILES_libsegfault${PKGSUFFIX} = "${base_libdir}/libSegFault*"
FILES_glibc-extra-nss = "${base_libdir}/libnss*"
FILES_sln = "${base_sbindir}/sln"
FILES_glibc-dev = "${glibcdevfiles}"
FILES_${PN}-dev = "${glibcdevfiles}"
FILES_glibc-dbg = "${glibcdbgfiles}"
FILES_${PN}-dbg = "${glibcdbgfiles}"
FILES_nscd${PKGSUFFIX} = "${sbindir}/nscd* ${sysconfdir}/nscd* ${sysconfdir}/init.d/nscd*"
FILES_glibc-utils = "${bindir}/* ${sbindir}/*"
FILES_glibc-gconv = "${libdir}/gconv/*"
FILES_catchsegv${PKGSUFFIX} = "${bindir}/catchsegv"
RDEPENDS_catchsegv${PKGSUFFIX} = "libsegfault"
FILES_glibc-pcprofile = "${base_libdir}/libpcprofile.so"
FILES_glibc-thread-db = "${base_libdir}/libthread_db*"
FILES_localedef${PKGSUFFIX} = "${bindir}/localedef"
RPROVIDES_glibc-dev += "libc-dev"

DESCRIPTION_sln = "glibc: create symbolic links between files"
DESCRIPTION_nscd = "glibc: name service cache daemon for passwd, group, and hosts"
DESCRIPTION_glibc-extra-nss = "glibc: nis, nisplus and hesiod search services"
DESCRIPTION_ldd = "glibc: print shared library dependencies"
DESCRIPTION_localedef = "glibc: compile locale definition files"
DESCRIPTION_glibc-utils = "glibc: misc utilities like iconf, local, gencat, tzselect, rpcinfo, ..."


EXTRA_OEMAKE += "rootsbindir=${base_sbindir}"

inherit libc-package
