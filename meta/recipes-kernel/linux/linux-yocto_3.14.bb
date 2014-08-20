require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b91ab4c1bf0dccef4c93aeb184fca3a933682ba0"
SRCREV_machine_qemumips ?= "1fdf6bfb5ecefccddead6e1d53b6e19d52da092e"
SRCREV_machine_qemuppc ?= "8a44bd5e4fca2260d2e24e2cb4a5f276d000fae8"
SRCREV_machine_qemux86 ?= "984636272f33a48803e1b0f8f48553894306b4e2"
SRCREV_machine_qemux86-64 ?= "c1750e55eb751d4773ea4c3e924e1858feb15e4e"
SRCREV_machine_qemumips64 ?= "a240524ebe0051faf7ada3cdb3241dda6d8d3ba3"
SRCREV_machine ?= "c1750e55eb751d4773ea4c3e924e1858feb15e4e"
SRCREV_meta ?= "54d07ec2566afe2a4c1eee4995781aab5599b5d5"

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
