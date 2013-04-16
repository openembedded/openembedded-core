require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "1473dd2b4505109d45bd7099f63826a66b71103d"
SRCREV_machine_qemumips  ?= "1695c38d04855cc225bcc1968289770f0fa0ea64"
SRCREV_machine_qemuppc ?= "8b39c0424704a40c740b1ac61449fdf190a8460a"
SRCREV_machine_qemux86 ?= "d9a45e3325030f7bd6f37947a7a0b12da7f602c3"
SRCREV_machine_qemux86-64 ?= "d9a45e3325030f7bd6f37947a7a0b12da7f602c3"
SRCREV_machine ?= "d9a45e3325030f7bd6f37947a7a0b12da7f602c3"
SRCREV_meta ?= "27b63fdbd25ad1a37bacc05f49a205c150d21779"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.4"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
