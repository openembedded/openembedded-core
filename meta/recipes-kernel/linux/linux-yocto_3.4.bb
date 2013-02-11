require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "a720acacc415b3323a3ee9076bf9a621a964af2f"
SRCREV_machine_qemumips  ?= "7d9f14326db1e602b4408f295bef7d12fd07dd55"
SRCREV_machine_qemuppc ?= "62d3cb6a8e048c4833a5501bea2f35998de0b89d"
SRCREV_machine_qemux86 ?= "13809f2cfd9be0ce86bd486e1643f9b90bed6f4f"
SRCREV_machine_qemux86-64 ?= "13809f2cfd9be0ce86bd486e1643f9b90bed6f4f"
SRCREV_machine ?= "13809f2cfd9be0ce86bd486e1643f9b90bed6f4f"
SRCREV_meta ?= "c0b1845314cebb21694dbb4459fcc6f80feb01d0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.28"

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
