KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "6d85563640cf2377d702637150ea8314ae915350"
SRCREV_machine_qemumips ?= "bfc1ee46ed29b1bac18deef51620536c66fa83c5"
SRCREV_machine_qemuppc ?= "f4c541bbad8b2a119f86fdc86d3a9d4b8546218b"
SRCREV_machine_qemux86 ?= "cad8f6f70717e54604315db0c9b8889199871e50"
SRCREV_machine_qemux86-64 ?= "cad8f6f70717e54604315db0c9b8889199871e50"
SRCREV_machine_qemumips64 ?= "d6f80d8d30197a278cd386f987998a85e9754a40"
SRCREV_machine ?= "cad8f6f70717e54604315db0c9b8889199871e50"
SRCREV_meta ?= "3283372105ee2f22c1d93ea63fa754179c203e98"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17.2"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
