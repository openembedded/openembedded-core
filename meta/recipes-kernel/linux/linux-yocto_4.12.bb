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

SRCREV_machine_qemuarm ?= "a5227887cfadd94068717e8166879bf88d00ef73"
SRCREV_machine_qemuarm64 ?= "084d8ae2f349a7621e1e51c81b4494b123f3c09a"
SRCREV_machine_qemumips ?= "a6983bac4e93ba7e892f666ad8c58b32c2d10945"
SRCREV_machine_qemuppc ?= "084d8ae2f349a7621e1e51c81b4494b123f3c09a"
SRCREV_machine_qemux86 ?= "084d8ae2f349a7621e1e51c81b4494b123f3c09a"
SRCREV_machine_qemux86-64 ?= "084d8ae2f349a7621e1e51c81b4494b123f3c09a"
SRCREV_machine_qemumips64 ?= "b344c1888b03e40683ba109bb45b44e9cf4d1eb4"
SRCREV_machine ?= "084d8ae2f349a7621e1e51c81b4494b123f3c09a"
SRCREV_meta ?= "09bc15990cc56fefe4111b8c64530cf0c4755817"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.7"

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
