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

SRCREV_machine_qemuarm ?= "de3a57fd015b3eca12d65530efc6a2cb59226d25"
SRCREV_machine_qemuarm64 ?= "e02ac252f68dca39c8fa06c8517dd805e75aed87"
SRCREV_machine_qemumips ?= "d00db934de0fa0ddb5811bb91d62c1c26e2bed95"
SRCREV_machine_qemuppc ?= "e02ac252f68dca39c8fa06c8517dd805e75aed87"
SRCREV_machine_qemux86 ?= "e02ac252f68dca39c8fa06c8517dd805e75aed87"
SRCREV_machine_qemux86-64 ?= "e02ac252f68dca39c8fa06c8517dd805e75aed87"
SRCREV_machine_qemumips64 ?= "aa673476922c02c6caef5c1d65e59f03259a596d"
SRCREV_machine ?= "e02ac252f68dca39c8fa06c8517dd805e75aed87"
SRCREV_meta ?= "79dbb64d9e179718369a7a5c7b364fda9936571f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.17"

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
