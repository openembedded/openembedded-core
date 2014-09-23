KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "a4195791d27ef50feb454765149ea34621ef8ed7"
SRCREV_machine_qemumips ?= "ff747b3dff7dfa643b3447a348b901cacc5b6a16"
SRCREV_machine_qemuppc ?= "46e30d608f4268b0ea77362398aae91f1d410ee3"
SRCREV_machine_qemux86 ?= "8e055f3b669c65e83ba7128c248c632eedafad72"
SRCREV_machine_qemux86-64 ?= "8e055f3b669c65e83ba7128c248c632eedafad72"
SRCREV_machine_qemumips64 ?= "42457c568170cb11a9011382ebca4677f22b35c1"
SRCREV_machine ?= "8e055f3b669c65e83ba7128c248c632eedafad72"
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
