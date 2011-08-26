require eglibc.inc

SRCREV = "14157"

DEPENDS += "gperf-native"
PR = "r14"
PR_append = "+svnr${SRCPV}"

EGLIBC_BRANCH="eglibc-2_13"
SRC_URI = "svn://www.eglibc.org/svn/branches/;module=${EGLIBC_BRANCH};proto=http \
           file://eglibc-svn-arm-lowlevellock-include-tls.patch \
           file://IO-acquire-lock-fix.patch \
           file://shorten-build-commands.patch \
           file://mips-rld-map-check.patch \
           file://stack-protector-test.patch \
           file://armv4-eabi-compile-fix.patch \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           file://glibc_bug_fix_12454.patch \
           file://ppc-sqrt.patch \
           file://multilib_readlib.patch \
	   "
LIC_FILES_CHKSUM = "file://LICENSES;md5=98a1128c4b58120182cbea3b1752d8b9 \
      file://COPYING;md5=393a5ca445f6965873eca0259a17f833 \
      file://posix/rxspencer/COPYRIGHT;md5=dc5485bb394a13b2332ec1c785f5d83a \
      file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff "

SRC_URI_append_virtclass-nativesdk = " file://ld-search-order.patch"
S = "${WORKDIR}/${EGLIBC_BRANCH}/libc"
B = "${WORKDIR}/build-${TARGET_SYS}"

PACKAGES_DYNAMIC = "libc6*"
RPROVIDES_${PN}-dev = "libc6-dev virtual-libc-dev"
PROVIDES_${PN}-dbg = "glibc-dbg"

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE}"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${layout_includedir}"

