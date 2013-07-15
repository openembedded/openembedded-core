require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "a3bd53305c7dcd11a2683fbe5d830d44236f4cd4"
SRCREV_machine_qemumips  ?= "e142ba98038a697e3a96b8f30a16e93602eff3c7"
SRCREV_machine_qemuppc ?= "290c3dd687145104d4e3f0e7732669c1c42b19fe"
SRCREV_machine_qemux86 ?= "4122d6cf3f9e1fd6d54f6e13af29c4c589289fdc"
SRCREV_machine_qemux86-64 ?= "4122d6cf3f9e1fd6d54f6e13af29c4c589289fdc"
SRCREV_machine ?= "4122d6cf3f9e1fd6d54f6e13af29c4c589289fdc"
SRCREV_meta ?= "7250de4d4afc2558082cd42b8a0d151903436e8f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.52"

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
