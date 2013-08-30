require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "24d53cd6ca4c3ae8f5dd2ff840f9275327d955ee"
SRCREV_machine_qemumips  ?= "0480771238bb87031b79ecb1ee6bce7bfe6ffb1c"
SRCREV_machine_qemuppc ?= "a9c383957f69e70fd2cb27d885160303a305cf57"
SRCREV_machine_qemux86 ?= "7144bcc4b8091675bfcf1941479067857b6242da"
SRCREV_machine_qemux86-64 ?= "7144bcc4b8091675bfcf1941479067857b6242da"
SRCREV_machine_qemumips64 ?= "10a8db0603d7186633a6bf90e2308710763e718e"
SRCREV_machine ?= "7144bcc4b8091675bfcf1941479067857b6242da"
SRCREV_meta ?= "cd502a88148ab214b54860f97a96f41858fd6446"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.9"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
