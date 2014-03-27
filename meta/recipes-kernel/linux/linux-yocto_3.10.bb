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

SRCREV_machine_qemuarm ?= "e18e8c2c7cad913a25342f9f860eeeefce24b8aa"
SRCREV_machine_qemumips ?= "6f191aaecfdbebda450cec4a1f30fb0d1a2ed889"
SRCREV_machine_qemuppc ?= "ba2e16160c7f910de432511ca0008101a7b2263b"
SRCREV_machine_qemux86 ?= "2ee37bfe732c73f7d39af55875ce8d30b282471c"
SRCREV_machine_qemux86-64 ?= "2ee37bfe732c73f7d39af55875ce8d30b282471c"
SRCREV_machine_qemumips64 ?= "e05f0378e9c21d689eed8aacb306d2c1a044e0d0"
SRCREV_machine ?= "2ee37bfe732c73f7d39af55875ce8d30b282471c"
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
