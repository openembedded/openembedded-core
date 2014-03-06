require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "6c34763a9e4e05e1fcaa32d915bd05cccd0abc1d"
SRCREV_machine_qemumips  ?= "fa9377eda8d2dceccb218f229551424c64ef0c28"
SRCREV_machine_qemuppc ?= "85868d48f06911b7dddaf0180af96c8f2e874922"
SRCREV_machine_qemux86 ?= "c29f5c8952c0f3ef27773d78e5cc64e437a357cb"
SRCREV_machine_qemux86-64 ?= "c29f5c8952c0f3ef27773d78e5cc64e437a357cb"
SRCREV_machine ?= "c29f5c8952c0f3ef27773d78e5cc64e437a357cb"
SRCREV_meta ?= "ef4cd500d4b64680f7a319d399b8a12f9ecc9fe6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.82"

PR = "${INC_PR}.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
