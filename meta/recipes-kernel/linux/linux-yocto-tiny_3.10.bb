KBRANCH ?= "standard/tiny/base"

require recipes-kernel/linux/linux-yocto.inc

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.43"

KMETA = "meta"

SRCREV_machine ?= "e4f08d724d6663e6d23d19668c97f9e6792c94d2"
SRCREV_meta ?= "fab23a4d44de25ee77e497cbf8605eadc3aaf3d8"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
