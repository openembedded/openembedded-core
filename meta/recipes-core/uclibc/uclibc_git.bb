SRCREV="a4421e60526d3420a9c0bd55bfa4a25a746cbc8e"

require uclibc.inc

# We prefer a release version so DP -1 for this
DEFAULT_PREFERENCE = "-1"

PV = "0.9.32+0.9.33-rc0"
PR = "${INC_PR}.1"
PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-git' ], d)}"

SRC_URI = "git://busybox.osuosl.org/uClibc.git;branch=master;protocol=git \
	file://uClibc.machine \
	file://uClibc.distro \
	file://uclibc-arm-ftruncate64.patch \
	file://uclibc_enable_log2_test.patch \
	file://ldso_use_arm_dl_linux_resolve_in_thumb_mode.patch \
	file://powerpc_copysignl.patch \
	file://argp-support.patch \
	file://argp-headers.patch \
	file://remove_attribute_optimize_Os.patch \
	file://compile-arm-fork-with-O2.patch \
	file://uclibc-execvpe.patch \
	file://fix_libdl.patch \
	file://cross_compile_check.patch \
	"

S = "${WORKDIR}/git"
