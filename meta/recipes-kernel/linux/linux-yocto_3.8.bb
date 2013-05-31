require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "e267fe88798892416828d89bde7f778380b87a90"
SRCREV_machine_qemumips  ?= "813bae2e17db9310f220935e87d168c8e52fafaf"
SRCREV_machine_qemuppc ?= "f90923775f9bcec3ce23246e8fb59d8f9551e566"
SRCREV_machine_qemux86 ?= "1f973c0fc8eea9a8f9758f47cf689ba89dbe9a25"
SRCREV_machine_qemux86-64 ?= "1f973c0fc8eea9a8f9758f47cf689ba89dbe9a25"
SRCREV_machine ?= "1f973c0fc8eea9a8f9758f47cf689ba89dbe9a25"
SRCREV_meta ?= "edd6461602f6c2fc27bc72997e4437f422a9dccd"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.13"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
