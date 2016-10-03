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

SRCREV_machine_qemuarm ?= "fc4881574f295bf2480f95d7c3e279ab32b44d8a"
SRCREV_machine_qemuarm64 ?= "707621a083512f037f749a711df3f4f1fb3ef842"
SRCREV_machine_qemumips ?= "013be1e230a0173677dfbfc081aea63fd7c58ec8"
SRCREV_machine_qemuppc ?= "707621a083512f037f749a711df3f4f1fb3ef842"
SRCREV_machine_qemux86 ?= "707621a083512f037f749a711df3f4f1fb3ef842"
SRCREV_machine_qemux86-64 ?= "707621a083512f037f749a711df3f4f1fb3ef842"
SRCREV_machine_qemumips64 ?= "7496d66804993fcfc569dd062f7e63a7490bc62f"
SRCREV_machine ?= "707621a083512f037f749a711df3f4f1fb3ef842"
SRCREV_meta ?= "6128a9e47cd1aeb46b604469c17bff3eba8d5f93"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8"

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
