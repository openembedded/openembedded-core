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

SRCREV_machine_qemuarm ?= "29b1dac023fac237c408620aaa79a60a611dc2df"
SRCREV_machine_qemuarm64 ?= "e6e0dea72e605a070373bd52c6c2637202ce4b38"
SRCREV_machine_qemumips ?= "edb3f19ae13818cd5af56f21566e843c975ba897"
SRCREV_machine_qemuppc ?= "e6e0dea72e605a070373bd52c6c2637202ce4b38"
SRCREV_machine_qemux86 ?= "e6e0dea72e605a070373bd52c6c2637202ce4b38"
SRCREV_machine_qemux86-64 ?= "e6e0dea72e605a070373bd52c6c2637202ce4b38"
SRCREV_machine_qemumips64 ?= "c583570fc64c2e01e3cf51edd0e732ee6789c9ef"
SRCREV_machine ?= "e6e0dea72e605a070373bd52c6c2637202ce4b38"
SRCREV_meta ?= "25fb74eaaef249519f25e243e7f9bf0cab0e1781"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8-rc6"

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
