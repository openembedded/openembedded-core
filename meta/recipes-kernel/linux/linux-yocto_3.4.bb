require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "38fe75106c5faa3ea49d0a285769de3d08478f90"
SRCREV_machine_qemumips  ?= "67c8918ce3cfd66f19f23b46381993ff488b3fe0"
SRCREV_machine_qemuppc ?= "0e81c96b36e05746ae978a830fe3dbdac8a51e58"
SRCREV_machine_qemux86 ?= "7f4d818b0450a5dc79f81b51dc7d13d0682b1287"
SRCREV_machine_qemux86-64 ?= "7f4d818b0450a5dc79f81b51dc7d13d0682b1287"
SRCREV_machine ?= "7f4d818b0450a5dc79f81b51dc7d13d0682b1287"
SRCREV_meta ?= "f36797c2df3fbe9491c8ac5977fb691f4a75e9b7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.69"

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
