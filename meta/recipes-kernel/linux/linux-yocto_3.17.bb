KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "d156e4658a13349f7a30adafd436e32fb722d248"
SRCREV_machine_qemumips ?= "5aa58a6ca9d4efe79ddf66c9d09695b42c84a3b6"
SRCREV_machine_qemuppc ?= "cc4943bf3b93f9c424b63c357ab2c9f71384f3ac"
SRCREV_machine_qemux86 ?= "268b7677421eef003a84f31c6bd0b8ec3acc1e36"
SRCREV_machine_qemux86-64 ?= "268b7677421eef003a84f31c6bd0b8ec3acc1e36"
SRCREV_machine_qemumips64 ?= "798c30525b1e0eacfa3ff97c4bb17c45b9b4b220"
SRCREV_machine ?= "268b7677421eef003a84f31c6bd0b8ec3acc1e36"
SRCREV_meta ?= "84b57b01050dd9e5a004e265e70120e2a792f38a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc7"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
