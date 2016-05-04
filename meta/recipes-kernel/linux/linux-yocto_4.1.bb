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

SRCREV_machine_qemuarm ?= "f781d64949f776cbfa5274a548becdcf6b596341"
SRCREV_machine_qemuarm64 ?= "d03753ddb28a1141e550a67c99ac95789a424fc5"
SRCREV_machine_qemumips ?= "192043960081896baaa24c2af702c7124bfe4286"
SRCREV_machine_qemuppc ?= "d03753ddb28a1141e550a67c99ac95789a424fc5"
SRCREV_machine_qemux86 ?= "d03753ddb28a1141e550a67c99ac95789a424fc5"
SRCREV_machine_qemux86-64 ?= "d03753ddb28a1141e550a67c99ac95789a424fc5"
SRCREV_machine_qemumips64 ?= "351bc6968f63ea6f27cbd7f1678ddc53a9168fd1"
SRCREV_machine ?= "d03753ddb28a1141e550a67c99ac95789a424fc5"
SRCREV_meta ?= "2bdebd11f1a0bc00071ec1467289a7feb5418dde"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.22"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
