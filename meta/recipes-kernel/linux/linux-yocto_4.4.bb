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

SRCREV_machine_qemuarm ?= "d3ce63d0c83f579605b308f83c62a49e5dbf73e6"
SRCREV_machine_qemuarm64 ?= "dadb4369046246d7788db1f3cdf0a34298e35b4b"
SRCREV_machine_qemumips ?= "06c34af69f436c8dca8ff23a30a7d1f834e1d07c"
SRCREV_machine_qemuppc ?= "dadb4369046246d7788db1f3cdf0a34298e35b4b"
SRCREV_machine_qemux86 ?= "dadb4369046246d7788db1f3cdf0a34298e35b4b"
SRCREV_machine_qemux86-64 ?= "dadb4369046246d7788db1f3cdf0a34298e35b4b"
SRCREV_machine_qemumips64 ?= "79b496614c5d0b4862796928ef9890f2eb375a3b"
SRCREV_machine ?= "dadb4369046246d7788db1f3cdf0a34298e35b4b"
SRCREV_meta ?= "89419d8b902aa81cb4c52f9dd12a6c9e083ce198"

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
