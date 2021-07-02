KBRANCH ?= "v5.13/standard/tiny/base"
KBRANCH_qemuarm  ?= "v5.13/standard/tiny/arm-versatile-926ejs"

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.13"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine_qemuarm ?= "84e5292c5adf2e715e842a9eb3985034941e0798"
SRCREV_machine ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_meta ?= "ceb5fa598d08902fe2934c041875aa92d9a6fa19"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "qemux86|qemux86-64|qemuarm|qemuarmv5"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"
