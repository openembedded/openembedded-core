require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/tiny/base"
KBRANCH = "${KBRANCH_DEFAULT}"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.17"

KMETA = "meta"

SRCREV_machine ?= "c03195ed6e3066494e3fb4be69154a57066e845b"
SRCREV_meta ?= "c16a2cf7b06c92a0217d61569bd0293233fe6ba7"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
