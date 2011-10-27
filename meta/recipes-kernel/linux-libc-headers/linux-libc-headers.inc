DESCRIPTION = "Sanitized set of 2.6 kernel headers for the C library's use."
SECTION = "devel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7" 
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v2.6/linux-${PV}.tar.bz2"

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
		avr32*)   ARCH=avr32 ;;
		bfin*)    ARCH=blackfin ;;
		microblaze*) ARCH=microblaze ;;
	esac
}

do_configure() {
	set_arch
	oe_runmake allnoconfig ARCH=$ARCH
}

do_compile () {
}

do_install() {
	set_arch
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix} ARCH=$ARCH
	# Kernel should not be exporting this header
	rm -f ${D}${exec_prefix}/include/scsi/scsi.h

	# The ..install.cmd conflicts between various configure runs
	find ${D}${includedir} -name ..install.cmd | xargs rm -f
}

BBCLASSEXTEND = "nativesdk"

#DEPENDS = "cross-linkage"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "unifdef-native"