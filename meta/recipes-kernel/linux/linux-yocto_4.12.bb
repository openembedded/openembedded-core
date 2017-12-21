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

SRCREV_machine_qemuarm ?= "6227bf7bc958840e020930ac250634129919571b"
SRCREV_machine_qemuarm64 ?= "4226b065fca4f630901d99b99d18c395ae3866fb"
SRCREV_machine_qemumips ?= "16b69ee760d800e7f16a01476a24a87b66702dc6"
SRCREV_machine_qemuppc ?= "4226b065fca4f630901d99b99d18c395ae3866fb"
SRCREV_machine_qemux86 ?= "4226b065fca4f630901d99b99d18c395ae3866fb"
SRCREV_machine_qemux86-64 ?= "4226b065fca4f630901d99b99d18c395ae3866fb"
SRCREV_machine_qemumips64 ?= "4eb2f843b67a8e97619689991b566808212a3677"
SRCREV_machine ?= "4226b065fca4f630901d99b99d18c395ae3866fb"
SRCREV_meta ?= "3574bb061c1bfbdcf4df8308870c03f88ef0788f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.16"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
