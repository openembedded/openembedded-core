require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KBRANCH_DEFAULT = "standard/tiny/base"
KBRANCH = "${KBRANCH_DEFAULT}"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.4.20"

KMETA = "meta"

SRCREV_machine ?= "b13bef6377719a488293af196236cc290566fad3"
SRCREV_meta ?= "6737e890fff2a423fbb022ab1f7f82ef187fd8b1"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
