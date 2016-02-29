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

SRCREV_machine_qemuarm ?= "1505f695c60842fa591ca6bc1e3cb0bba1be0055"
SRCREV_machine_qemuarm64 ?= "d4eb3ab036f8c37c5bc5f45ad0fa4dc34b7228c8"
SRCREV_machine_qemumips ?= "257ab9142b29e01b257977f5a3afe7f40f79f7bc"
SRCREV_machine_qemuppc ?= "d4eb3ab036f8c37c5bc5f45ad0fa4dc34b7228c8"
SRCREV_machine_qemux86 ?= "d4eb3ab036f8c37c5bc5f45ad0fa4dc34b7228c8"
SRCREV_machine_qemux86-64 ?= "d4eb3ab036f8c37c5bc5f45ad0fa4dc34b7228c8"
SRCREV_machine_qemumips64 ?= "de5aa76a9b909645533b44018bed0aa110e2dbd0"
SRCREV_machine ?= "d4eb3ab036f8c37c5bc5f45ad0fa4dc34b7228c8"
SRCREV_meta ?= "4d2d541ae090e9646e6fb06ad296bdba54fd7497"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.1"

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
