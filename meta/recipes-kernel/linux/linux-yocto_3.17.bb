KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "e86f70699655d1f5bf2518c8dcdb949a39161c86"
SRCREV_machine_qemumips ?= "0acaa5991a61fbab0d897157049ac2e638e9c7bf"
SRCREV_machine_qemuppc ?= "50e33fec112527de12c2833cd8a283dd4a75ae33"
SRCREV_machine_qemux86 ?= "1a6dd1205de5b7c6135a42fec00550738ff777b5"
SRCREV_machine_qemux86-64 ?= "1a6dd1205de5b7c6135a42fec00550738ff777b5"
SRCREV_machine_qemumips64 ?= "c0558aaa8251a022447a4ac782d4e707401c52d3"
SRCREV_machine ?= "1a6dd1205de5b7c6135a42fec00550738ff777b5"
SRCREV_meta ?= "9ba007f8d0abf3cc5499e8eee13065b7f0713e81"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
