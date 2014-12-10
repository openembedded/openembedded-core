KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "f6b075991c91b7c2bb641b389757863e2fd34b8c"
SRCREV_machine_qemumips ?= "48afdc632312b6cc26fe7bca151cfb66b2ebc308"
SRCREV_machine_qemuppc ?= "cf35ea9ac92153858dadd2f4ab71cccd3a1fa26b"
SRCREV_machine_qemux86 ?= "b2ac933df119a3444a32fcccf5e4ad453f5ac89d"
SRCREV_machine_qemux86-64 ?= "b2ac933df119a3444a32fcccf5e4ad453f5ac89d"
SRCREV_machine_qemumips64 ?= "ebdb374ca5130ef456d0baf75b6fe7a242932d0d"
SRCREV_machine ?= "b2ac933df119a3444a32fcccf5e4ad453f5ac89d"
SRCREV_meta ?= "f4ab00d96a3d8e443d7f7744ad996e184eac03b5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.62"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
