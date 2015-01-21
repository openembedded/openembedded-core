KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "7a8c197b86281f9a445e23378fdfeeb4da0c48e9"
SRCREV_machine_qemumips ?= "926e71942842a9d1a9f64aae0458b2d777d3bca9"
SRCREV_machine_qemuppc ?= "65648fce58a5e095cfe2bf394bc0f6200efb281c"
SRCREV_machine_qemux86 ?= "a2f2be49cd60b8d022fa47daae0a8293c3066b78"
SRCREV_machine_qemux86-64 ?= "a2f2be49cd60b8d022fa47daae0a8293c3066b78"
SRCREV_machine_qemumips64 ?= "06d173388a171e7371816d74567fdec994925aa4"
SRCREV_machine ?= "a2f2be49cd60b8d022fa47daae0a8293c3066b78"
SRCREV_meta ?= "d5456dd830cad14bd844753b751b83744ced3793"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.65"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
