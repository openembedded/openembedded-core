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

SRCREV_machine_qemuarm ?= "c75bfc837fa8079278e77e8c122fe506c4ba7438"
SRCREV_machine_qemuarm64 ?= "6c21811060c03100a32d0acc493df5fb3743b7c8"
SRCREV_machine_qemumips ?= "b6aae264b3d4e74cc29a5db8c376840579d1f433"
SRCREV_machine_qemuppc ?= "f6c2fa7ca86bd548cc96bd0ddf8aa89229e4c2c2"
SRCREV_machine_qemux86 ?= "6c21811060c03100a32d0acc493df5fb3743b7c8"
SRCREV_machine_qemux86-64 ?= "6c21811060c03100a32d0acc493df5fb3743b7c8"
SRCREV_machine_qemumips64 ?= "d3a1f6f5501167c76fb662d518140133f5ce78c5"
SRCREV_machine ?= "6c21811060c03100a32d0acc493df5fb3743b7c8"
SRCREV_meta ?= "118f27167f4626fc7c71cefbec7c55a41d3a6d62"

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
