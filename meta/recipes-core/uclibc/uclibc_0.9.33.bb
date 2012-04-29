SRCREV="f7c18471f1ff8f3e54e462df0e96e19739869c78"

require uclibc.inc

PR = "${INC_PR}.1"
PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

SRC_URI = "git://uclibc.org/uClibc.git;branch=${PV};protocol=git \
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
	file://dup3.patch \
	file://define-MSG_CMSG_CLOEXEC.patch \
	"

S = "${WORKDIR}/git"
