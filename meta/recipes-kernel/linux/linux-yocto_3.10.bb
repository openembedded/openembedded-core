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

SRCREV_machine_qemuarm ?= "db489eed7f5c86037358cc9d0fefa7b90dbbaa86"
SRCREV_machine_qemumips ?= "780aac11b3f20ed2f5df3f173b0d02b28a6eb96b"
SRCREV_machine_qemuppc ?= "48a9dec5fc4cc9c19d12866c6ec560a83df21d85"
SRCREV_machine_qemux86 ?= "aa677a2d02677ec92d59a8c36d001cf2f5cf3260"
SRCREV_machine_qemux86-64 ?= "aa677a2d02677ec92d59a8c36d001cf2f5cf3260"
SRCREV_machine_qemumips64 ?= "d4b0d6f727167d7946e03dbfb50fef0891207bd1"
SRCREV_machine ?= "aa677a2d02677ec92d59a8c36d001cf2f5cf3260"
SRCREV_meta ?= "199943142f7e0a283240246ee6c02f4376b315f0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.43"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
