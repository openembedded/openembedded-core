SRCREV="c2c684f88157f8bd0c236c7546f922fbf66b64a1"

PV = "0.9.33+git${SRCPV}"
PR = "${INC_PR}.4"

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/uclibc-git' ], d)}"

SRC_URI = "git://uclibc.org/uClibc.git;branch=master;protocol=git \
	file://uClibc.machine \
	file://uClibc.distro \
	file://obstack.cfg \
	file://locale.cfg \
	file://uclibc_enable_log2_test.patch \
	file://powerpc_copysignl.patch \
	file://argp-support.patch \
	file://argp-headers.patch \
	file://remove_attribute_optimize_Os.patch \
	file://compile-arm-fork-with-O2.patch \
	file://orign_path.patch \
	file://0001-atexit_old-Do-not-add-it-to-shared-libc.patch \
	file://0001-nptl-arm-Move-aeabi_read_tp-to-uclibc_nonshared.a.patch \
	file://0001-nptl-atfork-Hide-pthread_atfork-in-shared-versions.patch \
	file://0001-librt-Use-nodefaultlibs-instead-of-nostdlib.patch \
	file://0001-Revert-utent.c-wtent.c-move-functions-from-utxent.c.patch \
	file://0001-add-posix_fadvise-for-arm.patch \
	"
S = "${WORKDIR}/git"
