DESCRIPTION = "C library for embedded systems"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "required"

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
    if not uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

PROVIDES += "virtual/libc virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES += "${@['virtual/libiconv', ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']}"
DEPENDS = "virtual/${TARGET_PREFIX}binutils \
	   virtual/${TARGET_PREFIX}gcc-intermediate linux-libc-headers ncurses-native pax-utils-native"
RDEPENDS_${PN}-dev = "linux-libc-headers-dev"

# Blackfin needs a wrapper around ld
#DEPENDS_append_bfin = " elf2flt "

INHIBIT_DEFAULT_DEPS = "1"
PARALLEL_MAKE = ""

PACKAGES =+ "ldd uclibc-utils-dbg uclibc-utils uclibc-gconv uclibc-thread-db"

LEAD_SONAME = "libc.so"

# The last line (gdb and lib1) is for uclinux-uclibc builds 
uclibc_baselibs = "${base_libdir}/libcrypt*.so.* ${base_libdir}/libcrypt-*.so \
		   ${base_libdir}/libintl*.so.* ${base_libdir}/libintl-*.so \
		   ${base_libdir}/libnsl*.so.* ${base_libdir}/libnsl-*.so \
		   ${base_libdir}/libresolv*.so.* ${base_libdir}/libresolv-*.so \
                   ${base_libdir}/ld*.so.* ${base_libdir}/ld-*.so \
		   ${base_libdir}/libc*.so.* ${base_libdir}/libuClibc-*.so \
                   ${base_libdir}/libdl*.so.* ${base_libdir}/libdl-*.so \
		   ${base_libdir}/libm*.so.* ${base_libdir}/libm-*.so \
                   ${base_libdir}/libutil*.so.* ${base_libdir}/libutil-*.so \
		   ${base_libdir}/libpthread*.so.* ${base_libdir}/libpthread-*.so \
                   ${base_libdir}/librt*.so.* ${base_libdir}/librt-*.so \
                   ${libdir}/libc.gdb ${libdir}/libc ${base_libdir}/lib1.so \
                   "
FILES_${PN} = "${sysconfdir} ${uclibc_baselibs} /sbin/ldconfig \
	       ${libexecdir} ${datadir}/zoneinfo ${libdir}/locale"
FILES_ldd = "${bindir}/ldd"
FILES_uclibc-dev += "${libdir}/*.o"
FILES_uclibc-utils = "${bindir} ${sbindir}"
FILES_uclibc-utils-dbg += "${bindir}/.debug ${sbindir}/.debug"
FILES_uclibc-gconv = "${libdir}/gconv"
FILES_uclibc-thread-db = "/lib/libthread_db*"
RPROVIDES_uclibc-dev += "libc-dev"

#
# This locale file gets copied into uClibc-${PV}/extra/locale/ prior to
# build, it does not need to be unpacked, but we can't inhibit the unpacking
# in the current build system.
#
UCLIBC_LOCALE_FILE = "uClibc-locale-030818.tgz"
UCLIBC_LOCALE_FILE_arm = "uClibc-locale-030818.arm.tgz"
UCLIBC_LOCALE_URI = "http://www.uclibc.org/downloads/${UCLIBC_LOCALE_FILE}"
UCLIBC_LOCALE_URI_arm = "http://wiki.openembedded.net/dl/uclibc-locale/${UCLIBC_LOCALE_FILE}"

SRC_URI = "${@['${UCLIBC_LOCALE_URI}', ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']} \
	   file://uClibc.config \
           http://www.uclibc.org/downloads/uClibc-${PV}.tar.bz2 \
          "

# do_stage barfs on a CC with whitepspace, therefore put the 'HOST_CC_ARCH' in
# the CFLAGS (for when building the utils).
OEMAKE_NO_CC = "'STRIPTOOL=true' 'LD=${LD}' \
		'LOCALE_DATA_FILENAME=${UCLIBC_LOCALE_FILE}'"
EXTRA_OEMAKE = "${OEMAKE_NO_CC} 'CC=${CC}' \
		'HOSTCFLAGS=-I${STAGING_INCDIR_NATIVE}' \
		ARCH=`grep TARGET_ARCH ${S}/.config|sed -e 's/TARGET_ARCH=//g'`"
EXTRA_OEMAKE_task_do_package = "${OEMAKE_NO_CC}"

KERNEL_SOURCE = "${STAGING_INCDIR}"
KERNEL_HEADERS = "${STAGING_INCDIR}"

# Lets munge this via siteinfo.bbclass as well:
# ARCH_BIG_ENDIAN=y
# ARCH_WANTS_BIG_ENDIAN=y
# ARCH_WANTS_LITTLE_ENDIAN is not set

# How to enable verbose logs:
#export VERBOSE="1"

configmangle = 's,^KERNEL_SOURCE=.*,KERNEL_SOURCE="${KERNEL_SOURCE}",g; \
		s,^KERNEL_HEADERS=.*,KERNEL_HEADERS="${KERNEL_HEADERS}",g; \
		s,^RUNTIME_PREFIX=.*,RUNTIME_PREFIX="/",g; \
		s,^DEVEL_PREFIX=.*,DEVEL_PREFIX="/${prefix}",g; \
		s,^SHARED_LIB_LOADER_PATH=.*,SHARED_LIB_LOADER_PATH="/lib",; \
		s,^SHARED_LIB_LOADER_PREFIX=.*,SHARED_LIB_LOADER_PREFIX="/lib",; \
		s,.*UCLIBC_HAS_WCHAR.*,UCLIBC_HAS_WCHAR=y,g; \
		${@["","s,.*UCLIBC_HAS_LOCALE.*,UCLIBC_HAS_LOCALE=y,;"][bb.data.getVar("USE_NLS", d, 1) == "yes"]} \
		${@["","s,.*LDSO_GNU_HASH_SUPPORT.*,# LDSO_GNU_HASH_SUPPORT is not set,;"][bb.data.getVar("TARGET_ARCH", d, 1) in ['mips', 'mipsel', 'avr32']]} \
               '

CFLAGS := "${@oe_filter_out('-I\S+', '${CFLAGS}', d)}"

python () {
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		bb.data.setVar('configmangle_append', ' s,^HAS_FPU=y,# HAS_FPU is not set,;', d)
}

uclibcbuild_do_patch() {
	ln -sf ${STAGING_INCDIR}/linux ${S}/include/linux
	ln -sf ${STAGING_INCDIR}/asm ${S}/include/asm
	
	${@['cp %s/%s extra/locale' % (bb.data.getVar('DL_DIR', d, 1) or '', bb.data.getVar('UCLIBC_LOCALE_FILE', d, 1) or ''), ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']}
}

python do_patch () {
	bb.build.exec_func('base_do_patch', d)
	bb.build.exec_func('uclibcbuild_do_patch', d)
}

do_configure() {
	rm -f ${S}/.config

	# For uClibc 0.9.29, OpenEmbedded splits the uClibc.config in two parts:
	# uClibc.machine and uClibc.distro. So, if they exist use them, otherwise
	# use a uClibc.config
	if [ -f ${WORKDIR}/uClibc.machine -a -f ${WORKDIR}/uClibc.distro ]; then
		echo "### uClibc.machine ###" >${S}/merged.config
		cat ${WORKDIR}/uClibc.machine >>${S}/merged.config
		echo "### uClibc.distro ###" >>${S}/merged.config
		cat ${WORKDIR}/uClibc.distro >>${S}/merged.config
	else
		echo "### uClibc.config ###" >${S}/merged.config
		cat ${WORKDIR}/uClibc.config >>${S}/merged.config
	fi
	cp ${S}/merged.config ${S}/.config	

	# Mangle the resulting .config depending on OE variables
	perl -i -p -e 's,^CROSS=.*,TARGET_ARCH=${TARGET_ARCH}\nCROSS=${TARGET_PREFIX},g' ${S}/Rules.mak
	sed -i -e s:'$(CROSS)strip':true: ${S}/Rules.mak	
	perl -i -p -e '${configmangle}' ${S}/.config

   	sed -i -e '/CONFIG_ARM_EABI/d' ${S}/.config

	if [ `echo ${TARGET_ARCH} | grep -e '^arm'` ]; then
		if [ `echo ${TARGET_OS} | grep -e 'eabi$'` ]; then
       			echo "CONFIG_ARM_EABI=y"                >> ${S}/.config
		else
			echo "# CONFIG_ARM_EABI is not set"     >> ${S}/.config
		fi
	fi
	yes '' | oe_runmake oldconfig
}

do_install() {
	oe_runmake PREFIX=${D} DEVEL_PREFIX=${prefix}/ RUNTIME_PREFIX=/ \
		install_dev install_runtime 

	# Need to overwrite the version from -initial
	if [ ! -e ${D}${libdir}/libc.so ]; then
		ln -s ../../lib/libc.so.0 ${D}${libdir}/libc.so
	fi

	# We don't really need this in ${includedir}
	rm -f ${D}${prefix}/include/.cvsignore

	# This conflicts with the c++ version of this header
	rm -f ${D}${prefix}/include/bits/atomicity.h

        oe_runmake "SSP_ALL_CFLAGS=${TARGET_LINK_HASH_STYLE}" utils
        oe_runmake STRIPTOOL=true PREFIX=${D} DEVEL_PREFIX=${prefix}/ RUNTIME_PREFIX=/ \
                install_utils
	
	# oe_runstrip needs +x on files	
	chmod +x ${D}/${base_libdir}/*
}
