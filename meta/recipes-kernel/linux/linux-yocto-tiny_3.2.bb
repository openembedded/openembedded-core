require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KBRANCH = "standard/tiny"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.2.18"

KMETA = "meta"

SRCREV_machine ?= "2f986ab91c08c16f8d201518b11be0e367eb0e14"
SRCREV_meta ?= "07ee09b520579b9f29bd15fefb01fd28b34c6064"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
