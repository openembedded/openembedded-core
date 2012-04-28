SRCREV="7bad8cf68b6e2434686bd80182f4a5bc22004416"

require uclibc.inc

# We prefer a release version so DP -1 for this
DEFAULT_PREFERENCE = "-1"

PV = "0.9.33+git${SRCPV}"
PR = "${INC_PR}.1"
PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-git' ], d)}"

SRC_URI = "git://uclibc.org/uClibc.git;branch=master;protocol=git \
	file://uClibc.machine \
	file://uClibc.distro \
	file://uclibc_enable_log2_test.patch \
	file://ldso_use_arm_dl_linux_resolve_in_thumb_mode.patch \
	file://powerpc_copysignl.patch \
	file://argp-support.patch \
	file://argp-headers.patch \
	file://remove_attribute_optimize_Os.patch \
	file://compile-arm-fork-with-O2.patch \
	file://uclibc-execvpe.patch \
	file://orign_path.patch \
	"

S = "${WORKDIR}/git"
