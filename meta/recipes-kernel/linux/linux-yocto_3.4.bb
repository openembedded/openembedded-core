require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "c1e1ed0f4ab5f90f11f49d4d1aa79b378a14708f"
SRCREV_machine_qemumips  ?= "d6138383ef03c70e3580beffe318e6a2f1a2767e"
SRCREV_machine_qemuppc ?= "9ef6eddfeeb8e5cda01c008c4fa49629902561de"
SRCREV_machine_qemux86 ?= "782622711662dc3676040f085f589d10780a1e33"
SRCREV_machine_qemux86-64 ?= "782622711662dc3676040f085f589d10780a1e33"
SRCREV_machine ?= "782622711662dc3676040f085f589d10780a1e33"
SRCREV_meta ?= "6d4cb3c86cde2f37ea1d67933a1d24e45920753c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.23"

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
