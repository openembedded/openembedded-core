# RISCV Architecture definition

DEFAULTTUNE ?= "riscv64"

TUNE_ARCH = "${TUNE_ARCH_tune-${DEFAULTTUNE}}"
TUNE_PKGARCH = "${TUNE_PKGARCH_tune-${DEFAULTTUNE}}"
TUNE_CCARGS .= ""

# QEMU usermode fails with invalid instruction error (For riscv32)
MACHINE_FEATURES_BACKFILL_CONSIDERED_append = "${@bb.utils.contains('TUNE_FEATURES', 'riscv32', ' qemu-usermode', '', d)}"

# Fix: ld: unrecognized option '--hash-style=sysv'
LINKER_HASH_STYLE_libc-newlib = ""
# Fix: ld: unrecognized option '--hash-style=gnu'
LINKER_HASH_STYLE_libc-baremetal = ""
