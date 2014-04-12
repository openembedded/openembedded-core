require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "d7a5330e50f78f63789b91cb9880b38633d33450"
SRCREV_machine_qemumips ?= "38f344fcab8c0ee29f40edb92dd6612eff4579bf"
SRCREV_machine_qemuppc ?= "b28241db34ab66f9e86718d8c94276d65d2457bb"
SRCREV_machine_qemux86 ?= "f9048769cc178f2f64ed492a9a649827167d9a34"
SRCREV_machine_qemux86-64 ?= "144595ef6215a0febfb8ee7d0c9e4eb2eaf93d61"
SRCREV_machine_qemumips64 ?= "c4e08d47c5eb36ae056f2eab82a74c3638e72e06"
SRCREV_machine ?= "144595ef6215a0febfb8ee7d0c9e4eb2eaf93d61"
SRCREV_meta ?= "09424cee646626c04105f08455a58fabb27eff31"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
