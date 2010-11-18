require linux-libc-headers.inc

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "unifdef-native"
PROVIDES = "linux-libc-headers"
PV = "2.6.34+git-${SRCPV}"
PR = "r1"

SRC_URI = "git://git.pokylinux.org/linux-2.6-windriver.git;fullclone=1"

S = "${WORKDIR}/linux"

set_arch() {
	case ${TARGET_ARCH} in
		arm*)     ARCH=arm ;;
		i*86*)    ARCH=i386 ;;
		ia64*)    ARCH=ia64 ;;
		mips*)    ARCH=mips ;;
		powerpc*) ARCH=powerpc ;;
		x86_64*)  ARCH=x86_64 ;;
	esac
}

do_configure() {
	set_arch
	oe_runmake allnoconfig ARCH=$ARCH
}

do_kernel_checkout() {
	if [ -d ${WORKDIR}/.git/refs/remotes/origin ]; then
		rm -rf ${S}
		mkdir ${S}
		mv ${WORKDIR}/.git ${S}
		mv ${S}/.git/refs/remotes/origin/* ${S}/.git/refs/heads
		rmdir ${S}/.git/refs/remotes/origin
	fi
	cd ${S}
	git checkout -f standard
}

addtask kernel_checkout before do_patch after do_unpack

do_compile () {
}

do_install() {
	set_arch
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix} ARCH=$ARCH
}

BBCLASSEXTEND = "nativesdk"
