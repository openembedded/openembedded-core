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

SRCREV_machine_qemuarm ?= "00d625a9e909ff5cbf138cb20dc54e10f30638b3"
SRCREV_machine_qemuarm64 ?= "374b5d0e09ea016975f3d5ab5544968f31054f52"
SRCREV_machine_qemumips ?= "76bf3763b224e6d15c060206015f787e8968a4d7"
SRCREV_machine_qemuppc ?= "6188d8bb5f774f0f760225f34371e94fcf8615d4"
SRCREV_machine_qemux86 ?= "374b5d0e09ea016975f3d5ab5544968f31054f52"
SRCREV_machine_qemux86-64 ?= "374b5d0e09ea016975f3d5ab5544968f31054f52"
SRCREV_machine_qemumips64 ?= "623f6699ea6c4148579c8ede54ccfe0f3784b531"
SRCREV_machine ?= "374b5d0e09ea016975f3d5ab5544968f31054f52"
SRCREV_meta ?= "7215fe431391a322c7e39f410e7b8f2a2b507892"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19.5"

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
