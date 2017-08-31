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

SRCREV_machine_qemuarm ?= "fd1626821467303bd084dc28d41f9093d60c3f31"
SRCREV_machine_qemuarm64 ?= "b5c8cfda2dfe296410d51e131289fb09c69e1e7d"
SRCREV_machine_qemumips ?= "0d351d8eb3feeb121e859cb77c16dd0f46007a15"
SRCREV_machine_qemuppc ?= "b5c8cfda2dfe296410d51e131289fb09c69e1e7d"
SRCREV_machine_qemux86 ?= "b5c8cfda2dfe296410d51e131289fb09c69e1e7d"
SRCREV_machine_qemux86-64 ?= "b5c8cfda2dfe296410d51e131289fb09c69e1e7d"
SRCREV_machine_qemumips64 ?= "ec95c3aebd1d653a409e844779d2a34666627f59"
SRCREV_machine ?= "b5c8cfda2dfe296410d51e131289fb09c69e1e7d"
SRCREV_meta ?= "42965d664ffcac13ff4dffddcd4959ab08f19508"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.10"

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
