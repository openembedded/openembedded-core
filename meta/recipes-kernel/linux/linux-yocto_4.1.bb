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

SRCREV_machine_qemuarm ?= "fc38bc54e15ab2943507b63e740c576f5537fbc0"
SRCREV_machine_qemuarm64 ?= "bdf4565e5dcc1ad65e26cebdff7bf427289f1d61"
SRCREV_machine_qemumips ?= "d7d76bf246979d635990a05961886292ddcb5600"
SRCREV_machine_qemuppc ?= "bdf4565e5dcc1ad65e26cebdff7bf427289f1d61"
SRCREV_machine_qemux86 ?= "bdf4565e5dcc1ad65e26cebdff7bf427289f1d61"
SRCREV_machine_qemux86-64 ?= "bdf4565e5dcc1ad65e26cebdff7bf427289f1d61"
SRCREV_machine_qemumips64 ?= "84dd3ffed15ebb59e6d396343ced2450f0f10379"
SRCREV_machine ?= "bdf4565e5dcc1ad65e26cebdff7bf427289f1d61"
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
