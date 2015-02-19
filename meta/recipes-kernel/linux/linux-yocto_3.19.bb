KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "91b64df8cdcdacb9ef8d707a0c34f55a09bf170d"
SRCREV_machine_qemuarm64 ?= "43b9eced9ba8a57add36af07736344dcc383f711"
SRCREV_machine_qemumips ?= "7c67469a4c77a977c46f218de7e2f4699292d28b"
SRCREV_machine_qemuppc ?= "7dc29d4265b44ad28ea3ec950b1a86be08933ce8"
SRCREV_machine_qemux86 ?= "43b9eced9ba8a57add36af07736344dcc383f711"
SRCREV_machine_qemux86-64 ?= "43b9eced9ba8a57add36af07736344dcc383f711"
SRCREV_machine_qemumips64 ?= "b7ab9d4bac55415f125c81f529dbbefb07de98ad"
SRCREV_machine ?= "43b9eced9ba8a57add36af07736344dcc383f711"
SRCREV_meta ?= "8897ef68b30e7426bc1d39895e71fb155d694974"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
