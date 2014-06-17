require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "2489f1f4d7aa27bf0808d7fa80a50399c643516d"
SRCREV_machine_qemumips ?= "63b4ca3223d9481baa77f527f50d906d15747141"
SRCREV_machine_qemuppc ?= "e5dfe8f89b45effe445d04e0f9b391948eb108ae"
SRCREV_machine_qemux86 ?= "41d5fe27dc3d3e769cb6af01770cac3d75a91e1a"
SRCREV_machine_qemux86-64 ?= "96930820e0cb6d4b31d5e0c8f3174805f4a868b3"
SRCREV_machine_qemumips64 ?= "103df748c6e83d5874a8385592f758feeb818324"
SRCREV_machine ?= "96930820e0cb6d4b31d5e0c8f3174805f4a868b3"
SRCREV_meta ?= "602be954ac45e8aea06cb93d6766bbf83c242090"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
