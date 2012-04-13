inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

KMACHINE = "common-pc"
KBRANCH = "standard/tiny"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.2.11"

SRCREV_machine ?= "ec236058dc254183dbfb3744bf21f110c37af30b"
SRCREV_meta ?= "135c75bf9615334b5b8bb9108d612fe7dfbdb901"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
