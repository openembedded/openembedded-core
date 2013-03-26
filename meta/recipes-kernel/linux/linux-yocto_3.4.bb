require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "7cc80532306889b75619f8a1b713048e25f59e19"
SRCREV_machine_qemumips  ?= "debce6677988e03b50c369aba5861d4f9b2e557d"
SRCREV_machine_qemuppc ?= "ddbc382cbc45a009e9ce17f7d448fcfd050ab5fc"
SRCREV_machine_qemux86 ?= "c994390cfa28339cbc1ec3b56eeec83a5fa75bb7"
SRCREV_machine_qemux86-64 ?= "c994390cfa28339cbc1ec3b56eeec83a5fa75bb7"
SRCREV_machine ?= "c994390cfa28339cbc1ec3b56eeec83a5fa75bb7"
SRCREV_meta ?= "80b4b5110dca5184b57e85f1e775fb006a2e85ad"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.36"

PR = "${INC_PR}.3"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
