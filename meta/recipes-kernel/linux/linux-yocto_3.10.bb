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

SRCREV_machine_qemuarm ?= "670a1d401ace1c5894a87466e5136ed60cf66966"
SRCREV_machine_qemumips ?= "45fc480deb2f4e5b42265c59fa4bec784e995294"
SRCREV_machine_qemuppc ?= "ac1c54b68ee44c00906933547b310652fd725d4b"
SRCREV_machine_qemux86 ?= "21df0c8486e129a4087970a07b423c533ae05de7"
SRCREV_machine_qemux86-64 ?= "21df0c8486e129a4087970a07b423c533ae05de7"
SRCREV_machine_qemumips64 ?= "31139804e58e049e696fa49175544a43c40b3506"
SRCREV_machine ?= "21df0c8486e129a4087970a07b423c533ae05de7"
SRCREV_meta ?= "284e9589436a5b199cf44b7261640c944558a35e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.33"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
