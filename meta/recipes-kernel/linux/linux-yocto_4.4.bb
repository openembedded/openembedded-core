KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "75b02cc20216b6a068e1757a997f4e0d405d6ded"
SRCREV_machine_qemuarm64 ?= "1238684aa5173dc6f431ad1b3c85a37830244824"
SRCREV_machine_qemumips ?= "e656b69259deb08cfe6d606fb126b4c0810abf64"
SRCREV_machine_qemuppc ?= "1238684aa5173dc6f431ad1b3c85a37830244824"
SRCREV_machine_qemux86 ?= "1238684aa5173dc6f431ad1b3c85a37830244824"
SRCREV_machine_qemux86-64 ?= "1238684aa5173dc6f431ad1b3c85a37830244824"
SRCREV_machine_qemumips64 ?= "8c40a1bc5e4ece3f72d4ade764087bd0fcc01c38"
SRCREV_machine ?= "1238684aa5173dc6f431ad1b3c85a37830244824"
SRCREV_meta ?= "8c6158ec5e43a4cae59058af49f70e0406d18091"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.15"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
