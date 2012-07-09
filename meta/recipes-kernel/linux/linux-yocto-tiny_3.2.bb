require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KBRANCH = "standard/tiny"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.2.18"

KMETA = "meta"

SRCREV_machine ?= "6f962975e52f7ebbb5c4897aa77d76f5957a000e"
SRCREV_meta ?= "07ee09b520579b9f29bd15fefb01fd28b34c6064"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
