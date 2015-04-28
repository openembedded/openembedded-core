require glibc.inc

DEPENDS += "gperf-native kconfig-frontends-native"

SRCREV = "4e42b5b8f89f0e288e68be7ad70f9525aebc2cff"

BRANCH = "release/${PV}/master"

SRC_URI = "git://sourceware.org/git/glibc.git;branch=${BRANCH} \
           file://IO-acquire-lock-fix.patch \
           file://mips-rld-map-check.patch \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           file://glibc.fix_sqrt2.patch \
           file://multilib_readlib.patch \
           file://ppc-sqrt_finite.patch \
           file://ppc_slow_ieee754_sqrt.patch \
           file://add_resource_h_to_wait_h.patch \
           file://0001-R_ARM_TLS_DTPOFF32.patch \
           file://0001-eglibc-run-libm-err-tab.pl-with-specific-dirs-in-S.patch \
           file://fix-tibetian-locales.patch \
           file://ppce6500-32b_slow_ieee754_sqrt.patch \
           file://grok_gold.patch \
           file://fix_am_rootsbindir.patch \
           file://0001-Add-unused-attribute.patch \
           file://0001-When-disabling-SSE-also-make-sure-that-fpmath-is-not.patch \
           file://0001-yes-within-the-path-sets-wrong-config-variables.patch \
           file://elf-Makefile-fix-a-typo.patch \
           file://makesyscall.patch \
           file://Fix-__memcpy_chk-on-non-SSE2-CPUs.patch \
           ${EGLIBCPATCHES} \
           ${CVEPATCHES} \
          "
EGLIBCPATCHES = "\
           file://timezone-re-written-tzselect-as-posix-sh.patch \
           file://eglibc.patch \
           file://option-groups.patch \
           file://GLRO_dl_debug_mask.patch \
           file://eglibc-header-bootstrap.patch \
           file://eglibc-resolv-dynamic.patch \
           file://eglibc-ppc8xx-cache-line-workaround.patch \
           file://eglibc-sh4-fpscr_values.patch \
           file://eglibc-use-option-groups.patch \
          "
#           file://eglibc-install-pic-archives.patch \
#	    file://initgroups_keys.patch \
#
CVEPATCHES = "\
        file://CVE-2015-1781-resolv-nss_dns-dns-host.c-buffer-overf.patch \
"

LIC_FILES_CHKSUM = "file://LICENSES;md5=e9a558e243b36d3209f380deb394b213 \
      file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
      file://posix/rxspencer/COPYRIGHT;md5=dc5485bb394a13b2332ec1c785f5d83a \
      file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"

SRC_URI_append_class-nativesdk = " file://ld-search-order.patch \
            file://relocatable_sdk.patch \
            file://relocatable_sdk_fix_openpath.patch \
            "
S = "${WORKDIR}/git"
B = "${WORKDIR}/build-${TARGET_SYS}"

PACKAGES_DYNAMIC = ""

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE}"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${includedir}"

GLIBC_BROKEN_LOCALES = " _ER _ET so_ET yn_ER sid_ET tr_TR mn_MN gez_ET gez_ER bn_BD te_IN es_CR.ISO-8859-1"

#
# We will skip parsing glibc when target system C library selection is not glibc
# this helps in easing out parsing for non-glibc system libraries
#
COMPATIBLE_HOST_libc-musl_class-target = "null"
COMPATIBLE_HOST_libc-uclibc_class-target = "null"

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
                --without-cvs --disable-profile \
                --disable-debug --without-gd \
                --enable-clocale=gnu \
                --enable-add-ons \
                --with-headers=${STAGING_INCDIR} \
                --without-selinux \
                --enable-obsolete-rpc \
                --with-kconfig=${STAGING_BINDIR_NATIVE} \
                ${GLIBC_EXTRA_OECONF}"

EXTRA_OECONF += "${@get_libc_fpu_setting(bb, d)}"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'libc-inet-anl', '--enable-nscd', '--disable-nscd', d)}"


do_patch_append() {
    bb.build.exec_func('do_fix_readlib_c', d)
}

# for mips glibc now builds syscall tables for all abi's
# so we make sure that we choose right march option which is
# compatible with o32,n32 and n64 abi's
# e.g. -march=mips32 is not compatible with n32 and n64 therefore
# we filter it out in such case -march=from-abi which will be
# mips1 when using o32 and mips3 when using n32/n64

TUNE_CCARGS_mips := "${@oe_filter_out('-march=mips32', '${TUNE_CCARGS}', d)}"
TUNE_CCARGS_mipsel := "${@oe_filter_out('-march=mips32', '${TUNE_CCARGS}', d)}"

do_fix_readlib_c () {
	sed -i -e 's#OECORE_KNOWN_INTERPRETER_NAMES#${EGLIBC_KNOWN_INTERPRETER_NAMES}#' ${S}/elf/readlib.c
}

do_configure () {
# override this function to avoid the autoconf/automake/aclocal/autoheader
# calls for now
# don't pass CPPFLAGS into configure, since it upsets the kernel-headers
# version check and doesn't really help with anything
        if [ -z "`which rpcgen`" ]; then
                echo "rpcgen not found.  Install glibc-devel."
                exit 1
        fi
        (cd ${S} && gnu-configize) || die "failure in running gnu-configize"
        find ${S} -name "configure" | xargs touch
        CPPFLAGS="" oe_runconf
}

rpcsvc = "bootparam_prot.x nlm_prot.x rstat.x \
	  yppasswd.x klm_prot.x rex.x sm_inter.x mount.x \
	  rusers.x spray.x nfs_prot.x rquota.x key_prot.x"

do_compile () {
	# -Wl,-rpath-link <staging>/lib in LDFLAGS can cause breakage if another glibc is in staging
	unset LDFLAGS
	base_do_compile
	(
		cd ${S}/sunrpc/rpcsvc
		for r in ${rpcsvc}; do
			h=`echo $r|sed -e's,\.x$,.h,'`
			rpcgen -h $r -o $h || bbwarn "unable to generate header for $r"
		done
	)
	echo "Adjust ldd script"
	if [ -n "${RTLDLIST}" ]
	then
		prevrtld=`cat ${B}/elf/ldd | grep "^RTLDLIST=" | sed 's#^RTLDLIST="\?\([^"]*\)"\?$#\1#'`
		if [ "${prevrtld}" != "${RTLDLIST}" ]
		then
			sed -i ${B}/elf/ldd -e "s#^RTLDLIST=.*\$#RTLDLIST=\"${prevrtld} ${RTLDLIST}\"#"
		fi
	fi

}

require glibc-package.inc

BBCLASSEXTEND = "nativesdk"
