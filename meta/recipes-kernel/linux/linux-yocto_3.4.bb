require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "e1dd6f40b76b3e9bd0686629004621aeddc6a982"
SRCREV_machine_qemumips  ?= "47af1ab871c8dfa4428cec26ec74e96a5b10c566"
SRCREV_machine_qemuppc ?= "65e4b20a87b02cf7bcb3ad3f725a079933828d4d"
SRCREV_machine_qemux86 ?= "ea977edd05ae2ebfa82731e0bee309bdfd08abee"
SRCREV_machine_qemux86-64 ?= "ea977edd05ae2ebfa82731e0bee309bdfd08abee"
SRCREV_machine ?= "ea977edd05ae2ebfa82731e0bee309bdfd08abee"
SRCREV_meta ?= "f36797c2df3fbe9491c8ac5977fb691f4a75e9b7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.59"

PR = "${INC_PR}.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
