inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KMACHINE = "common-pc"
KBRANCH = "standard/tiny"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.2.11"

SRCREV_machine ?= "61960ba8e910d54b5525d5e9b6a7469bc399c246"
SRCREV_meta ?= "6b3d4e09aa2531e9649f3f03827b7efbccfcec03"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
