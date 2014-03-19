require recipes-kernel/linux/linux-yocto.inc

KBRANCH ?= "standard/base"

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "92de8060e113e4a13f16c6b4b5333f538e291c6d"
SRCREV_machine_qemumips ?= "313647599dd3cd7f42374d918a9397f82eed36fb"
SRCREV_machine_qemuppc ?= "45ef97bc22532a5a2d2fb94df3eec0aca27d0335"
SRCREV_machine_qemux86 ?= "edec15a07d310c4368b8149496da0548b564a413"
SRCREV_machine_qemux86-64 ?= "d2f8bdb8818bf2b83ac75b6b5e8428be61242d19"
SRCREV_machine_qemumips64 ?= "474fc6753d80afbd4483ea5531b03bc5b9c1ac18"
SRCREV_machine ?= "d2f8bdb8818bf2b83ac75b6b5e8428be61242d19"
SRCREV_meta ?= "3c987080bc943b8eae37fb6672368bb74978e484"

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
