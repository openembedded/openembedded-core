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

SRCREV_machine_qemuarm ?= "97428dbb2231f8a715de9db66f3c2358e4927716"
SRCREV_machine_qemuarm64 ?= "a3f6f39fc369dba2816908bc1b324e0170aaa27d"
SRCREV_machine_qemumips ?= "a4573b23f8fa35a6e93131fac2f1c0f1d5115fea"
SRCREV_machine_qemuppc ?= "8bbd8a1369c773dbe8bed27b613aa4b7dd175670"
SRCREV_machine_qemux86 ?= "a3f6f39fc369dba2816908bc1b324e0170aaa27d"
SRCREV_machine_qemux86-64 ?= "a3f6f39fc369dba2816908bc1b324e0170aaa27d"
SRCREV_machine_qemumips64 ?= "62681c2cfa32bee03aee368b704228bc91c2483f"
SRCREV_machine ?= "a3f6f39fc369dba2816908bc1b324e0170aaa27d"
SRCREV_meta ?= "44879ee54dce8e11695dcb476d1b178a48a71f6a"

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
