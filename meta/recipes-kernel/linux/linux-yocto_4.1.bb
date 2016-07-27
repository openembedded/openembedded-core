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

SRCREV_machine_qemuarm ?= "460340f28dd4ed608fd66692bcbac0bb24fe4aaf"
SRCREV_machine_qemuarm64 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemumips ?= "9b9337aa156bd3bcf3e813455e4bbdc2895c4da1"
SRCREV_machine_qemuppc ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemux86 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemux86-64 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemumips64 ?= "cb03bb92ed4d45c55e7a8ecfcca714f9897da96b"
SRCREV_machine ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_meta ?= "afbc6bd00e6fa854ae10eb67ab8c3be5112f6f41"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.28"

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
