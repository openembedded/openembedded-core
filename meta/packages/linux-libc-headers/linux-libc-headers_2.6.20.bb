require linux-libc-headers.inc

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "unifdef-native"
PR = "r3"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-${PV}.tar.bz2"

S = "${WORKDIR}/linux-${PV}"

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

do_configure() {
	set_arch
	oe_runmake allnoconfig ARCH=${ARCH}
}

do_compile () {
}

do_install() {
	set_arch
	oe_runmake headers_install INSTALL_HDR_PATH=${D}/usr ARCH=$ARCH
}

do_install_append_arm() {
	cp include/asm-arm/procinfo.h ${D}${includedir}/asm
}

STAGE_TEMP="${WORKDIR}/temp-staging"

do_stage () {
	set_arch
	rm -rf ${STAGE_TEMP}
	mkdir -p ${STAGE_TEMP}
	oe_runmake headers_install INSTALL_HDR_PATH=${STAGE_TEMP}/usr ARCH=${ARCH}
	if [ "$ARCH" == "arm" ]; then
		cp include/asm-arm/procinfo.h ${STAGE_TEMP}${includedir}/asm
	fi
	install -d ${STAGING_INCDIR}
	rm -rf ${STAGING_INCDIR}/linux ${STAGING_INCDIR}/asm ${STAGING_INCDIR}/asm-generic
	cp -pfLR ${STAGE_TEMP}${includedir}/linux ${STAGING_INCDIR}/
	cp -pfLR ${STAGE_TEMP}${includedir}/asm ${STAGING_INCDIR}/
	cp -pfLR ${STAGE_TEMP}${includedir}/asm-generic ${STAGING_INCDIR}/
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/linux
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm-generic
	install -d ${CROSS_DIR}/${TARGET_SYS}/include
	cp -pfLR ${STAGE_TEMP}${includedir}/linux ${CROSS_DIR}/${TARGET_SYS}/include/
	cp -pfLR ${STAGE_TEMP}${includedir}/asm ${CROSS_DIR}/${TARGET_SYS}/include/
	cp -pfLR ${STAGE_TEMP}${includedir}/asm-generic ${CROSS_DIR}/${TARGET_SYS}/include/
}
