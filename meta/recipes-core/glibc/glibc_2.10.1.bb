require glibc.inc

ARM_INSTRUCTION_SET = "arm"

PACKAGES_DYNAMIC = "libc6*"
RPROVIDES_${PN}-dev = "libc6-dev virtual-libc-dev"

PR = "r7"

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE}"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${includedir}"

GLIBC_ADDONS ?= "ports,nptl,libidn"

GLIBC_BROKEN_LOCALES = " _ER _ET so_ET yn_ER sid_ET tr_TR mn_MN gez_ET gez_ER bn_BD te_IN"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/glibc-${PV}', '${FILE_DIRNAME}/glibc-2.4', '${FILE_DIRNAME}/glibc', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"

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

RDEPENDS_${PN}-dev = "linux-libc-headers-dev"

#	   file://noinfo.patch;patch=1
#	   file://ldconfig.patch;patch=1;pnum=0
#	   file://arm-machine-gmon.patch;patch=1;pnum=0
#	   file://arm-ioperm.patch;patch=1;pnum=0
#	   file://ldd.patch;patch=1;pnum=0
SRC_URI = "${GNU_MIRROR}/glibc/glibc-${PV}.tar.bz2 \
           ${GNU_MIRROR}/glibc/glibc-ports-${PV}.tar.bz2 \
           ${GNU_MIRROR}/glibc/glibc-libidn-${PV}.tar.bz2 \
           file://nscd-init.patch;striplevel=0 \
           file://arm-memcpy.patch \
           file://arm-longlong.patch \
           file://fhs-linux-paths.patch \
           file://dl-cache-libcmp.patch \
           file://ldsocache-varrun.patch \
           file://nptl-crosscompile.patch \
           file://glibc-check_pf.patch;striplevel=0 \
           file://ldd-unbash.patch \
           file://glibc-arm-IO-acquire-lock-fix.diff \
           file://mips-rld-map-check.patch \
           file://generic-bits_select.h \
           file://generic-bits_types.h \
           file://generic-bits_typesizes.h \
           file://generic-bits_time.h \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           file://march-i686.patch;striplevel=0 \
           file://rulesfix.patch \
           file://glibc-2.9-enable-binutils-2.2.patch"

# Build fails on sh3 and sh4 without additional patches
SRC_URI_append_sh3 = " file://no-z-defs.patch"
SRC_URI_append_sh4 = " file://no-z-defs.patch"

#powerpc patches to add support for soft-float
SRC_URI_append_powerpc= " file://powerpc-sqrt-hack.diff"

S = "${WORKDIR}/glibc-${PV}"
B = "${WORKDIR}/build-${TARGET_SYS}"

# We need this for nativesdk
export libc_cv_slibdir = "${base_libdir}"

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
                --without-cvs --disable-profile --disable-debug --without-gd \
                --enable-clocale=gnu \
                --enable-add-ons=${GLIBC_ADDONS} \
                --with-headers=${STAGING_INCDIR} \
                --without-selinux \
                ${GLIBC_EXTRA_OECONF}"

EXTRA_OECONF += "${@get_libc_fpu_setting(bb, d)}"

do_munge() {
	# Integrate ports and libidn into tree
	mv ${WORKDIR}/glibc-ports-${PV} ${S}/ports
	mv ${WORKDIR}/glibc-libidn-${PV} ${S}/libidn

	# Ports isn't really working... Fix it
	# Some of this is rather dirty, but it seems to be the only
	# quick way to get this cruft to compile
	rm -rf ${S}/ports/sysdeps/unix/sysv/linux/arm/linuxthreads
	ln -s nptl ${S}/ports/sysdeps/unix/sysv/linux/arm/linuxthreads
	cp ${S}/nptl/sysdeps/pthread/bits/sigthread.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	cp ${S}/sysdeps/unix/sysv/linux/i386/bits/wchar.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	cp ${S}/sysdeps/wordsize-32/bits/wordsize.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	cp ${WORKDIR}/generic-bits_select.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/select.h
	cp ${WORKDIR}/generic-bits_types.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/types.h
	cp ${WORKDIR}/generic-bits_typesizes.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/typesizes.h
	cp ${WORKDIR}/generic-bits_time.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/time.h
	# Copy in generic stuff for not yet implemented headers
	for i in ${S}/bits/*.h; do
		F=`basename $i`
		[ "$F" = "local_lim.h" ] && continue
		[ "$F" = "errno.h" ] && continue
		test -e ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/$F || test -e ${S}/ports/sysdeps/arm/bits/$F || test -e ${S}/sysdeps/unix/sysv/linux/bits/$F || test -e ${S}/sysdeps/ieee754/bits/$F || cp $i ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	done
	# This is harmful; we need to get the one from nptl/sysdeps/pthreads
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/libc-lock.h
	# Obsoleted by sysdeps/arm/{fpu,eabi}/bits/fenv.h
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/fenv.h
	# Obsoleted by sysdeps/gnu/bits/utmp.h
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/utmp.h
}

addtask munge before do_patch after do_unpack


do_configure () {
# /var/db was not included to FHS
	sed -i s:/var/db/nscd:/var/run/nscd: ${S}/nscd/nscd.h
# override this function to avoid the autoconf/automake/aclocal/autoheader
# calls for now
# don't pass CPPFLAGS into configure, since it upsets the kernel-headers
# version check and doesn't really help with anything
	if [ -z "`which rpcgen`" ]; then
		echo "rpcgen not found.  Install glibc-devel."
		exit 1
	fi
	(cd ${S} && gnu-configize) || die "failure in running gnu-configize"
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
}

require glibc-stage.inc

require glibc-package.inc

BBCLASSEXTEND = "nativesdk"
