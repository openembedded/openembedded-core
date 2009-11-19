require gcc-cross_${PV}.bb
inherit crosssdk

SYSTEMHEADERS = "${SDKPATH}/include"
SYSTEMLIBS1 = "${SDKPATH}/lib/"

GCCMULTILIB = "--disable-multilib"

DEPENDS = "virtual/${TARGET_PREFIX}binutils-crosssdk virtual/${TARGET_PREFIX}libc-for-gcc-nativesdk"
PROVIDES = "virtual/${TARGET_PREFIX}gcc-crosssdk virtual/${TARGET_PREFIX}g++-crosssdk"

do_configure_prepend () {
	# Change the default dynamic linker path to the one in the SDK
	sed -i ${S}/gcc/config/*/linux*.h -e 's#\(GLIBC_DYNAMIC_LINKER.*\)/lib/#\1${SYSTEMLIBS1}#'
	sed -i ${S}/gcc/config/*/linux*.h -e 's#\(GLIBC_DYNAMIC_LINKER.*\)/lib64/#\1${SYSTEMLIBS1}#'
}
