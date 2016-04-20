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

SRCREV_machine_qemuarm ?= "373384988d421dfc209175c1ffc691e12ec55e23"
SRCREV_machine_qemuarm64 ?= "5a562b765837414416d609058c4e8dd7a8072dab"
SRCREV_machine_qemumips ?= "87b2d0c1f001650016ec13290c3e2de7b34cf468"
SRCREV_machine_qemuppc ?= "5a562b765837414416d609058c4e8dd7a8072dab"
SRCREV_machine_qemux86 ?= "5a562b765837414416d609058c4e8dd7a8072dab"
SRCREV_machine_qemux86-64 ?= "5a562b765837414416d609058c4e8dd7a8072dab"
SRCREV_machine_qemumips64 ?= "ed3c898c537dee36adc713bd30ec3ef6035ed95a"
SRCREV_machine ?= "5a562b765837414416d609058c4e8dd7a8072dab"
SRCREV_meta ?= "e1515ef98cec975df218363e28c2abe3a71c6a59"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.3"

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
