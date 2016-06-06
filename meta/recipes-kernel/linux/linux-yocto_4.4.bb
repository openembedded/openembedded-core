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

SRCREV_machine_qemuarm ?= "ae04be16985c86803d8016b06aa57c02fe63cab3"
SRCREV_machine_qemuarm64 ?= "1f3e98df094cb7afb7d3d540dd0e47b3b8c89711"
SRCREV_machine_qemumips ?= "1d16db0bce02f4f46a925e5425194de8d25a926e"
SRCREV_machine_qemuppc ?= "1f3e98df094cb7afb7d3d540dd0e47b3b8c89711"
SRCREV_machine_qemux86 ?= "1f3e98df094cb7afb7d3d540dd0e47b3b8c89711"
SRCREV_machine_qemux86-64 ?= "1f3e98df094cb7afb7d3d540dd0e47b3b8c89711"
SRCREV_machine_qemumips64 ?= "6fe42d5321cd24411c2a4b7f2aac8a01dfa82b8f"
SRCREV_machine ?= "1f3e98df094cb7afb7d3d540dd0e47b3b8c89711"
SRCREV_meta ?= "8900370d334ab4f7224fa71d7d46d62f0b11199d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.12"

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
