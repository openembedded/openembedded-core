require recipes-kernel/linux/linux-yocto.inc

KBRANCH ?= "standard/base"

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "65c9b3a3996f541eb26e5918de0ce02ec81a36e2"
SRCREV_machine_qemumips ?= "e6a4a3bd2e332e2d1b01b4008497775d1ccfea19"
SRCREV_machine_qemuppc ?= "05e7be95eb9d48590cf7e8dea2538b927a8ba1c6"
SRCREV_machine_qemux86 ?= "3270ea3cf75d58316913b42ea51ad85068a595de"
SRCREV_machine_qemux86-64 ?= "42477caf6bfda5ca7439121d348e474cff44ab5c"
SRCREV_machine_qemumips64 ?= "f2317d326cce17a7a595e0c346e0f15b1a1df981"
SRCREV_machine ?= "42477caf6bfda5ca7439121d348e474cff44ab5c"
SRCREV_meta ?= "ccad961c4cb6be245ed198bd2c17c27ab33cfcd7"

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
