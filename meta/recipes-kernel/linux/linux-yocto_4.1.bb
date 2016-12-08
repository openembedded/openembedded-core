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

SRCREV_machine_qemuarm ?= "6f0ca13105ff444e5adcea39d68c5f7707b1d23b"
SRCREV_machine_qemuarm64 ?= "f75d58721859cd2ce5382bcdcae35479a6ce9f21"
SRCREV_machine_qemumips ?= "d5bd5d3069bfccfb76c420f27e1e6fd94aaa3dcc"
SRCREV_machine_qemuppc ?= "1006b2e79cf808a2e8a74a1b0325dec9b4d0873e"
SRCREV_machine_qemux86 ?= "f75d58721859cd2ce5382bcdcae35479a6ce9f21"
SRCREV_machine_qemux86-64 ?= "f75d58721859cd2ce5382bcdcae35479a6ce9f21"
SRCREV_machine_qemumips64 ?= "985d7f27f0ad497a0072320fbbf05626e27c741a"
SRCREV_machine ?= "f75d58721859cd2ce5382bcdcae35479a6ce9f21"
SRCREV_meta ?= "44719fa8f73fd7c444044ad3c04f5fc66f57b993"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.36"

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
