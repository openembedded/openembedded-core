KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "3144baf8ff07651b13bc2043266eac713aafb965"
SRCREV_machine_qemuarm64 ?= "47cfdc0923bd6af61cea53e7bd6d9a23761efd32"
SRCREV_machine_qemumips ?= "bbb7ba114946c2aa95d86ccbf543dede7f147470"
SRCREV_machine_qemuppc ?= "47cfdc0923bd6af61cea53e7bd6d9a23761efd32"
SRCREV_machine_qemux86 ?= "47cfdc0923bd6af61cea53e7bd6d9a23761efd32"
SRCREV_machine_qemux86-64 ?= "47cfdc0923bd6af61cea53e7bd6d9a23761efd32"
SRCREV_machine_qemumips64 ?= "74e50a115b86a392c610e73193b49dd029391cb4"
SRCREV_machine ?= "47cfdc0923bd6af61cea53e7bd6d9a23761efd32"
SRCREV_meta ?= "245d701df6c3691a078a268eff54009959beb842"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.18"

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
