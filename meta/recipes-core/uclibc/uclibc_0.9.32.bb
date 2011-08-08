SRCREV="9152c4d67c763fde5712e2d181d92c0d7e1e2ab9"

require uclibc.inc
PR = "${INC_PR}.3"
PROVIDES += "virtual/${TARGET_PREFIX}libc-for-gcc"

SRC_URI = "git://uclibc.org/uClibc.git;branch=${PV};protocol=git \
	file://uClibc.machine \
	file://uClibc.distro \
	file://uclibc-arm-ftruncate64.patch \
	file://uclibc_enable_log2_test.patch \
	file://ldso_use_arm_dl_linux_resolve_in_thumb_mode.patch \
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
	file://compile-arm-fork-with-O2.patch \
	file://epoll-asm-fix.patch \
	file://orign_path.patch \
	file://rtld_no.patch \
	file://0001-Config.in.arch-Free-UCLIBC_HAS_FPU-setting-from-depe.patch \
	file://0001-mips-signalfd.h-SFD_NONBLOCK-for-mips-is-0200-unlike.patch \
	file://uclibc-execvpe.patch \
	file://uclibc_scheduler_update.patch \
	file://sync_file_range2.patch \
	file://mount.h-update.patch \
	"
S = "${WORKDIR}/git"
