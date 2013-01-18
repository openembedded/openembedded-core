require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "50986c7d971c28505acdbdcab6601f4d5b6dbff4"
SRCREV_machine_qemumips  ?= "4d355c57fe0d9f117c272f1679fa3d3f451a8c8e"
SRCREV_machine_qemuppc ?= "1c8a1aa8cc839f0328edbfe0d755100368bc4ae6"
SRCREV_machine_qemux86 ?= "5432e2acb6053f9f7563cf63abd101ed2fdc1b6f"
SRCREV_machine_qemux86-64 ?= "5432e2acb6053f9f7563cf63abd101ed2fdc1b6f"
SRCREV_machine ?= "5432e2acb6053f9f7563cf63abd101ed2fdc1b6f"
SRCREV_meta ?= "d9646442b3763097a425c4e728525685bafc4b89"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.24"

PR = "${INC_PR}.3"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES_append = " features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm"
KERNEL_FEATURES_append_qemux86-64=" cfg/paravirt_kvm"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32", "" ,d)}"
