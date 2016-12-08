KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "9e3e856bc06ddaacc5f4075603630fcca76810f7"
SRCREV_machine_qemuarm64 ?= "008b4cdf62307da4e07082fd8782d70ba07500cb"
SRCREV_machine_qemumips ?= "c86ac5b3228b43304d8b2f1d49a0e4bd66207342"
SRCREV_machine_qemuppc ?= "5a245217d5271d1961df408b9a723a4eb188e0bf"
SRCREV_machine_qemux86 ?= "45f25516edd8636f1fec250f6e3020408cd30be9"
SRCREV_machine_qemux86-64 ?= "45f25516edd8636f1fec250f6e3020408cd30be9"
SRCREV_machine_qemumips64 ?= "7bfa2a19b1aa9f1389f8276d6a7669472517b18a"
SRCREV_machine ?= "45f25516edd8636f1fec250f6e3020408cd30be9"
SRCREV_meta ?= "b22e47739683444916dc64548df1dbf6856500a5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.12"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
