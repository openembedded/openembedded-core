KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "dbd8e70653ffb6b63a1b8b7134813d3a93975ff5"
SRCREV_machine_qemuarm64 ?= "bda175966009d5a94103559e6e6ae51279952f39"
SRCREV_machine_qemumips ?= "b985d5039e5b14f5acac135a84f2d6541b531552"
SRCREV_machine_qemuppc ?= "dbbdaf47fae37664ea30181b18a436b3d5401293"
SRCREV_machine_qemux86 ?= "d3943ebf24457188669472c85d82ab32e2f95f6b"
SRCREV_machine_qemux86-64 ?= "bda175966009d5a94103559e6e6ae51279952f39"
SRCREV_machine_qemumips64 ?= "a594ff1fef248d72bf4ef929fa4497cf54a04500"
SRCREV_machine ?= "bda175966009d5a94103559e6e6ae51279952f39"
SRCREV_meta ?= "a996d95104b72c422a56e7d9bc8615ec4219ac74"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.36"

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
