SRCREV="0dcc13bf7a61b1d0708e5dd103d5515e0ffec79a"

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
	file://powerpc_copysignl.patch \
	file://argp-support.patch \
	file://argp-headers.patch \
	file://remove_attribute_optimize_Os.patch \
	file://compile-arm-fork-with-O2.patch \
	file://uclibc-execvpe.patch \
	file://orign_path.patch \
	"

S = "${WORKDIR}/git"
