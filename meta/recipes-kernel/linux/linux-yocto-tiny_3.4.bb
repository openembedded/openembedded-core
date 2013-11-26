require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KBRANCH_DEFAULT = "standard/tiny/base"
KBRANCH = "${KBRANCH_DEFAULT}"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.4.69"

KMETA = "meta"

SRCREV_machine ?= "7f4d818b0450a5dc79f81b51dc7d13d0682b1287"
SRCREV_meta ?= "f36797c2df3fbe9491c8ac5977fb691f4a75e9b7"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
