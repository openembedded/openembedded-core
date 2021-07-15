KBRANCH ?= "v5.10/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.10/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.10/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.10/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.10/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.10/standard/base"
KBRANCH_qemuriscv32  ?= "v5.10/standard/base"
KBRANCH_qemux86  ?= "v5.10/standard/base"
KBRANCH_qemux86-64 ?= "v5.10/standard/base"
KBRANCH_qemumips64 ?= "v5.10/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ac3dc3fddbea1db1d6cde9681b408f4d04541951"
SRCREV_machine_qemuarm64 ?= "de7626dcc41204d22682fa5c424c4bddd587ee97"
SRCREV_machine_qemumips ?= "ab2cde78ab5c1def7ab03d21c8986c794c6f69ce"
SRCREV_machine_qemuppc ?= "d9a946c583b56ccdcf4ccd228d64129138e698e6"
SRCREV_machine_qemuriscv64 ?= "3d78eb65b99aa6a6aaaf4de890b262f4dcb88623"
SRCREV_machine_qemuriscv32 ?= "3d78eb65b99aa6a6aaaf4de890b262f4dcb88623"
SRCREV_machine_qemux86 ?= "3d78eb65b99aa6a6aaaf4de890b262f4dcb88623"
SRCREV_machine_qemux86-64 ?= "3d78eb65b99aa6a6aaaf4de890b262f4dcb88623"
SRCREV_machine_qemumips64 ?= "793a200e51c176cad7416603b995c2ec30d6ddde"
SRCREV_machine ?= "3d78eb65b99aa6a6aaaf4de890b262f4dcb88623"
SRCREV_meta ?= "44498529cd41b235e527c48e48272d9af64fdf7d"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.50"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
