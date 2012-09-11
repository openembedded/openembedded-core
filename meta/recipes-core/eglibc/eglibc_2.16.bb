require eglibc.inc

SRCREV = "20393"

DEPENDS += "gperf-native kconfig-frontends-native"
PR = "r10"
PR_append = "+svnr${SRCPV}"

EGLIBC_BRANCH="eglibc-2_16"
SRC_URI = "svn://www.eglibc.org/svn/branches/;module=${EGLIBC_BRANCH};protocol=http \
           file://eglibc-svn-arm-lowlevellock-include-tls.patch \
           file://IO-acquire-lock-fix.patch \
           file://mips-rld-map-check.patch \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           file://ppc-sqrt.patch \
           file://multilib_readlib.patch \
           file://use-sysroot-cxx-headers.patch \
           file://ppc-sqrt_finite.patch \
           file://GLRO_dl_debug_mask.patch \
           file://initgroups_keys.patch \
           file://eglibc_fix_findidx_parameters.patch \
           file://ppc_slow_ieee754_sqrt.patch \
           file://e500-math_private.patch \
           file://fileops-without-wchar-io.patch \
           file://add_resource_h_to_wait_h.patch \
           file://0001-Avoid-use-of-libgcc_s-and-libgcc_eh-when-building-gl.patch \
           file://0001-Add-ARM-specific-static-stubs.c.patch \
           file://0001-eglibc-menuconfig-support.patch \
           file://0002-eglibc-menuconfig-hex-string-options.patch \
           file://0003-eglibc-menuconfig-build-instructions.patch \
           file://0001-Add-name_to_handle_at-open_by_handle-etc.-to-PowerPC.patch \
           file://fsl-ppc-no-fsqrt.patch \
          "
LIC_FILES_CHKSUM = "file://LICENSES;md5=98a1128c4b58120182cbea3b1752d8b9 \
      file://COPYING;md5=393a5ca445f6965873eca0259a17f833 \
      file://posix/rxspencer/COPYRIGHT;md5=dc5485bb394a13b2332ec1c785f5d83a \
      file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff "

SRC_URI_append_virtclass-nativesdk = " file://ld-search-order.patch \
            file://relocatable_sdk.patch \
            "
S = "${WORKDIR}/${EGLIBC_BRANCH}/libc"
B = "${WORKDIR}/build-${TARGET_SYS}"

PACKAGES_DYNAMIC = "libc6*"
RPROVIDES_${PN}-dev = "libc6-dev virtual-libc-dev"
PROVIDES_${PN}-dbg = "glibc-dbg"

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE}"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${includedir}"

GLIBC_BROKEN_LOCALES = " _ER _ET so_ET yn_ER sid_ET tr_TR mn_MN gez_ET gez_ER bn_BD te_IN es_CR.ISO-8859-1"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/eglibc-${PV}', '${FILE_DIRNAME}/eglibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import re
    uc_os = (re.match('.*uclibc$', d.getVar('TARGET_OS', True)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   d.getVar('TARGET_OS', True))
}

export libc_cv_slibdir = "${base_libdir}"

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

do_unpack_append() {
    bb.build.exec_func('do_move_ports', d)
}

do_move_ports() {
        if test -d ${WORKDIR}/${EGLIBC_BRANCH}/ports ; then
	    rm -rf ${S}/ports
	    mv ${WORKDIR}/${EGLIBC_BRANCH}/ports ${S}/
	fi
}

do_patch_append() {
    bb.build.exec_func('do_fix_readlib_c', d)
}

# for mips eglibc now builds syscall tables for all abi's
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
		sed -i ${B}/elf/ldd -e 's#^\(RTLDLIST=\)"\(.*\)"$#\1\2#'
		sed -i ${B}/elf/ldd -e 's#^\(RTLDLIST=\)\(.*\)$#\1"${RTLDLIST} \2"#'
	fi

}

require eglibc-package.inc

BBCLASSEXTEND = "nativesdk"
