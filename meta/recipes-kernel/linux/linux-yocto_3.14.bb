require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b712808b59883692245fc2f28defcc03cdb60dd1"
SRCREV_machine_qemumips ?= "f5b6f121c61c72f91b25f85f19e5d410b7f66b21"
SRCREV_machine_qemuppc ?= "047f8cacac6a0f160ea15fb9f6de6a063ddceb63"
SRCREV_machine_qemux86 ?= "f148e056dbce4206edd68b4fbd913f2757b3a7bb"
SRCREV_machine_qemux86-64 ?= "d61940e2aaeeedc43f166c9e7ac763461f380cd2"
SRCREV_machine_qemumips64 ?= "cc1dad65948575548188866c85fae5fbd55c7e66"
SRCREV_machine ?= "d61940e2aaeeedc43f166c9e7ac763461f380cd2"
SRCREV_meta ?= "3eefa4379f073768df150184e9dad1ff3228a0ff"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.13"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
