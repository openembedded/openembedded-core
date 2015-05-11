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

SRCREV_machine_qemuarm ?= "123f12cf162082802442d70701d83749376c45df"
SRCREV_machine_qemuarm64 ?= "0befa35f40997c90936047e57fbbdd7edbf7e525"
SRCREV_machine_qemumips ?= "c6bc6e6a10b188f96b9761411421be78ee67aa75"
SRCREV_machine_qemuppc ?= "b79584c854f875890d708d7fb3cda85430a9d3ec"
SRCREV_machine_qemux86 ?= "0befa35f40997c90936047e57fbbdd7edbf7e525"
SRCREV_machine_qemux86-64 ?= "0befa35f40997c90936047e57fbbdd7edbf7e525"
SRCREV_machine_qemumips64 ?= "b25c62fa52d51dac118b0eed20b8d5971434fa80"
SRCREV_machine ?= "0befa35f40997c90936047e57fbbdd7edbf7e525"
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
