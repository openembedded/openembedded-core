DESCRIPTION = "GNU C Library"
HOMEPAGE = "http://www.gnu.org/software/libc/libc.html"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "required"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/glibc-cvs-2.3.5"
SRCDATE = "20050627"
PR = "r1"

GLIBC_ADDONS ?= "ports,linuxthreads"
GLIBC_EXTRA_OECONF ?= ""

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

# nptl needs unwind support in gcc, which can't be built without glibc.
PROVIDES = "virtual/libc ${@['virtual/${TARGET_PREFIX}libc-for-gcc', '']['nptl' in '${GLIBC_ADDONS}']}"
PROVIDES += "virtual/libintl virtual/libiconv"
DEPENDS = "${@['virtual/${TARGET_PREFIX}gcc-initial', 'virtual/${TARGET_PREFIX}gcc']['nptl' in '${GLIBC_ADDONS}']} linux-libc-headers"
RDEPENDS_${PN}-dev = "linux-libc-headers-dev"
INHIBIT_DEFAULT_DEPS = "1"

#	   file://noinfo.patch;patch=1
#	   file://ldconfig.patch;patch=1;pnum=0
#	   file://arm-machine-gmon.patch;patch=1;pnum=0 \
#	   \
#	   file://arm-ioperm.patch;patch=1;pnum=0 \
#	   file://ldd.patch;patch=1;pnum=0 \
SRC_URI = "cvs://anoncvs@sources.redhat.com/cvs/glibc;module=libc \
	   cvs://anoncvs@sources.redhat.com/cvs/glibc;module=ports \
	   file://arm-audit.patch;patch=1 \
	   file://arm-audit2.patch;patch=1 \
	   file://arm-no-hwcap.patch;patch=1 \
	   file://arm-memcpy.patch;patch=1 \
	   file://arm-longlong.patch;patch=1;pnum=0 \
	   file://fhs-linux-paths.patch;patch=1 \
	   file://dl-cache-libcmp.patch;patch=1 \
	   file://ldsocache-varrun.patch;patch=1 \
           file://etc/ld.so.conf \
	   file://generate-supported.mk"

# seems to fail on tls platforms
SRC_URI_append_arm = " file://dyn-ldconfig-20041128.patch;patch=1"

S = "${WORKDIR}/libc"
B = "${WORKDIR}/build-${TARGET_SYS}"

inherit autotools

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
	        --without-cvs --disable-profile --disable-debug --without-gd \
		--enable-clocale=gnu \
	        --enable-add-ons=${GLIBC_ADDONS} \
		--with-headers=${CROSS_DIR}/${TARGET_SYS}/include \
		--without-selinux \
		${GLIBC_EXTRA_OECONF}"

EXTRA_OECONF += "${@get_glibc_fpu_setting(bb, d)}"

def get_glibc_fpu_setting(bb, d):
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		return "--without-fp"
	return ""

do_munge() {
	# Integrate ports into tree
	mv ${WORKDIR}/ports ${S}

	# http://www.handhelds.org/hypermail/oe/51/5135.html
	# Some files were moved around between directories on
	# 2005-12-21, which means that any attempt to check out
	# from CVS using a datestamp older than that will be doomed.
	#
	# This is a workaround for that problem.
	rm -rf ${S}/bits
}

addtask munge before do_patch after do_unpack

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
}

do_stage() {
	rm -f ${STAGING_LIBDIR}/libc.so.6
	oe_runmake 'install_root=${STAGING_DIR}/${HOST_SYS}' \
		   'includedir=/include' 'libdir=/lib' 'slibdir=/lib' \
		   '${STAGING_LIBDIR}/libc.so.6' \
		   install-headers install-lib

	install -d ${STAGING_INCDIR}/gnu \
		   ${STAGING_INCDIR}/bits \
		   ${STAGING_INCDIR}/rpcsvc
	install -m 0644 ${S}/include/gnu/stubs.h ${STAGING_INCDIR}/gnu/
	install -m 0644 ${B}/bits/stdio_lim.h ${STAGING_INCDIR}/bits/
	install -m 0644 misc/syscall-list.h ${STAGING_INCDIR}/bits/syscall.h
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${STAGING_INCDIR}/rpcsvc/
	done
	for i in libc.a libc_pic.a libc_nonshared.a; do
		install -m 0644 ${B}/$i ${STAGING_LIBDIR}/ || die "failed to install $i"
	done
	echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${STAGING_LIBDIR}/libpthread.so
	echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${STAGING_LIBDIR}/libc.so

	rm -f ${CROSS_DIR}/${TARGET_SYS}/lib/libc.so.6
	oe_runmake 'install_root=${CROSS_DIR}/${TARGET_SYS}' \
		   'includedir=/include' 'libdir=/lib' 'slibdir=/lib' \
		   '${CROSS_DIR}/${TARGET_SYS}/lib/libc.so.6' \
		   install-headers install-lib

	install -d ${CROSS_DIR}/${TARGET_SYS}/include/gnu \
		   ${CROSS_DIR}/${TARGET_SYS}/include/bits \
		   ${CROSS_DIR}/${TARGET_SYS}/include/rpcsvc
	install -m 0644 ${S}/include/gnu/stubs.h ${CROSS_DIR}/${TARGET_SYS}/include/gnu/
	install -m 0644 ${B}/bits/stdio_lim.h ${CROSS_DIR}/${TARGET_SYS}/include/bits/
	install -m 0644 misc/syscall-list.h ${CROSS_DIR}/${TARGET_SYS}/include/bits/syscall.h
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${CROSS_DIR}/${TARGET_SYS}/include/rpcsvc/
	done

	for i in libc.a libc_pic.a libc_nonshared.a; do
		install -m 0644 ${B}/$i ${CROSS_DIR}/${TARGET_SYS}/lib/ || die "failed to install $i"
	done
	echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${CROSS_DIR}/${TARGET_SYS}/lib/libpthread.so
	echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${CROSS_DIR}/${TARGET_SYS}/lib/libc.so
}

include glibc-package.bbclass
