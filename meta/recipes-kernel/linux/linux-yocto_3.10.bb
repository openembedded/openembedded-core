require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "aa1fa3cec7bd6e47f29acc5b5fbffddc16569883"
SRCREV_machine_qemumips  ?= "5b908fd12f60de1e51e932c5df477a49b0ab2b40"
SRCREV_machine_qemuppc ?= "e80029ac30022c554e916ed438435ecc03cc2cea"
SRCREV_machine_qemux86 ?= "e1aa804148370cda6f85640281af156ffa007d52"
SRCREV_machine_qemux86-64 ?= "e1aa804148370cda6f85640281af156ffa007d52"
SRCREV_machine_qemumips64 ?= "6973844d304411893420a7e57545edc4dc854bd7"
SRCREV_machine ?= "e1aa804148370cda6f85640281af156ffa007d52"
SRCREV_meta ?= "dad2b7e1ceed654fba89907f3e14050007699b90"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.11"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
