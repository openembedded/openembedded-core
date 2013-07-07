require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "89c1e24271fba1c235959cb96c91dba98c542821"
SRCREV_machine_qemumips  ?= "99e87eb8b1e985eb6addf7590c4e837ff7c7e873"
SRCREV_machine_qemuppc ?= "62f5c50609c93c5b70736a4374cee8075ab82566"
SRCREV_machine_qemux86 ?= "cdd7a546922ca1c46c94adeec3b9c90dc9aaad2d"
SRCREV_machine_qemux86-64 ?= "cdd7a546922ca1c46c94adeec3b9c90dc9aaad2d"
SRCREV_machine ?= "cdd7a546922ca1c46c94adeec3b9c90dc9aaad2d"
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
