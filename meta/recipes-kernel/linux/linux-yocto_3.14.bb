KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "90f1033d72d422b4e4a8125f68cd1623d6788870"
SRCREV_machine_qemuarm64 ?= "2a2be8720802fae73fbdf1da37f61d7843332ba3"
SRCREV_machine_qemumips ?= "bc927927f774c9d12f6c4689dbfba9f5578d0b62"
SRCREV_machine_qemuppc ?= "e4dfdbf1a059e0edb1be677973f8e2c73e012c8a"
SRCREV_machine_qemux86 ?= "594bfbce3133d75d9aa569883bdeaed15eeadc8f"
SRCREV_machine_qemux86-64 ?= "2a2be8720802fae73fbdf1da37f61d7843332ba3"
SRCREV_machine_qemumips64 ?= "ea8b5406c6ec027d0475146ac84f24eb34196b78"
SRCREV_machine ?= "2a2be8720802fae73fbdf1da37f61d7843332ba3"
SRCREV_meta ?= "46df2668fa657162d11f96a6f1af138c562a03aa"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.36"

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
