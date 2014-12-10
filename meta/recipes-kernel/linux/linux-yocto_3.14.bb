require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "9882294294f307dc52c29e5dbb24d7f9a2f5a635"
SRCREV_machine_qemumips ?= "94e2f8429bcc953cb0df499446b847ba67a0f334"
SRCREV_machine_qemuppc ?= "2795a4d16724b41fbcd62c2f9f56c427d1ce3797"
SRCREV_machine_qemux86 ?= "4aa41764bf8dba2044ff9fae806d61cac7cdd9de"
SRCREV_machine_qemux86-64 ?= "cb22733185cd9db3e8945dadb899d9eb3831b9ad"
SRCREV_machine_qemumips64 ?= "342ccacd4e28d1cc7e30277ee4ac6caa3086ff2f"
SRCREV_machine ?= "cb22733185cd9db3e8945dadb899d9eb3831b9ad"
SRCREV_meta ?= "183622e8095545999a64bd72adedea2dffb6ec4b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.4"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
