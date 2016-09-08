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

SRCREV_machine_qemuarm ?= "2505d9c2441eb638d55b330144988d1730065074"
SRCREV_machine_qemuarm64 ?= "a67e1ae4bf3570e93f8d8fe33b1830de058c1b72"
SRCREV_machine_qemumips ?= "544caf9c03c40c2de6b93db9a767e568e0baaa8f"
SRCREV_machine_qemuppc ?= "a67e1ae4bf3570e93f8d8fe33b1830de058c1b72"
SRCREV_machine_qemux86 ?= "a67e1ae4bf3570e93f8d8fe33b1830de058c1b72"
SRCREV_machine_qemux86-64 ?= "a67e1ae4bf3570e93f8d8fe33b1830de058c1b72"
SRCREV_machine_qemumips64 ?= "c1b45251a5fabfb0f2aba3a0168d8a952a2cd827"
SRCREV_machine ?= "a67e1ae4bf3570e93f8d8fe33b1830de058c1b72"
SRCREV_meta ?= "59290c5f6192da2eccf478d37a8f9f88134822b3"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.18"

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
