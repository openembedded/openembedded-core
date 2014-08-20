require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ac43643f8f35e4f5f068a9bb8c74c88a6ed7d447"
SRCREV_machine_qemumips ?= "a8b3d0215f52c1e7e1c241323ed393e4f4c37162"
SRCREV_machine_qemuppc ?= "9f92eada918f43ff6c06532aaba69718c20bb189"
SRCREV_machine_qemux86 ?= "a89b66bba2c9c77d21b8ba519981df2137a8f6e3"
SRCREV_machine_qemux86-64 ?= "abf013a41ba30ca5a3dd76e96cfde36bf7f3c5fa"
SRCREV_machine_qemumips64 ?= "862e58b06d5fdc67f7b2c636d6b5b179def5c10e"
SRCREV_machine ?= "abf013a41ba30ca5a3dd76e96cfde36bf7f3c5fa"
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
