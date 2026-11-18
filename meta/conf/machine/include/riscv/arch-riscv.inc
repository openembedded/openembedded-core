# RISCV Architecture definition

# Based on the RISC-V Instruction Set Manual Volume I: Unprivileged ISA from May 2025
# As well as the RISC-V options for using GCC (as of June 2025)

# Note: the following should be implemented in the order that GCC expects
# -march= values to be defined in.

# Base ISA
# All supported march strings must start with rv32 or rv64
TUNEVALID[rv] = "RISC-V"
TUNE_RISCV_ARCH = "${@bb.utils.contains("TUNE_FEATURES", "rv", "riscv", "", d)}"
TUNE_RISCV_MARCH = "${@bb.utils.contains("TUNE_FEATURES", "rv", "rv", "", d)}"
TUNE_RISCV_ABI = ""

# There are two primary ABIs, ilp32 and lp64
# There are variants of both, that appears to be based on extensions above
# For example:
#   rv32i uses ilp32, rv32e uses ilp32e, rv32f uses ilp32f
#   rv64i uses lp64, rv64if uses lp64f, rv64id uses lp64d
TUNEVALID[32] = "ISA XLEN - 32-bit"
TUNECONFLICTS[32] = "64"
TUNE_RISCV_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "32", "32", "", d)}"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "32", "32", "", d)}"
TUNE_RISCV_ABI .= "${@bb.utils.contains("TUNE_FEATURES", "32", "ilp32", "", d)}"

TUNEVALID[64] = "ISA XLEN - 64-bit"
TUNECONFLICTS[64] = "32"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "64", "64", "", d)}"
TUNE_RISCV_ARCH .= "${@bb.utils.contains("TUNE_FEATURES", "64", "64", "", d)}"
TUNE_RISCV_ABI .= "${@bb.utils.contains("TUNE_FEATURES", "64", "lp64", "", d)}"

# The package arch starts with the canonical arch, but adds some extensions to make
# package compatibility clear
TUNE_RISCV_PKGARCH = "${TUNE_RISCV_ARCH}"

# i, e, or g are defined by gcc, but 'g' refers to 'i' + extensions 'MAFD Zicsr Zifencei'
# So 'g' will not be defined here as it is an abbreviation of the expanded version
TUNEVALID[e] = "Reduced register base integer extension"
TUNECONFLICTS[e] = "i"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "e", "e", "", d)}"
TUNE_RISCV_ABI .= "${@bb.utils.contains("TUNE_FEATURES", "e", "e", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "e", "e", "", d)}"

TUNEVALID[i] = "Base integer extension"
TUNECONFLICTS[i] = "e"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "i", "i", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "i", "i", "", d)}"

# Extensions
TUNEVALID[m] = "Integer multiplication and division extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "m", "m", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "m", "m", "", d)}"

TUNEVALID[a] = "Atomic extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "a", "a", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "a", "a", "", d)}"

TUNEVALID[f] = "Single-precision floating-point extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "f d", "f", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "f d", "f", "", d)}"

TUNEVALID[d] = "Double-precision floating-point extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "d", "d", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "d", "d", "", d)}"

# Only f OR d, but just one
TUNE_RISCV_ABI .= "${@bb.utils.contains("TUNE_FEATURES", "d", "d", bb.utils.contains("TUNE_FEATURES", "f", "f", "", d), d)}"

TUNEVALID[c] = "Compressed extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "c", "c", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "c", "c", "", d)}"

TUNEVALID[b] = "Bit Manipulation extension"
# Handled below via zba, zbb, zbs
# This matches current Linux kernel behavior
#TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "b", "b", "", d)}"
#TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "b", "b", "", d)}"

TUNEVALID[v] = "Vector operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "v", "v", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "v", "v", "", d)}"

# Now the special Z extensions
# zic64b, zicbop, zicboz, zihintpause, zihpm, za64rs, zfhmin and zkt are
# mandatory in the RVA22U64 and later profiles
# zicond, zihintntl, zimop, zawrs, zfa, zcb, zcmop, zvbb, zvfhmin, zvkb, zvkt
# and supm, as well as v, are mandatory in the RVA23U64 and later profiles
TUNEVALID[zic64b] = "Cache blocks are 64 bytes extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zic64b", "_zic64b", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zic64b", "_zic64b", "", d)}"

TUNEVALID[zicbom] = "Cache-block management extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zicbom", "_zicbom", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zicbom", "_zicbom", "", d)}"

TUNEVALID[zicbop] = "Cache-block prefetch extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicbop", "_zicbop", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicbop", "_zicbop", "", d)}"

TUNEVALID[zicboz] = "Cache-block zero extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicboz", "_zicboz", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicboz", "_zicboz", "", d)}"

# The following are mandatory in the RVA20U64 and later profiles
TUNEVALID[ziccamoa] = "Main memory supports all atomics in A extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccamoa", "_ziccamoa", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccamoa", "_ziccamoa", "", d)}"