GLIBC_ADDONS ?= "ports,nptl,libidn"

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
    import bb, re
    uc_os = (re.match('.*uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

export libc_cv_slibdir = "${base_libdir}"

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
                --without-cvs --disable-profile --disable-debug --without-gd \
                --enable-clocale=gnu \
                --enable-add-ons=${GLIBC_ADDONS},ports \
                --with-headers=${STAGING_INCDIR} \
                --without-selinux \
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
	bb.build.exec_func('do_fix_ia_headers', d)
	bb.build.exec_func('do_fix_readlib_c', d)
}

# We need to ensure that all of the i386 and x86_64 headers are identical
# to support the multilib case.  We do this by copying headers from x86_64
# to i386 directories.  Normally when hand building eglibc or a combined
# system you would build 32-bit, and then overwrite any files with the x86_64
# versions.
#
# Each time eglibc is updated, this will need to be re-evaluated.  In order
# to do this, disable this function.  Build eglibc for a 32-bit and a 64-bit
# IA32 target.  Compare the contents of the include files -- comments specific
# to the x86_64 version compared to the 32-bit one.
#
# For eglibc 2.13, each conflict noted below:
#  bits/a.out.h - Add support for __WORDSIZE = 64
#  bits/byteswap.h - Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/endian.h - Comment mismatch
#  bits/environment.h - add support for __WORDSIZE = 64
#  bits/fcntl.h - Comment/Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/fenv.h - Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/huge_vall.h - Comment/Copyright date mismatch, remove support for older gcc
#  bits/link.h - Function name difference, add x86_64 definitions
#  bits/mathdef.h - Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/mathinline.h - Copyright date mismatch, contributed by mismatch, remove support for older gcc/assembly optimization, add support for __WORDSIZE = 64
#  bits/mman.h - Header/Copyright date mismatch, add MAP_32BIT definition
#  bits/msq.h - Copyright date mismatch, add __WORDSIZE = 32 definitions
#  bits/pthread_type.h -- Contributed by added, add support for __WORDSIZE = 64
#  bits/select.h - Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/semaphore.h - Copyright date mismatch, add support for __WORDSIZE = 64
#  bits/sem.h - Copyright date mismatch
#  bits/setjmp.h - Copyrgiht date mismatch, add support for __WORDSIZE = 64
#  bits/shm.h - Copyright date mismatch, add support for __WORDSIZE = 32
#  bits/sigcontext.h - Copyright date mismatch, license wording mismatch, add support for __WORDSIZE = 32
#  bits/stat.h - Copyright date mismatch, add support for __WORDSIZE = 32 and __WORDSIZE = 64
#  bits/string.h - Header/Copyright date mismatch, remove assembly optimizations
#  bits/syscall.h - different order, some different syscalls listed
#  bits/wchar.h - Change the way the definitions are done
#  bits/wordsize.h - Different header, remove license notice, add __x86_64__ support
#  bits/xtitypes.h - Header difference, different typedef format
#  bits/fpu_control.h - header difference, revised comments, updated assembly macros
#  sys/debugreg.h - Copyright date mismatch, new definition and added __WORDSIZE=64 support
#  sys/epoll.h - Copyright date mismatch, slightly different definitions
#  sys/io.h - Copyright date mismatch, slightly different assembly formats
#  sys/perm.h - Copyright date mismatch
#  sys/procfs.h - Copyright date mismatch, support for __WORDSIZE = 32
#  sys/reg.h - Copyright date mismatch, support for __WORDSIZE = 64
#  sys/ucontext.h - Copyright date mismatch, support for __WORDSIZE = 64
#  sys/user.h - Copyright date mismatch, support for __WORDSIZE = 64
#
# we rm something to return to the default version
#
do_fix_ia_headers() {
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/a.out.h ${S}/sysdeps/unix/sysv/linux/i386/bits/a.out.h
	cp ${S}/sysdeps/x86_64/bits/byteswap.h ${S}/sysdeps/i386/bits/byteswap.h
	cp ${S}/sysdeps/x86_64/bits/endian.h ${S}/sysdeps/i386/bits/endian.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/environments.h ${S}/sysdeps/unix/sysv/linux/i386/bits/environments.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/fcntl.h ${S}/sysdeps/unix/sysv/linux/i386/bits/fcntl.h
	cp ${S}/sysdeps/x86_64/fpu/bits/fenv.h ${S}/sysdeps/i386/fpu/bits/fenv.h
	rm ${S}/sysdeps/i386/bits/huge_vall.h
	cp ${S}/sysdeps/x86_64/bits/link.h ${S}/sysdeps/i386/bits/link.h
	cp ${S}/sysdeps/x86_64/bits/mathdef.h ${S}/sysdeps/i386/bits/mathdef.h
	cp ${S}/sysdeps/x86_64/fpu/bits/mathinline.h ${S}/sysdeps/i386/fpu/bits/mathinline.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/mman.h ${S}/sysdeps/unix/sysv/linux/i386/bits/mman.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/msq.h ${S}/sysdeps/unix/sysv/linux/i386/bits/msq.h
	cp ${S}/nptl/sysdeps/unix/sysv/linux/x86_64/bits/pthreadtypes.h ${S}/nptl/sysdeps/unix/sysv/linux/i386/bits/pthreadtypes.h
	cp ${S}/sysdeps/x86_64/bits/select.h ${S}/sysdeps/i386/bits/select.h
	cp ${S}/nptl/sysdeps/unix/sysv/linux/x86_64/bits/semaphore.h ${S}/nptl/sysdeps/unix/sysv/linux/i386/bits/semaphore.h
	rm ${S}/sysdeps/unix/sysv/linux/x86_64/bits/sem.h
	cp ${S}/sysdeps/x86_64/bits/setjmp.h ${S}/sysdeps/i386/bits/setjmp.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/shm.h ${S}/sysdeps/unix/sysv/linux/i386/bits/shm.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/sigcontext.h ${S}/sysdeps/unix/sysv/linux/i386/bits/sigcontext.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/bits/stat.h ${S}/sysdeps/unix/sysv/linux/i386/bits/stat.h
	rm ${S}/sysdeps/i386/i486/bits/string.h ; cp ${S}/sysdeps/x86_64/bits/string.h ${S}/sysdeps/i386/bits/string.h
	# Skip syscall.h, see do_install
	rm ${S}/sysdeps/unix/sysv/linux/i386/bits/wchar.h
	cp ${S}/sysdeps/x86_64/bits/wordsize.h ${S}/sysdeps/i386/bits/wordsize.h
	cp ${S}/sysdeps/x86_64/bits/xtitypes.h ${S}/sysdeps/i386/bits/xtitypes.h
	# i386 version is correct, x86_64 is incorrect for fpu_control.h
	cp ${S}/sysdeps/i386/fpu_control.h ${S}/sysdeps/x86_64/fpu_control.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/debugreg.h ${S}/sysdeps/unix/sysv/linux/i386/sys/debugreg.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/epoll.h ${S}/sysdeps/unix/sysv/linux/i386/sys/epoll.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/io.h ${S}/sysdeps/unix/sysv/linux/i386/sys/io.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/perm.h ${S}/sysdeps/unix/sysv/linux/i386/sys/perm.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/procfs.h ${S}/sysdeps/unix/sysv/linux/i386/sys/procfs.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/reg.h ${S}/sysdeps/unix/sysv/linux/i386/sys/reg.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/ucontext.h ${S}/sysdeps/unix/sysv/linux/i386/sys/ucontext.h
	cp ${S}/sysdeps/unix/sysv/linux/x86_64/sys/user.h ${S}/sysdeps/unix/sysv/linux/i386/sys/user.h
}

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
			rpcgen -h $r -o $h || oewarn "unable to generate header for $r"
		done
	)
	echo "Adjust ldd script"
	[ -z "${RTLDLIST}" ] && return
	sed -i ${B}/elf/ldd -e 's#^\(RTLDLIST=\)"\(.*\)"$#\1\2#'
	sed -i ${B}/elf/ldd -e 's#^\(RTLDLIST=\)\(.*\)$#\1"${RTLDLIST} \2"#'

}

require eglibc-package.inc

BBCLASSEXTEND = "nativesdk"
