SRCREV="555ae2e88cd16a83f854634b6c3f35715b11d3d4"

DEFAULT_PREFERENCE = "-1"

PV = "0.9.33+git${SRCPV}"
PR = "${INC_PR}.3"

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
	file://dup3.patch \
	file://define-MSG_CMSG_CLOEXEC.patch \
	file://posix_fallocate.patch \
	file://0001-atexit_old-Do-not-add-it-to-shared-libc.patch \
	file://0001-nptl-arm-Move-aeabi_read_tp-to-uclibc_nonshared.a.patch \
	file://0001-nptl-atfork-Hide-pthread_atfork-in-shared-versions.patch \
	file://0001-librt-Use-nodefaultlibs-instead-of-nostdlib.patch \
	file://mount.h-update.patch \
	"
S = "${WORKDIR}/git"
