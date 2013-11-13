require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "5714b747cf0087bb964cbb962db8d3d2041f3177"
SRCREV_machine_qemumips  ?= "e87d2cb44bc5d10f3619871541849064bf0d79b1"
SRCREV_machine_qemuppc ?= "3e99f981fea427696f63af7fd8e99bf05039efee"
SRCREV_machine_qemux86 ?= "c03195ed6e3066494e3fb4be69154a57066e845b"
SRCREV_machine_qemux86-64 ?= "c03195ed6e3066494e3fb4be69154a57066e845b"
SRCREV_machine_qemumips64 ?= "8d21f71847640fc052bda1bf1f3792634cae5bb1"
SRCREV_machine ?= "c03195ed6e3066494e3fb4be69154a57066e845b"
SRCREV_meta ?= "6ad20f049abd52b515a8e0a4664861cfd331f684"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
