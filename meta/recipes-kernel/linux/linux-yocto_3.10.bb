require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ccf8a3041cd815d3b401000345176870903cf77f"
SRCREV_machine_qemumips ?= "5233fddda6c698399eca44320027d5ee3ba662a8"
SRCREV_machine_qemuppc ?= "265e7bcceaa36d7cddbc20d73e451d376b66fb30"
SRCREV_machine_qemux86 ?= "25d0ebad3e2bf9f12bd6d386ead68e1f64ceaf29"
SRCREV_machine_qemux86-64 ?= "25d0ebad3e2bf9f12bd6d386ead68e1f64ceaf29"
SRCREV_machine_qemumips64 ?= "7a092b58f5f8f8f7893b2ee05d3d725ac2968d99"
SRCREV_machine ?= "25d0ebad3e2bf9f12bd6d386ead68e1f64ceaf29"
SRCREV_meta ?= "713abc0efa9fc21234b2f342d0a849e4a4a36c65"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.28"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
