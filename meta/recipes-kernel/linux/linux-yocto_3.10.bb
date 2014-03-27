require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "0e99eabbe5b3bec853ace496f94612bc37800260"
SRCREV_machine_qemumips ?= "b6b20f49e9a169a0672b7cc2d7b93d6652ca7873"
SRCREV_machine_qemuppc ?= "d71b782615b802c78e1586494b94dd40531775c8"
SRCREV_machine_qemux86 ?= "c7739be126930006e3bfbdb2fb070a967abc5e09"
SRCREV_machine_qemux86-64 ?= "c7739be126930006e3bfbdb2fb070a967abc5e09"
SRCREV_machine_qemumips64 ?= "88abb1eb327f4e3c711d19a228cb153fdd9b3506"
SRCREV_machine ?= "c7739be126930006e3bfbdb2fb070a967abc5e09"
SRCREV_meta ?= "df3aa753c8826127fb5ad811d56d57168551d6e4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.34"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
