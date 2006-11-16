DESCRIPTION = "GNU C Library"
HOMEPAGE = "http://www.gnu.org/software/libc/libc.html"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "required"
# DEFAULT_PREFERENCE = "-1"
PV = "2.3.6+csl-arm-2005q3-2"
PR = "r10"

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_DIR}/${BUILD_SYS}/include"
TARGET_CPPFLAGS = "-I${STAGING_DIR}/${TARGET_SYS}/include"

FILESDIR = "${FILE_DIRNAME}/files"

GLIBC_ADDONS ?= "nptl,libidn"
GLIBC_EXTRA_OECONF ?= ""

GLIBC_BROKEN_LOCALES = "sid_ET tr_TR mn_MN gez_ET bn_BD gez_ER te_IN"

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
RDEPENDS_${PN}-dev = ""
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/arm-none-linux-gnueabi/arm-2005q3-2-arm-none-linux-gnueabi.src.tar.bz2 \
           file://nptl-crosscompile-2.3.6.patch;patch=1 \
           file://etc/ld.so.conf \
	   file://generate-supported.mk \
	   "

do_unpack2() {
	cd ${WORKDIR}
	tar -xvjf ./arm-2005q3-2-arm-none-linux-gnueabi/glibc-2005q3-2.tar.bz2
	rm -rf ./arm-2005q3-2-arm-none-linux-gnueabi
}

addtask unpack2 after do_unpack before do_patch

S = "${WORKDIR}/glibc-2.3.5pre"
B = "${WORKDIR}/build-${TARGET_SYS}"

inherit autotools

EXTRA_OECONF = "--without-cvs --disable-profile --disable-debug --without-gd \
		--enable-clocale=gnu \
	        --enable-add-ons \
		--with-headers=${STAGING_INCDIR} \
		--without-selinux \
		${GLIBC_EXTRA_OECONF}"

EXTRA_OECONF += "${@get_glibc_fpu_setting(bb, d)}"

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

require glibc-package.bbclass
