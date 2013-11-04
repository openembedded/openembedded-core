require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "47ba9c54bfa38b70e2c7500a7e30a738c667cb95"
SRCREV_machine_qemumips  ?= "b9d36bdb86b595d9823392402b31bdb76439054c"
SRCREV_machine_qemuppc ?= "e13586324a05aba0f0cf2523ad9eecda2edd3b94"
SRCREV_machine_qemux86 ?= "375aee37c2508899b6a8c0bdff7d4d67cb75fb36"
SRCREV_machine_qemux86-64 ?= "375aee37c2508899b6a8c0bdff7d4d67cb75fb36"
SRCREV_machine_qemumips64 ?= "44eaa7b02aa1e4b8e539c2227702f74f9fcbcfea"
SRCREV_machine ?= "375aee37c2508899b6a8c0bdff7d4d67cb75fb36"
SRCREV_meta ?= "f1c9080cd27f99700fa59b5375d1ddd0afe625ad"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
