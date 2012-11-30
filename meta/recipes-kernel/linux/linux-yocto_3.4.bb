require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "092d66e326abfda80c50dcf1869417d7420847f1"
SRCREV_machine_qemumips  ?= "b258437665477f44a868c38c6c9cc5199db58cd8"
SRCREV_machine_qemuppc ?= "67a370223d695ec08b6e5a38860fa1fe5d0d9f1e"
SRCREV_machine_qemux86 ?= "b13bef6377719a488293af196236cc290566fad3"
SRCREV_machine_qemux86-64 ?= "b13bef6377719a488293af196236cc290566fad3"
SRCREV_machine ?= "b13bef6377719a488293af196236cc290566fad3"
SRCREV_meta ?= "8b0a93c0a224c9b91f90545b123b055b2e313bfc"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.20"

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
