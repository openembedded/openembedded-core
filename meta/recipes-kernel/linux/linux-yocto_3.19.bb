KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "4f9b327ff33521c08ed279891e2a2099b5504d0e"
SRCREV_machine_qemuarm64 ?= "d5d30ba4d20e65c15df624ffce7a5cd38150348b"
SRCREV_machine_qemumips ?= "03b975caa49e6e1693d6fe5fec8f316e0481ead5"
SRCREV_machine_qemuppc ?= "9a3edc9c341e6f57423f2b4b218b83a84fc2726d"
SRCREV_machine_qemux86 ?= "d5d30ba4d20e65c15df624ffce7a5cd38150348b"
SRCREV_machine_qemux86-64 ?= "d5d30ba4d20e65c15df624ffce7a5cd38150348b"
SRCREV_machine_qemumips64 ?= "f631de1c3df29a85a4b882acf19682fe05eec0f3"
SRCREV_machine ?= "d5d30ba4d20e65c15df624ffce7a5cd38150348b"
SRCREV_meta ?= "7215fe431391a322c7e39f410e7b8f2a2b507892"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19.5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
