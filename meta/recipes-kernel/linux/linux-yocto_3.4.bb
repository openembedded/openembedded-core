require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "169d2f318fdfe980cac9b688bd38f508cd4436d6"
SRCREV_machine_qemumips  ?= "80467d492ecefd82f733632b08323a728587c277"
SRCREV_machine_qemuppc ?= "e954091f4a17ad1dfdec3989382aab55dc666e79"
SRCREV_machine_qemux86 ?= "79ba9469f349566c49ae007326198e3241a0d5d3"
SRCREV_machine_qemux86-64 ?= "79ba9469f349566c49ae007326198e3241a0d5d3"
SRCREV_machine ?= "79ba9469f349566c49ae007326198e3241a0d5d3"
SRCREV_meta ?= "80b4b5110dca5184b57e85f1e775fb006a2e85ad"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.36"

PR = "${INC_PR}.4"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
