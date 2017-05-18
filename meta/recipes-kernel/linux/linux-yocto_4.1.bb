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

SRCREV_machine_qemuarm ?= "3b0fd24797cb11aae54010b1bbc09dd2ae8a108e"
SRCREV_machine_qemuarm64 ?= "398d1785c6bde0085b99f00f346e199a15fbdcef"
SRCREV_machine_qemumips ?= "7420516ff545f8ab3e2471cf08bf851744264a6a"
SRCREV_machine_qemuppc ?= "6952f5a70bc8a84a4c4d926cdec93e7a39878f32"
SRCREV_machine_qemux86 ?= "398d1785c6bde0085b99f00f346e199a15fbdcef"
SRCREV_machine_qemux86-64 ?= "398d1785c6bde0085b99f00f346e199a15fbdcef"
SRCREV_machine_qemumips64 ?= "59727538774860381f42bcfb6ec05fe21facd319"
SRCREV_machine ?= "398d1785c6bde0085b99f00f346e199a15fbdcef"
SRCREV_meta ?= "82aa97b3ebb3cd7eec8296a7cb090dfc67c2d382"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.39"

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
