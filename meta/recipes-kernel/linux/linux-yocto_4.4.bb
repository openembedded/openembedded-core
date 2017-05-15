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

SRCREV_machine_qemuarm ?= "114f5a4f97fe44ac95c3e0ed370bd932b9296510"
SRCREV_machine_qemuarm64 ?= "98b9340a7858f786f5ff7d50021ef83eaad0613d"
SRCREV_machine_qemumips ?= "b0b3b8012a49293e98ba7601af03ee2e02ddbc85"
SRCREV_machine_qemuppc ?= "98b9340a7858f786f5ff7d50021ef83eaad0613d"
SRCREV_machine_qemux86 ?= "98b9340a7858f786f5ff7d50021ef83eaad0613d"
SRCREV_machine_qemux86-64 ?= "98b9340a7858f786f5ff7d50021ef83eaad0613d"
SRCREV_machine_qemumips64 ?= "a9b8241a0c0e7626baba529abb4940831ff3a83e"
SRCREV_machine ?= "98b9340a7858f786f5ff7d50021ef83eaad0613d"
SRCREV_meta ?= "db18675035528619e366d71de17bb84c2b9804c5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.67"

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
