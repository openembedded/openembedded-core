S = "${STAGING_KERNEL_DIR}"
do_unpack[depends] += "virtual/kernel:do_patch"
do_package[depends] += "virtual/kernel:do_populate_sysroot"
KERNEL_VERSION = "${@get_kernelversion_file("${S}")}"

inherit linux-kernel-base