TUNEVALID[ziccif] = "Main memory supports instruction fetch with atomicity requirement extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccif", "_ziccif", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccif", "_ziccif", "", d)}"

TUNEVALID[zicclsm] = "Main memory supports misaligned loads/stores extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicclsm", "_zicclsm", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicclsm", "_zicclsm", "", d)}"

TUNEVALID[ziccrse] = "Main memory supports forward progress on LR/SC sequences extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccrse", "_ziccrse", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "ziccrse", "_ziccrse", "", d)}"

TUNEVALID[zicntr] = "Base counters and timers extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicntr", "_zicntr", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicntr", "_zicntr", "", d)}"

TUNEVALID[zicond] = "Integer conditional operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicond", "_zicond", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zicond", "_zicond", "", d)}"

TUNEVALID[zicsr] = "Control and status register access extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zicsr f d", "_zicsr", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zicsr f d", "_zicsr", "", d)}"

TUNEVALID[zifencei] = "Instruction-fetch fence extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zifencei", "_zifencei", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zifencei", "_zifencei", "", d)}"

TUNEVALID[zihintntl] = "Non-temporal locality hints extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihintntl", "_zihintntl", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihintntl", "_zihintntl", "", d)}"

TUNEVALID[zihintpause] = "Pause hint extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihintpause", "_zihintpause", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihintpause", "_zihintpause", "", d)}"

TUNEVALID[zihpm] = "Hardware performance counters extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihpm", "_zihpm", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zihpm", "_zihpm", "", d)}"

TUNEVALID[zimop] = "May-be-operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zimop", "_zimop", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zimop", "_zimop", "", d)}"

# zmmul, zaamo, zalrsc, zca and zcd are subsets of single-letter extensions
# above (m, a, c and c with d), which compilers enable along with them. They
# are only passed on when listed in TUNE_FEATURES, so that -march doesn't
# change for existing tunes, and only added to the package arch when the
# extension they're part of isn't enabled.
TUNEVALID[zmmul] = "Integer multiplication (without division) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zmmul", "_zmmul", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zmmul", bb.utils.contains("TUNE_FEATURES", "m", "", "_zmmul", d), "", d)}"

# Mandatory in the RVA20U64 profile. Later profiles require za64rs (below),
# which is stricter, instead.
TUNEVALID[za128rs] = "Reservation set size of at most 128 bytes extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "za128rs", "_za128rs", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "za128rs", "_za128rs", "", d)}"

TUNEVALID[za64rs] = "Reservation set size of at most 64 bytes extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "za64rs", "_za64rs", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "za64rs", "_za64rs", "", d)}"

TUNEVALID[zaamo] = "Atomic memory operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zaamo", "_zaamo", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zaamo", bb.utils.contains("TUNE_FEATURES", "a", "", "_zaamo", d), "", d)}"

TUNEVALID[zalrsc] = "Load-reserved/store-conditional extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zalrsc", "_zalrsc", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zalrsc", bb.utils.contains("TUNE_FEATURES", "a", "", "_zalrsc", d), "", d)}"

TUNEVALID[zawrs] = "Wait-on-reservation-set instructions extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zawrs", "_zawrs", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zawrs", "_zawrs", "", d)}"

TUNEVALID[zfa] = "Additional floating-point instructions extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zfa", "_zfa", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zfa", "_zfa", "", d)}"

TUNEVALID[zfhmin] = "Minimal half-precision floating-point extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zfhmin", "_zfhmin", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zfhmin", "_zfhmin", "", d)}"

TUNEVALID[zca] = "Compressed integer instructions extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zca", "_zca", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zca", bb.utils.contains("TUNE_FEATURES", "c", "", "_zca", d), "", d)}"

TUNEVALID[zcb] = "Additional compressed instructions extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcb", "_zcb", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcb", "_zcb", "", d)}"

TUNEVALID[zcd] = "Compressed double-precision floating-point instructions extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcd", "_zcd", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcd", bb.utils.contains("TUNE_FEATURES", "c d", "", "_zcd", d), "", d)}"

TUNEVALID[zcmop] = "Compressed may-be-operations extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcmop", "_zcmop", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zcmop", "_zcmop", "", d)}"

TUNEVALID[zba] = "Address bit manipulation extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zba", "_zba", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zba", "_zba", "", d)}"

TUNEVALID[zbb] = "Basic bit manipulation extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zbb", "_zbb", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zbb", "_zbb", "", d)}"

TUNEVALID[zbc] = "Carry-less multiplication extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zbc", "_zbc", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "zbc", "_zbc", "", d)}"

TUNEVALID[zbs] = "Single-bit manipulation extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zbs", "_zbs", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains_any("TUNE_FEATURES", "b zbs", "_zbs", "", d)}"

