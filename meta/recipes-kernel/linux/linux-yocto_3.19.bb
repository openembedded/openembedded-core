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

SRCREV_machine_qemuarm ?= "060c6306d0be2726638ab18bd68977de4c7a38e5"
SRCREV_machine_qemuarm64 ?= "0a0d499541e2bb13ecc1001d0947bed6106c26a6"
SRCREV_machine_qemumips ?= "4b206f611f6d4c35a27a44e24ba0cedfc71301ec"
SRCREV_machine_qemuppc ?= "18e00694ed76d18bf020e3288a6fdce63412887d"
SRCREV_machine_qemux86 ?= "0a0d499541e2bb13ecc1001d0947bed6106c26a6"
SRCREV_machine_qemux86-64 ?= "0a0d499541e2bb13ecc1001d0947bed6106c26a6"
SRCREV_machine_qemumips64 ?= "0e31a9b3e54a2c87645f34bcbbe704ac62c04602"
SRCREV_machine ?= "0a0d499541e2bb13ecc1001d0947bed6106c26a6"
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
