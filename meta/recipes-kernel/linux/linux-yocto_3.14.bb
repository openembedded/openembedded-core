KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "c0ed33beda334dcd387dff89da9804c03e24d648"
SRCREV_machine_qemumips ?= "808071c5f72dc98ed2192de28bdd4e9ba2f7e820"
SRCREV_machine_qemuppc ?= "0f93713ce91cc93f055ae6bcf1bf728d4036d097"
SRCREV_machine_qemux86 ?= "f6aa7aaca80e8532316bfb6e8dd2fbf9ab4f8b8e"
SRCREV_machine_qemux86-64 ?= "21ba402e0a5286baf0b8960da132e2d56f020c88"
SRCREV_machine_qemumips64 ?= "f44eb151dc53822f4f94f50b2ec67a0a1aab041b"
SRCREV_machine ?= "21ba402e0a5286baf0b8960da132e2d56f020c88"
SRCREV_meta ?= "6eddbf47875ef48ddc5864957a7b63363100782b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.29"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
