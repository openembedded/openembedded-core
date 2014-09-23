KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "f075141532317cb7f8b88843354cd901c18c13a1"
SRCREV_machine_qemumips ?= "e7582bdb55376801e9749141af9f1cc0a9425a31"
SRCREV_machine_qemuppc ?= "fdc57cf78d788a5efc44ffaafa09db1a3799e5f0"
SRCREV_machine_qemux86 ?= "e001d3cbec5320c03d2a316aa0e287012be19133"
SRCREV_machine_qemux86-64 ?= "e001d3cbec5320c03d2a316aa0e287012be19133"
SRCREV_machine_qemumips64 ?= "e8fdd57656858f06547f61336e130d006fc88de5"
SRCREV_machine ?= "e001d3cbec5320c03d2a316aa0e287012be19133"
SRCREV_meta ?= "5ea3fd0817c2a3173e85102c61c51a5a6268cad6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.54"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
