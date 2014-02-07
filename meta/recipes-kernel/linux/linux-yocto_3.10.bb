require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "2a4d8c0884c8d08896c052a6e42c51e861cf48b5"
SRCREV_machine_qemumips ?= "08872a160571b9bf45b6cd262b977063f12aca46"
SRCREV_machine_qemuppc ?= "eba0c4fcc962ffddd384251b5b6ee0c61bd5ea4d"
SRCREV_machine_qemux86 ?= "78d2a615b13a1c307d482eaa9499c1b2dee40599"
SRCREV_machine_qemux86-64 ?= "78d2a615b13a1c307d482eaa9499c1b2dee40599"
SRCREV_machine_qemumips64 ?= "de555074575d2997a8cd7a4b2d6dbf22e7ddfc9b"
SRCREV_machine ?= "78d2a615b13a1c307d482eaa9499c1b2dee40599"
SRCREV_meta ?= "713abc0efa9fc21234b2f342d0a849e4a4a36c65"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.25"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
