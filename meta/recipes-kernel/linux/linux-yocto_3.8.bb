require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "3a73643770a32ea6c86724e92e1e5abecb5dc822"
SRCREV_machine_qemumips  ?= "1aefa19417793412ef87217a4cb5d1074dc549ba"
SRCREV_machine_qemuppc ?= "8844013c81e9ad80246aabca6573eed9dbaac646"
SRCREV_machine_qemux86 ?= "15a0766b3d007e5fafce3503375694dff2b0603e"
SRCREV_machine_qemux86-64 ?= "15a0766b3d007e5fafce3503375694dff2b0603e"
SRCREV_machine ?= "15a0766b3d007e5fafce3503375694dff2b0603e"
SRCREV_meta ?= "1b534b2f8bbe9b8a773268cfa30a4850346f6f5f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
