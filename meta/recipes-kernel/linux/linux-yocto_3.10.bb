KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "9c8da40f662806406940decf972841c1b86895a5"
SRCREV_machine_qemumips ?= "96adff9ce54c009ba90c3ddcaa6d16beb7a9c2b3"
SRCREV_machine_qemuppc ?= "cf3a9a021e2abb3b19807b2ba3c29c1d0918730b"
SRCREV_machine_qemux86 ?= "3677ea7f9476458aa6dec440243de3a6fb1343a9"
SRCREV_machine_qemux86-64 ?= "3677ea7f9476458aa6dec440243de3a6fb1343a9"
SRCREV_machine_qemumips64 ?= "89ab3082e01c4d98f9d14630aa904dba641c5373"
SRCREV_machine ?= "3677ea7f9476458aa6dec440243de3a6fb1343a9"
SRCREV_meta ?= "f79a00265eefbe2fffc2cdb03f67235497a9a87e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.55"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
