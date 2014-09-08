KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "4bacea69cf78bdf42bcf7619239294b8996c5dac"
SRCREV_machine_qemumips ?= "69d4e97c23d4d031f55645cd0486064979e4db5c"
SRCREV_machine_qemuppc ?= "4e5f8849093f71f8caebedd8af4660baace9bccb"
SRCREV_machine_qemux86 ?= "1a3939eb747a635b23b4234f785795c52dce72a8"
SRCREV_machine_qemux86-64 ?= "1a3939eb747a635b23b4234f785795c52dce72a8"
SRCREV_machine_qemumips64 ?= "5e036fb17397a241fda2211a6caaa6e6f3121250"
SRCREV_machine ?= "1a3939eb747a635b23b4234f785795c52dce72a8"
SRCREV_meta ?= "7df849fc7eba54765368e26634341dbe6e2ad161"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-dev.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc4"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
