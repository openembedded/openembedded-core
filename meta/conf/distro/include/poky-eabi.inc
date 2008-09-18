#
# Poky's default configuration (ARM EABI)
# gcc 4.2.3, binutils 2.18, glibc 2.5
# use gcc 4.3.1 for armv7 targets
#

TARGET_VENDOR = "-poky"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "gcc-cross-intermediate"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross"

GCCVERSION ?= "4.2.3"
GCCVERSION_qemuarmv7 = "4.3.1"
GCCVERSION_beagleboard = "4.3.1"
GCCVERSION_omap-3430sdp = "4.3.1"
GCCVERSION_omap-3430ldp = "4.3.1"
# 4.2.3+csl-arm-2008q1-126 doesn't work

BINUVERSION = "2.18"

PREFERRED_VERSION_gcc ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-initial ?= "${GCCVERSION}"
PREFERRED_VERSION_gcc-cross-intermediate ?= "${GCCVERSION}"
PREFERRED_VERSION_binutils ?= "${BINUVERSION}"
PREFERRED_VERSION_binutils-cross ?= "${BINUVERSION}"
PREFERRED_VERSION_linux-libc-headers ?= "2.6.24"
PREFERRED_VERSION_glibc ?= "2.6.1"
PREFERRED_VERSION_glibc-initial ?= "2.6.1"
PREFERRED_VERSION_elfutils ?= "0.131"

ASNEEDED = "-Wl,--as-needed"
ASNEEDED_pn-console-tools = ""
ASNEEDED_pn-distcc = ""
ASNEEDED_pn-openobex = ""
ASNEEDED_pn-icu = ""
ASNEEDED_pn-xserver-kdrive-xomap = ""
ASNEEDED_pn-minimo = ""
ASNEEDED_pn-pciutils = ""
ASNEEDED_pn-puzzles = ""
ASNEEDED_pn-dialer = ""

TARGET_LDFLAGS += "${ASNEEDED}"
