SRCREV = "fec308fdfaf9f557ef5fb17c308c48259012b825"

PR = "${INC_PR}.3"

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
	file://posix_fallocate.patch \
	file://mount.h-update.patch \
	"

S = "${WORKDIR}/git"
