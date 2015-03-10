KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "a40304a6dfdad6618420854121ee91cf09481bd4"
SRCREV_machine_qemuarm64 ?= "8c38c1015bbb5fc121018f67ed45a7eb2f357cfe"
SRCREV_machine_qemumips ?= "6f1c5213591d7a35efc542a0971c70dcd16ea00e"
SRCREV_machine_qemuppc ?= "6b9a1cb9e10e76d8a3102c046656c93651fde9ab"
SRCREV_machine_qemux86 ?= "8c38c1015bbb5fc121018f67ed45a7eb2f357cfe"
SRCREV_machine_qemux86-64 ?= "8c38c1015bbb5fc121018f67ed45a7eb2f357cfe"
SRCREV_machine_qemumips64 ?= "723d6e1eb8dfa06f374d925d2c36e22feb1a2c86"
SRCREV_machine ?= "8c38c1015bbb5fc121018f67ed45a7eb2f357cfe"
SRCREV_meta ?= "e3303ca4bd7d7a5a3d9a9a9a9467c4c70db4258d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19.1"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
