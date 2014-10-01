KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "787dbffa74ebb7e0f3d8ade959623c69c9c066a0"
SRCREV_machine_qemumips ?= "417faf05188f982bb1d47709f7c152a5cf116a0d"
SRCREV_machine_qemuppc ?= "975512c7f6caa812869c4fa0ed1788072bf383f6"
SRCREV_machine_qemux86 ?= "8b5bc69a2e44100313cf2a5073a07f495a6ff08e"
SRCREV_machine_qemux86-64 ?= "8b5bc69a2e44100313cf2a5073a07f495a6ff08e"
SRCREV_machine_qemumips64 ?= "39878abc7e56c23cd4bc87b2c5d812d489903dee"
SRCREV_machine ?= "8b5bc69a2e44100313cf2a5073a07f495a6ff08e"
SRCREV_meta ?= "6050ee24b0b51ccaf35894afc5def0b59f434977"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc6"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
