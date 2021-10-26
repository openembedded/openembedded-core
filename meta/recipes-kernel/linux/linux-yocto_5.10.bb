KBRANCH ?= "v5.10/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.10/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.10/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.10/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.10/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.10/standard/base"
KBRANCH:qemuriscv32  ?= "v5.10/standard/base"
KBRANCH:qemux86  ?= "v5.10/standard/base"
KBRANCH:qemux86-64 ?= "v5.10/standard/base"
KBRANCH:qemumips64 ?= "v5.10/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "65c6899db7f84ee81bf9fe7b73b5f5d181636351"
SRCREV_machine:qemuarm64 ?= "6d43c23b8d3aa1161644880d85b83491f8f39729"
SRCREV_machine:qemumips ?= "4b9a9768850a12d2e806b87e0ceeab5083495035"
SRCREV_machine:qemuppc ?= "4db24c929502797874a3cc30a7d04022526b6e8a"
SRCREV_machine:qemuriscv64 ?= "9e84a42af61ff9c6feb89ab8d61ee5f25fb35c72"
SRCREV_machine:qemuriscv32 ?= "9e84a42af61ff9c6feb89ab8d61ee5f25fb35c72"
SRCREV_machine:qemux86 ?= "9e84a42af61ff9c6feb89ab8d61ee5f25fb35c72"
SRCREV_machine:qemux86-64 ?= "9e84a42af61ff9c6feb89ab8d61ee5f25fb35c72"
SRCREV_machine:qemumips64 ?= "66e7684e39bbe9b36b8673792a07c51a394c5766"
SRCREV_machine ?= "9e84a42af61ff9c6feb89ab8d61ee5f25fb35c72"
SRCREV_meta ?= "7676dde0569ceba548579dafcf8235b4eececbc8"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.74"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
