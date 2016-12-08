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

SRCREV_machine_qemuarm ?= "0166111980b931967bf3449d3d040aac6ff523a8"
SRCREV_machine_qemuarm64 ?= "9cc72ea6194abae764a2db8f6a32cde60e739d7c"
SRCREV_machine_qemumips ?= "90bf4bfeee3d0ca1cf0077dc1e0045cd9237e392"
SRCREV_machine_qemuppc ?= "9cc72ea6194abae764a2db8f6a32cde60e739d7c"
SRCREV_machine_qemux86 ?= "9cc72ea6194abae764a2db8f6a32cde60e739d7c"
SRCREV_machine_qemux86-64 ?= "9cc72ea6194abae764a2db8f6a32cde60e739d7c"
SRCREV_machine_qemumips64 ?= "fd20390d53c24f1ef0293b2c26b8e883e87e91f3"
SRCREV_machine ?= "9cc72ea6194abae764a2db8f6a32cde60e739d7c"
SRCREV_meta ?= "b846fc6436aa5d4c747d620e83dfda969854d10c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.36"

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
