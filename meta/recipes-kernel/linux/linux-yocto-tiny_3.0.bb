inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

#KMACHINE = "yocto/standard/tiny/base"
KMACHINE = "yocto/standard/base"
KBRANCH = "${KMACHINE}"

LINUX_VERSION ?= "3.0.23"

SRCREV_machine ?= "8fd24b3570ab995848e4123ef13bac64e2c924be"
SRCREV_meta ?= "e559129b4a6f39f68b75141096b2d516cf7a7f35"

PR = "r3"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta \
           file://core.cfg \
           file://serial.cfg \
           file://ext2.cfg \
           file://rtc-pc.cfg \
           file://ramfs.cfg \
           file://devtmpfs.cfg \
           file://net.cfg \
           file://debug.cfg \
           file://lzma.cfg \
           "


# Enable qemux86 specific emulated options
SRC_URI_append_qemux86 = " file://defconfig file://ata.cfg file://e1000.cfg"

#COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"
COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
