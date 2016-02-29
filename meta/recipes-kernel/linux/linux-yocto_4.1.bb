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

SRCREV_machine_qemuarm ?= "2af5b3dff27887af8ffbc700a6a09a9d75c2b05f"
SRCREV_machine_qemuarm64 ?= "dd6492b44151164242718855d6c9eebbf0018eac"
SRCREV_machine_qemumips ?= "deab449b07a79b9a108f1c94e3dabef34135517a"
SRCREV_machine_qemuppc ?= "dd6492b44151164242718855d6c9eebbf0018eac"
SRCREV_machine_qemux86 ?= "dd6492b44151164242718855d6c9eebbf0018eac"
SRCREV_machine_qemux86-64 ?= "dd6492b44151164242718855d6c9eebbf0018eac"
SRCREV_machine_qemumips64 ?= "4984422b4cd3deb6d68f46d010fb8eb007468677"
SRCREV_machine ?= "dd6492b44151164242718855d6c9eebbf0018eac"
SRCREV_meta ?= "56dcb623ebf5e83d65fdb4eb270f23676bb000a5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
