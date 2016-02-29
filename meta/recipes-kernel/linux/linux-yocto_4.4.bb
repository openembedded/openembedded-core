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

SRCREV_machine_qemuarm ?= "fa6a2f888d8adfe03b24ef32654be470960aed41"
SRCREV_machine_qemuarm64 ?= "ff4c4ef15b51f45b9106d71bf1f62fe7c02e63c2"
SRCREV_machine_qemumips ?= "a23b6eb1c5bca3bde2a9f94d9059274fff7da281"
SRCREV_machine_qemuppc ?= "ff4c4ef15b51f45b9106d71bf1f62fe7c02e63c2"
SRCREV_machine_qemux86 ?= "ff4c4ef15b51f45b9106d71bf1f62fe7c02e63c2"
SRCREV_machine_qemux86-64 ?= "ff4c4ef15b51f45b9106d71bf1f62fe7c02e63c2"
SRCREV_machine_qemumips64 ?= "8bbcb369cf605d1ada384f4b950da2abc5d1f4cc"
SRCREV_machine ?= "ff4c4ef15b51f45b9106d71bf1f62fe7c02e63c2"
SRCREV_meta ?= "8b6a7d80344837fd64163008521a31a6f891313e"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
