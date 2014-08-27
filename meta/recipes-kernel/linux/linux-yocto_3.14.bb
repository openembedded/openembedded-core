require recipes-kernel/linux/linux-yocto.inc

KBRANCH ?= "standard/base"

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "5130b21b5ef3562c3cb03ad401bc9960fdedf49a"
SRCREV_machine_qemumips ?= "ef22686356ce614f4bb3637ce6ee0dab068ea5e7"
SRCREV_machine_qemuppc ?= "c7fed96ea15a49706582d84866f6552a33b3cac4"
SRCREV_machine_qemux86 ?= "e0f04763abba7d487357f98f4267117fd5e79262"
SRCREV_machine_qemux86-64 ?= "5a47bbc4c3c2472f3746a8cf1485db7134cf9245"
SRCREV_machine_qemumips64 ?= "2af2cebe7e8c3755ac1f031c241b0cacad032d93"
SRCREV_machine ?= "5a47bbc4c3c2472f3746a8cf1485db7134cf9245"
SRCREV_meta ?= "ccad961c4cb6be245ed198bd2c17c27ab33cfcd7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
