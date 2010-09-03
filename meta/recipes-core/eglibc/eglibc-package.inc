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

# Binary locales are generated at build time if ENABLE_BINARY_LOCALE_GENERATION
# is set. The idea is to avoid running localedef on the target (at first boot)
# to decrease initial boot time and avoid localedef being killed by the OOM
# killer which used to effectively break i18n on machines with < 128MB RAM.

# default to disabled until qemu works for everyone
ENABLE_BINARY_LOCALE_GENERATION ?= "0"
ENABLE_BINARY_LOCALE_GENERATION_pn-eglibc-nativesdk = "0"

# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.* i[3-6]86 x86_64 powerpc mips"

# Set this to zero if you don't want ldconfig in the output package
USE_LDCONFIG ?= "1"

PACKAGES = "${PN}-dbg ${PN} catchsegv sln nscd ldd localedef ${PN}-utils ${PN}-pic ${PN}-dev eglibc-doc eglibc-locale libcidn libmemusage libsegfault eglibc-extra-nss eglibc-thread-db eglibc-pcprofile"

PACKAGES_DYNAMIC = " \
		    glibc-gconv-* glibc-charmap-* glibc-localedata-* glibc-binary-localedata-* \
		    eglibc-gconv-* eglibc-charmap-* eglibc-localedata-* eglibc-binary-localedata-* \
		    locale-base-*"

RPROVIDES_eglibc = "glibc"
RPROVIDES_eglibc-utils = "glibc-utils"
RPROVIDES_eglibc-pic = "glibc-pic"
RPROVIDES_eglibc-dev = "glibc-dev"
RPROVIDES_eglibc-doc = "glibc-doc"
RPROVIDES_eglibc-locale = "glibc-locale"
RPROVIDES_eglibc-extra-nss = "glibc-extra-nss"
RPROVIDES_eglibc-thread-db = "glibc-thread-db"
RPROVIDES_eglibc-pcprofile = "glibc-pcprofile"
RPROVIDES_eglibc-dbg = "glibc-dbg"
libc_baselibs = "${base_libdir}/libcrypt*.so.* ${base_libdir}/libcrypt-*.so ${base_libdir}/libc*.so.* ${base_libdir}/libc*.so ${base_libdir}/libm*.so.* ${base_libdir}/libm-*.so ${base_libdir}/ld*.so.* ${base_libdir}/ld-*.so ${base_libdir}/libpthread*.so.* ${base_libdir}/libpthread-*.so ${base_libdir}/libresolv*.so.* ${base_libdir}/libresolv-*.so ${base_libdir}/librt*.so.* ${base_libdir}/librt-*.so ${base_libdir}/libutil*.so.* ${base_libdir}/libutil-*.so ${base_libdir}/libnsl*.so.* ${base_libdir}/libnsl-*.so ${base_libdir}/libnss_files*.so.* ${base_libdir}/libnss_files-*.so ${base_libdir}/libnss_compat*.so.* ${base_libdir}/libnss_compat-*.so ${base_libdir}/libnss_dns*.so.* ${base_libdir}/libnss_dns-*.so ${base_libdir}/libdl*.so.* ${base_libdir}/libdl-*.so ${base_libdir}/libanl*.so.* ${base_libdir}/libanl-*.so ${base_libdir}/libBrokenLocale*.so.* ${base_libdir}/libBrokenLocale-*.so"

FILES_${PN} = "${libc_baselibs} ${libexecdir}/* ${@base_conditional('USE_LDCONFIG', '1', '${base_sbindir}/ldconfig', '', d)}"
FILES_ldd = "${bindir}/ldd"
FILES_libsegfault = "${base_libdir}/libSegFault*"
FILES_libcidn = "${base_libdir}/libcidn*.so"
FILES_libmemusage = "${base_libdir}/libmemusage.so"
FILES_eglibc-extra-nss = "${base_libdir}/libnss*"
FILES_sln = "/sbin/sln"
FILES_eglibc-pic = "${libdir}/*_pic.a ${libdir}/*_pic.map ${libdir}/libc_pic/"
FILES_eglibc-dev_append += "${bindir}/rpcgen ${libdir}/*.a \
	${base_libdir}/*.a ${base_libdir}/*.o ${datadir}/aclocal"
FILES_nscd = "${sbindir}/nscd*"
FILES_eglibc-utils = "${bindir}/* ${sbindir}/*"
FILES_eglibc-gconv = "${libdir}/gconv/*"
FILES_${PN}-dbg += "${libexecdir}/*/.debug ${libdir}/gconv/.debug"
FILES_catchsegv = "${bindir}/catchsegv"
RDEPENDS_catchsegv = "libsegfault"
FILES_eglibc-pcprofile = "${base_libdir}/libpcprofile.so"
FILES_eglibc-thread-db = "${base_libdir}/libthread_db*"
FILES_localedef = "${bindir}/localedef"
RPROVIDES_eglibc-dev += "libc-dev"

DESCRIPTION_sln = "eglibc: create symbolic links between files"
DESCRIPTION_nscd = "eglibc: name service cache daemon for passwd, group, and hosts"
DESCRIPTION_eglibc-extra-nss = "eglibc: nis, nisplus and hesiod search services"
DESCRIPTION_ldd = "eglibc: print shared library dependencies"
DESCRIPTION_localedef = "eglibc: compile locale definition files"
DESCRIPTION_eglibc-utils = "eglibc: misc utilities like iconf, local, gencat, tzselect, rpcinfo, ..."

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

