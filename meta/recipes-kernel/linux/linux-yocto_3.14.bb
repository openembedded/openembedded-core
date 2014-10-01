KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "0ed31872de2c0e62e9843fa714cb0f8e5f53b70c"
SRCREV_machine_qemumips ?= "c3537ecd101c8494a1da5534ad934f25d6fa256e"
SRCREV_machine_qemuppc ?= "df6daa113d535fd46668ba3bb0970c959f103f76"
SRCREV_machine_qemux86 ?= "1d678e0d4ecd53b973a70fb087dc1275b4de4e0c"
SRCREV_machine_qemux86-64 ?= "0ca8f6b51728ca26129b4260be388ef2b7f366a8"
SRCREV_machine_qemumips64 ?= "6aa17794bc6795847dae3b60f9d9be01dac874ec"
SRCREV_machine ?= "0ca8f6b51728ca26129b4260be388ef2b7f366a8"
SRCREV_meta ?= "fb6271a942b57bdc40c6e49f0203be153699f81c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.19"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
