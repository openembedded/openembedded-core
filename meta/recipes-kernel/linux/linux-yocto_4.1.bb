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

SRCREV_machine_qemuarm ?= "a2fde8c9a8b709574b36931f728fb78617892d98"
SRCREV_machine_qemuarm64 ?= "a6b3a8cc120640bf5e528c8558ce060675757fc1"
SRCREV_machine_qemumips ?= "33efda07565f45c576671358e75b39dc6a069cbf"
SRCREV_machine_qemuppc ?= "96b9a39490bc348b36d0de13397a086e66a4b1cb"
SRCREV_machine_qemux86 ?= "a6b3a8cc120640bf5e528c8558ce060675757fc1"
SRCREV_machine_qemux86-64 ?= "a6b3a8cc120640bf5e528c8558ce060675757fc1"
SRCREV_machine_qemumips64 ?= "a46e08c52a6764b0e54f4dbbdfaa3050a379ac4a"
SRCREV_machine ?= "a6b3a8cc120640bf5e528c8558ce060675757fc1"
SRCREV_meta ?= "19cafe114e7d35e6202afa1079b32ce600646660"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.32"

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
