SRCREV="71d63ed75648da9b0b71afabb9c60aaad792c55c"

require uclibc.inc
PV = "0.9.31+0.9.32rc3"
PR = "${INC_PR}.3"
PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-git' ], d)}"

SRC_URI = "git://uclibc.org/uClibc.git;branch=master;protocol=git \
	file://uClibc.machine \
	file://uClibc.distro \
	file://uclibc-arm-ftruncate64.patch \
	file://uclibc_enable_log2_test.patch \
	file://ldso_use_arm_dl_linux_resolve_in_thumb_mode.patch \
	file://reorder-use-BX.patch \
	file://select-force-thumb.patch \
	file://remove-sub-arch-variants.patch \
	file://transform-eabi-oabi-choice.patch \
	file://include-arm-asm.h.patch \
	file://detect-bx-availibility.patch \
	file://remove-eabi-oabi-selection.patch \
	file://powerpc_copysignl.patch \
	file://argp-support.patch \
	file://argp-headers.patch \
	file://remove_attribute_optimize_Os.patch \
	file://append_UCLIBC_EXTRA_CFLAGS.patch \
	file://compile-arm-fork-with-O2.patch \
	file://epoll-asm-fix.patch \
	"
S = "${WORKDIR}/git"
