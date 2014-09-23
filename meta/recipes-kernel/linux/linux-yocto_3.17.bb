KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "1d23a0b00164e5ba372988456090d95312f58083"
SRCREV_machine_qemumips ?= "2f0e803b12728d7af5ab3967c50ef3342d946c65"
SRCREV_machine_qemuppc ?= "ad01ce94ff8ebaedfa470ee16890255cf6887cc4"
SRCREV_machine_qemux86 ?= "6cd8a70a8c51c4167f96695fb74356de6bab1c50"
SRCREV_machine_qemux86-64 ?= "6cd8a70a8c51c4167f96695fb74356de6bab1c50"
SRCREV_machine_qemumips64 ?= "823eecd78233f65582724ea1d9c8c6002518ecb0"
SRCREV_machine ?= "6cd8a70a8c51c4167f96695fb74356de6bab1c50"
SRCREV_meta ?= "eebdfd73e01f53a042d8b03b785db9f998d69b19"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc6"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
