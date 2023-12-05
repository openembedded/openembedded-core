KBRANCH ?= "v5.4/standard/tiny/base"
KBRANCH_qemuarm  ?= "v5.4/standard/tiny/arm-versatile-926ejs"

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.4.262"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine_qemuarm ?= "73dbd7ea55670218b017d9719868e7d61d0b1011"
SRCREV_machine ?= "0b41bdd8d544c72af97c34e9c464b7534fd18c7a"
SRCREV_meta ?= "9f1a56eeea58f7d9b597f0ea33e43e35b8288762"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "qemux86|qemux86-64|qemuarm|qemuarmv5"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"