TUNEVALID[zkt] = "Data-independent execution latency extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zkt", "_zkt", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zkt", "_zkt", "", d)}"

TUNEVALID[zvbb] = "Vector basic bit-manipulation extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvbb", "_zvbb", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvbb", "_zvbb", "", d)}"

# The zve* and zvl* extensions are subsets of v, like zmmul is of m above, so
# they are only added to the package arch when v isn't enabled. Other extensions
# also imply them (e.g. zvbb implies zvkb, zve32x, and zvl32b), which the
# compilers handle but the package arch doesn't. Tunes using them without v
# should set RISCVPKGARCH:tune-<name>.
TUNEVALID[zve32f] = "Embedded vector (32-bit integer and floating-point) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve32f", "_zve32f", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve32f", bb.utils.contains("TUNE_FEATURES", "v", "", "_zve32f", d), "", d)}"

TUNEVALID[zve32x] = "Embedded vector (32-bit integer) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve32x", "_zve32x", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve32x", bb.utils.contains("TUNE_FEATURES", "v", "", "_zve32x", d), "", d)}"

TUNEVALID[zve64d] = "Embedded vector (64-bit integer, 64-bit floating-point) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64d", "_zve64d", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64d", bb.utils.contains("TUNE_FEATURES", "v", "", "_zve64d", d), "", d)}"

TUNEVALID[zve64f] = "Embedded vector (64-bit integer, 32-bit floating-point) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64f", "_zve64f", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64f", bb.utils.contains("TUNE_FEATURES", "v", "", "_zve64f", d), "", d)}"

TUNEVALID[zve64x] = "Embedded vector (64-bit integer) extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64x", "_zve64x", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zve64x", bb.utils.contains("TUNE_FEATURES", "v", "", "_zve64x", d), "", d)}"

TUNEVALID[zvfhmin] = "Vector minimal half-precision floating-point extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvfhmin", "_zvfhmin", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvfhmin", "_zvfhmin", "", d)}"

# zvkb is a subset of zvbb
TUNEVALID[zvkb] = "Vector cryptography bit-manipulation extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvkb", "_zvkb", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvkb", bb.utils.contains("TUNE_FEATURES", "zvbb", "", "_zvkb", d), "", d)}"

TUNEVALID[zvkt] = "Vector data-independent execution latency extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvkt", "_zvkt", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvkt", "_zvkt", "", d)}"

TUNEVALID[zvl128b] = "Minimum vector length of 128 bits extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl128b", "_zvl128b", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl128b", bb.utils.contains("TUNE_FEATURES", "v", "", "_zvl128b", d), "", d)}"

TUNEVALID[zvl32b] = "Minimum vector length of 32 bits extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl32b", "_zvl32b", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl32b", bb.utils.contains("TUNE_FEATURES", "v", "", "_zvl32b", d), "", d)}"

TUNEVALID[zvl64b] = "Minimum vector length of 64 bits extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl64b", "_zvl64b", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "zvl64b", bb.utils.contains("TUNE_FEATURES", "v", "", "_zvl64b", d), "", d)}"

# Supervisor-level extensions, which come after all of the Z extensions
TUNEVALID[supm] = "User-mode pointer masking extension"
TUNE_RISCV_MARCH .= "${@bb.utils.contains("TUNE_FEATURES", "supm", "_supm", "", d)}"
TUNE_RISCV_PKGARCH .= "${@bb.utils.contains("TUNE_FEATURES", "supm", "_supm", "", d)}"

# Construct TUNE_CCARGS
# This should result in a CCARG similar to:
#   -march=rv32imac -mabi=ilp32
TUNE_CCARGS = "${@d.getVar('TUNE_CCARGS:tune-${DEFAULTTUNE}') or '-march=${TUNE_RISCV_MARCH} -mabi=${TUNE_RISCV_ABI}'}"

# Construct TUNE_ARCH
# This should result in an arch string similar to:
#   riscv32
TUNE_ARCH = "${TUNE_RISCV_ARCH}"

# Construct TUNE_PKGARCH
# As with ARMPKGARCH on arm, each tune sets its package arch as a literal in
# RISCVPKGARCH:tune-<name>, so that it only changes when deliberately changed.
# Tunes that don't set it fall back to TUNE_RISCV_PKGARCH, which is built from
# TUNE_FEATURES above, e.g.:
#    riscv32imac
RISCVPKGARCH = "${@d.getVar('RISCVPKGARCH:tune-${DEFAULTTUNE}') or d.getVar('TUNE_RISCV_PKGARCH')}"
TUNE_PKGARCH = "${RISCVPKGARCH}"

# Misc settings
# Fix: ld: unrecognized option '--hash-style=sysv'
LINKER_HASH_STYLE:libc-newlib = ""
LINKER_HASH_STYLE:libc-picolibc = ""
# Fix: ld: unrecognized option '--hash-style=gnu'
LINKER_HASH_STYLE:libc-baremetal = ""
