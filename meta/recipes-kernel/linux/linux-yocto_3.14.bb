KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b768ebfe28a20b65aba734b9f6646a3fa0adc89b"
SRCREV_machine_qemumips ?= "c76146e6c8a015c1225141fc55d867a3e47453c6"
SRCREV_machine_qemuppc ?= "e54ab8451c482b695ff7a0c580ef2fb1be605299"
SRCREV_machine_qemux86 ?= "146fcb1dd7b3c5547d67f04d50b082f0723741e9"
SRCREV_machine_qemux86-64 ?= "c100e8665052051487a17169748c457829d3f88c"
SRCREV_machine_qemumips64 ?= "71621ee758600abd3cf7187d7a8e7d51f4052c53"
SRCREV_machine ?= "c100e8665052051487a17169748c457829d3f88c"
SRCREV_meta ?= "fba2d0cdb745e0f807ce134fd9d1524b7bed9742"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.24"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
