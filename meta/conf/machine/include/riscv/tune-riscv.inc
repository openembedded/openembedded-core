require conf/machine/include/riscv/arch-riscv.inc

DEFAULTTUNE ?= "riscv64gc"

AVAILTUNES += "riscv64gcv riscv64gc riscv32gc riscv64 riscv32 riscv64nc riscv64nf riscv32nf rva20u64"

# Default
TUNE_FEATURES:tune-riscv64gc := "${@oe.tune.riscv_isa_to_tune("rv64gc")}"
RISCVPKGARCH:tune-riscv64gc ?= "riscv64imafdc_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv64gc = "${RISCVPKGARCH:tune-riscv64gc}"

TUNE_FEATURES:tune-riscv64gcv := "${@oe.tune.riscv_isa_to_tune("rv64gcv")}"
RISCVPKGARCH:tune-riscv64gcv ?= "riscv64imafdcv_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv64gcv = "${RISCVPKGARCH:tune-riscv64gcv}"

TUNE_FEATURES:tune-riscv32gc := "${@oe.tune.riscv_isa_to_tune("rv32gc")}"
RISCVPKGARCH:tune-riscv32gc ?= "riscv32imafdc_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv32gc = "${RISCVPKGARCH:tune-riscv32gc}"

# Compatibility aliases so that layers using old tunes don't break
TUNE_FEATURES:tune-riscv64 := "${TUNE_FEATURES:tune-riscv64gc}"
RISCVPKGARCH:tune-riscv64 ?= "${RISCVPKGARCH:tune-riscv64gc}"
PACKAGE_EXTRA_ARCHS:tune-riscv64 = "${PACKAGE_EXTRA_ARCHS:tune-riscv64gc}"
TUNE_FEATURES:tune-riscv32 := "${TUNE_FEATURES:tune-riscv32gc}"
RISCVPKGARCH:tune-riscv32 ?= "${RISCVPKGARCH:tune-riscv32gc}"
PACKAGE_EXTRA_ARCHS:tune-riscv32 = "${PACKAGE_EXTRA_ARCHS:tune-riscv32gc}"

# No float
TUNE_FEATURES:tune-riscv64nf := "${@oe.tune.riscv_isa_to_tune("rv64imac_zicsr_zifencei")}"
RISCVPKGARCH:tune-riscv64nf ?= "riscv64imac_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv64nf = "${RISCVPKGARCH:tune-riscv64nf}"

TUNE_FEATURES:tune-riscv32nf := "${@oe.tune.riscv_isa_to_tune("rv32imac_zicsr_zifencei")}"
RISCVPKGARCH:tune-riscv32nf ?= "riscv32imac_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv32nf = "${RISCVPKGARCH:tune-riscv32nf}"

# no compressed
TUNE_FEATURES:tune-riscv64nc := "${@oe.tune.riscv_isa_to_tune("rv64imafd_zicsr_zifencei")}"
RISCVPKGARCH:tune-riscv64nc ?= "riscv64imafd_zicsr_zifencei"
PACKAGE_EXTRA_ARCHS:tune-riscv64nc = "${RISCVPKGARCH:tune-riscv64nc}"

# RISC-V profiles. zifencei isn't mandatory in them, but Linux requires it, so
# it's added on top of each profile's mandatory extensions.
TUNE_FEATURES:tune-rva20u64 := "${@oe.tune.riscv_isa_to_tune("rva20u64")} zifencei"
RISCVPKGARCH:tune-rva20u64 ?= "rva20u64_zifencei"
PACKAGE_EXTRA_ARCHS:tune-rva20u64 = "${RISCVPKGARCH:tune-rva20u64}"
