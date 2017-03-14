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

SRCREV_machine_qemuarm ?= "96c11ef2a20975febadc839abaae3127731de642"
SRCREV_machine_qemuarm64 ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_machine_qemumips ?= "e22a4ddcb6472ae6f258c16af1b38e8e5866c5eb"
SRCREV_machine_qemuppc ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_machine_qemux86 ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_machine_qemux86-64 ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_machine_qemumips64 ?= "ca07238310e9ef502adbb1416e9f327657886343"
SRCREV_machine ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_meta ?= "864bc5736aa58c510713e7b76d205d5341377972"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.53"

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
