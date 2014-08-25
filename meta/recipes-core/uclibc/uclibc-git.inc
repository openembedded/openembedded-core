SRCREV = "48a0006012679ff0eda6f256da958d73a924fb57"

PV = "0.9.33+git${SRCPV}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/uclibc-git:"

SRC_URI = "git://uclibc.org/uClibc.git;branch=master \
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
	file://0001-Add-eventfd_read-and-eventfd_write.patch \
	file://0002-wire-setns-syscall.patch \
	file://0001-Define-IPTOS_CLASS_-macros-according-to-RFC-2474.patch \
	file://0001-timex-Sync-with-glibc.patch \
	"
S = "${WORKDIR}/git"
