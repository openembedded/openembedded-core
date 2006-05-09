SECTION = "devel"
DESCRIPTION = "Sanitized set of 2.6 kernel headers for the C library's use."
# This package is derived from the original linux-libc-headers at
#   http://ep09.pld-linux.org/~mmazur/linux-libc-headers/
# More specifically, llh-2.6.12.0 was patched up to 2.6.16-rc6 with 
# the official linux patches (where applicable) and then fixed up just
# enough to build glibc-2.4. BEWARE!
#
# license note from the linux-libc-headers package: 
#   Linux-libc-headers are derived from linux kernel headers. For license of a
#   particular header, check it's content, and if copyright notice isn't present,
#   standard linux kernel license applies. 
# since we assume GPL for linux i think we can also assume it here
LICENSE = "GPL"
DEFAULT_PREFERENCE = "-1"
INHIBIT_DEFAULT_DEPS = "1"
PR = "r4"

SRC_URI = "http://ewi546.ewi.utwente.nl/OE/eabi/linux-libc-headers-${PV}.tar.bz2 \
	file://keyboard.patch;patch=1 \
	file://asm-arch-irqs.patch;patch=1 \
	file://linux-netdevice.patch;patch=1 \
	file://linux-netfilter_ipv4.patch;patch=1 \
	file://linux-rtc.patch;patch=1 \
	file://linux-videodev2.patch;patch=1 \
	file://3477-1.patch;patch=1"

S = "${WORKDIR}/linux-libc-headers-${PV}"

do_configure () {
	case ${TARGET_ARCH} in
		alpha*)   ARCH=alpha ;;
		arm*)     ARCH=arm ;;
		cris*)    ARCH=cris ;;
		hppa*)    ARCH=parisc ;;
		i*86*)    ARCH=i386 ;;
		ia64*)    ARCH=ia64 ;;
		mips*)    ARCH=mips ;;
		m68k*)    ARCH=m68k ;;
		powerpc*) ARCH=ppc ;;
		s390*)    ARCH=s390 ;;
		sh*)      ARCH=sh ;;
		sparc64*) ARCH=sparc64 ;;
		sparc*)   ARCH=sparc ;;
		x86_64*)  ARCH=x86_64 ;;
	esac
	if test !  -e include/asm-$ARCH; then
		oefatal unable to create asm symlink in kernel headers
	fi
	rm "include/asm"
	cp -pPR "include/asm-$ARCH" "include/asm"
	if test "$ARCH" = "arm"; then
		cp -pPR include/asm/arch-ebsa285 include/asm/arch
	elif test "$ARCH" = "sh"; then
		cp -pPR include/asm/cpu-${TARGET_ARCH} include/asm/cpu || die "unable to create include/asm/cpu"
	fi
}

do_stage () {
	install -d ${STAGING_INCDIR}
	rm -rf ${STAGING_INCDIR}/linux ${STAGING_INCDIR}/asm ${STAGING_INCDIR}/asm-generic
	cp -pfLR include/linux ${STAGING_INCDIR}/
	cp -pfLR include/asm ${STAGING_INCDIR}/
	cp -pfLR include/asm-generic ${STAGING_INCDIR}/
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/linux
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm
	rm -rf ${CROSS_DIR}/${TARGET_SYS}/include/asm-generic
	install -d ${CROSS_DIR}/${TARGET_SYS}/include
	cp -pfLR include/linux ${CROSS_DIR}/${TARGET_SYS}/include/
	cp -pfLR include/asm ${CROSS_DIR}/${TARGET_SYS}/include/
	cp -pfLR include/asm-generic ${CROSS_DIR}/${TARGET_SYS}/include/
}

do_install() {
	install -d ${D}${includedir}
	cp -pfLR include/linux ${D}${includedir}/
	cp -pfLR include/asm ${D}${includedir}/
	cp -pfLR include/asm-generic ${D}${includedir}/
}

