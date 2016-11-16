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

SRCREV_machine_qemuarm ?= "2041d76e0048b345098fbc680f5719da5d7dae7b"
SRCREV_machine_qemuarm64 ?= "4c2d50b725d6dd5e90d1151abdbcb7418938be3a"
SRCREV_machine_qemumips ?= "0cd523cd89c13cb3674b55a6afe41aa976950fc1"
SRCREV_machine_qemuppc ?= "4c2d50b725d6dd5e90d1151abdbcb7418938be3a"
SRCREV_machine_qemux86 ?= "4c2d50b725d6dd5e90d1151abdbcb7418938be3a"
SRCREV_machine_qemux86-64 ?= "4c2d50b725d6dd5e90d1151abdbcb7418938be3a"
SRCREV_machine_qemumips64 ?= "baea2f5605baba0d49a9afac8a46717193ccef90"
SRCREV_machine ?= "4c2d50b725d6dd5e90d1151abdbcb7418938be3a"
SRCREV_meta ?= "24ea5324fc90c7cb15ce1a08cdd294f22c6e6382"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.32"

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
