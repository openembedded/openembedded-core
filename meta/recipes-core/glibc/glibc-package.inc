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

# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.* i[3-6]86 x86_64 powerpc mips"

PACKAGES = "glibc-dbg glibc catchsegv sln nscd ldd localedef glibc-utils glibc-dev glibc-doc glibc-locale libsegfault glibc-extra-nss glibc-thread-db glibc-pcprofile"
PACKAGES_DYNAMIC = "glibc-gconv-* glibc-charmap-* glibc-localedata-* locale-base-* glibc-binary-localedata-*"

libc_baselibs = "${base_libdir}/libc* ${base_libdir}/libm* ${base_libdir}/ld* ${base_libdir}/libpthread* ${base_libdir}/libresolv* ${base_libdir}/librt* ${base_libdir}/libutil* ${base_libdir}/libnsl* ${base_libdir}/libnss_files* ${base_libdir}/libnss_compat* ${base_libdir}/libnss_dns* ${base_libdir}/libdl* ${base_libdir}/libanl* ${base_libdir}/libBrokenLocale*"

# The problem is that if PN = "glibc", FILES_${PN} will overwrite FILES_glibc
# Solution: Make them both the same thing, then it doesn't matter

glibcfiles = "${sysconfdir} ${libc_baselibs} ${base_sbindir}/ldconfig ${libexecdir}/* ${datadir}/zoneinfo"
glibcdbgfiles = "${bindir}/.debug ${sbindir}/.debug ${libdir}/.debug \
                  ${base_bindir}/.debug ${base_sbindir}/.debug ${base_libdir}/.debug \
                  ${libdir}/gconv/.debug ${libexecdir}/*/.debug"
glibcdevfiles = "${bindir}/rpcgen ${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la \
                ${libdir}/*.a ${libdir}/*.o ${libdir}/pkgconfig \
                ${base_libdir}/*.a ${base_libdir}/*.o ${datadir}/aclocal"

FILES_glibc = "${glibcfiles}"
FILES_${PN} = "${glibcfiles}"
FILES_ldd = "${bindir}/ldd"
FILES_libsegfault = "${base_libdir}/libSegFault*"
FILES_glibc-extra-nss = "${base_libdir}/libnss*"
FILES_sln = "${base_sbindir}/sln"
FILES_glibc-dev = "${glibcdevfiles}"
FILES_${PN}-dev = "${glibcdevfiles}"
FILES_glibc-dbg = "${glibcdbgfiles}"
FILES_${PN}-dbg = "${glibcdbgfiles}"
FILES_nscd = "${sbindir}/nscd* ${sysconfdir}/nscd* ${sysconfdir}/init.d/nscd*"
FILES_glibc-utils = "${bindir}/* ${sbindir}/*"
FILES_glibc-gconv = "${libdir}/gconv/*"
FILES_catchsegv = "${bindir}/catchsegv"
RDEPENDS_catchsegv = "libsegfault"
FILES_glibc-pcprofile = "${base_libdir}/libpcprofile.so"
FILES_glibc-thread-db = "${base_libdir}/libthread_db*"
FILES_localedef = "${bindir}/localedef"
RPROVIDES_glibc-dev += "libc-dev"

DESCRIPTION_sln = "glibc: create symbolic links between files"
DESCRIPTION_nscd = "glibc: name service cache daemon for passwd, group, and hosts"
DESCRIPTION_glibc-extra-nss = "glibc: nis, nisplus and hesiod search services"
DESCRIPTION_ldd = "glibc: print shared library dependencies"
DESCRIPTION_localedef = "glibc: compile locale definition files"
DESCRIPTION_glibc-utils = "glibc: misc utilities like iconf, local, gencat, tzselect, rpcinfo, ..."


EXTRA_OEMAKE += "rootsbindir=${base_sbindir}"

python __anonymous () {
    enabled = bb.data.getVar("ENABLE_BINARY_LOCALE_GENERATION", d, 1)

    if enabled and int(enabled):
        import re

        target_arch = bb.data.getVar("TARGET_ARCH", d, 1)
        binary_arches = bb.data.getVar("BINARY_LOCALE_ARCHES", d, 1) or ""

        for regexp in binary_arches.split(" "):
            r = re.compile(regexp)

            if r.match(target_arch):
                depends = bb.data.getVar("DEPENDS", d, 1)
                depends = "%s qemu-native" % depends
                bb.data.setVar("DEPENDS", depends, d)
                bb.data.setVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", "compile", d)
                break
}

inherit libc-package
