require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ed834b297cd5d36b303d36119549b90789e2890e"
SRCREV_machine_qemumips ?= "12eba41a9a3bb017dcb45e721f20d7e68903b1c3"
SRCREV_machine_qemuppc ?= "e2dbfaf796b18b0b9918f194e2a4c9e9eded0c2c"
SRCREV_machine_qemux86 ?= "e8eb08d85050a944582e974cd461f741191bd07c"
SRCREV_machine_qemux86-64 ?= "5bee7e1583d4f075ac5a96d121271b347b384fd7"
SRCREV_machine_qemumips64 ?= "4ecb96fcb1826a127d6afbf67b8e69cccd7ccc8e"
SRCREV_machine ?= "5bee7e1583d4f075ac5a96d121271b347b384fd7"
SRCREV_meta ?= "b2af4e3528e65583c98f3a08c6edb0cad7a120b0"

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
