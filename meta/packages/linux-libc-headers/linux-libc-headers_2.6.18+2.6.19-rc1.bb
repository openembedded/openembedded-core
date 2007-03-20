require linux-libc-headers.inc

DEFAULT_PREFERENCE = "-1"
INHIBIT_DEFAULT_DEPS = "1"
PR = "r1"

SRC_URI = " \
	${KERNELORG_MIRROR}/pub/linux/kernel/people/dwmw2/kernel-headers/snapshot/linux-kernel-headers-2.6.19-rc1.tar.bz2 \
	file://arm-procinfo-hwcap.patch;patch=1 \
	file://arm-unistd-syscall.patch;patch=1 \
	file://linux-err.patch;patch=1 \
"

S = "${WORKDIR}/linux-2.6.19-rc1"

set_arch() {
	case ${TARGET_ARCH} in
		alpha*)   ARCH=alpha ;;
		arm*)     ARCH=arm ;;
		cris*)    ARCH=cris ;;
		hppa*)    ARCH=parisc ;;
		i*86*)    ARCH=i386 ;;
		ia64*)    ARCH=ia64 ;;
		mips*)    ARCH=mips ;;
		m68k*)    ARCH=m68k ;;
		powerpc*) ARCH=powerpc ;;
		s390*)    ARCH=s390 ;;
		sh*)      ARCH=sh ;;
		sparc64*) ARCH=sparc64 ;;
		sparc*)   ARCH=sparc ;;
		x86_64*)  ARCH=x86_64 ;;
	esac
}

do_install() {
	set_arch
	install -d ${D}${includedir}
	cp -pfLR ${S}${includedir}/linux ${D}${includedir}/
	cp -pfLR ${S}${includedir}/asm-$ARCH ${D}${includedir}/asm
	cp -pfLR ${S}${includedir}/asm-generic ${D}${includedir}/
}

do_stage () {
	set_arch
	install -d ${STAGING_INCDIR}
	rm -rf ${STAGING_INCDIR}/linux ${STAGING_INCDIR}/asm ${STAGING_INCDIR}/asm-generic
	cp -pfLR ${S}${includedir}/linux ${STAGING_INCDIR}/
	cp -pfLR ${S}${includedir}/asm-$ARCH ${STAGING_INCDIR}/asm
	cp -pfLR ${S}${includedir}/asm-generic ${STAGING_INCDIR}/
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/linux
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm-generic
	install -d ${CROSS_DIR}/${TARGET_SYS}/include
	cp -pfLR ${S}${includedir}/linux ${CROSS_DIR}/${TARGET_SYS}/include/
	cp -pfLR ${S}${includedir}/asm-${ARCH} ${CROSS_DIR}/${TARGET_SYS}/include/asm
	cp -pfLR ${S}${includedir}/asm-generic ${CROSS_DIR}/${TARGET_SYS}/include/
}
