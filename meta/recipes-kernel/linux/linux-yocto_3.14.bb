KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "1c7a46a46b2cbf68777713531647cd4b1cf22f12"
SRCREV_machine_qemumips ?= "f3dea87bc7aaccae5c24240b378a4e09034d80af"
SRCREV_machine_qemuppc ?= "195f2029d49fc2440cf236d5134eb2ab88b8d407"
SRCREV_machine_qemux86 ?= "bdef75805ede16c13f4383cc53d66dad3e83da64"
SRCREV_machine_qemux86-64 ?= "14fea2388e7b248c4e9442ac95fcbf1e4d265ee2"
SRCREV_machine_qemumips64 ?= "b5c533e6ffba299b24d31cc2b8f32f6302f5d7e3"
SRCREV_machine ?= "14fea2388e7b248c4e9442ac95fcbf1e4d265ee2"
SRCREV_meta ?= "0bd3ec19b64191efc9d0ba83d05c9f7f9bf4473a"

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
