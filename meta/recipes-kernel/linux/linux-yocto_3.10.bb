require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "6a38a13d2aa6058d7e7d88c3dc44b0bdfc4fe551"
SRCREV_machine_qemumips  ?= "6c8921e44618938847865e4a9951086d7dc23085"
SRCREV_machine_qemuppc ?= "677afd6cdbfb5c23a9e5479acafb7ba484789837"
SRCREV_machine_qemux86 ?= "ebc8428fdd938cfdfcdcadd77c3308ece6a57de1"
SRCREV_machine_qemux86-64 ?= "ebc8428fdd938cfdfcdcadd77c3308ece6a57de1"
SRCREV_machine_qemumips64 ?= "ebc55bed9558a0e2f390e9cef03161b299138326"
SRCREV_machine ?= "ebc8428fdd938cfdfcdcadd77c3308ece6a57de1"
SRCREV_meta ?= "ea900d1db60ba48962227f0976ac55f9e25bfa24"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.10"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
