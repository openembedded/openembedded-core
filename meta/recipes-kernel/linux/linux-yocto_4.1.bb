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

SRCREV_machine_qemuarm ?= "2e57b0b4eba5545f8dcd6d204930acc9b16668cc"
SRCREV_machine_qemuarm64 ?= "2e0ac7b6c4e3ada23a84756287e9b7051ace939a"
SRCREV_machine_qemumips ?= "1da18606598d131831b2cb5db377c2379c26cab2"
SRCREV_machine_qemuppc ?= "2e0ac7b6c4e3ada23a84756287e9b7051ace939a"
SRCREV_machine_qemux86 ?= "2e0ac7b6c4e3ada23a84756287e9b7051ace939a"
SRCREV_machine_qemux86-64 ?= "2e0ac7b6c4e3ada23a84756287e9b7051ace939a"
SRCREV_machine_qemumips64 ?= "a73051ce903a0a646e1c198b71b2ec8eb1091ed0"
SRCREV_machine ?= "2e0ac7b6c4e3ada23a84756287e9b7051ace939a"
SRCREV_meta ?= "79dbb64d9e179718369a7a5c7b364fda9936571f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.17"

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
