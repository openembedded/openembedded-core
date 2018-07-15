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

SRCREV_machine_qemuarm ?= "cd831469d8eb4d900fe65985921d2003c59f3a86"
SRCREV_machine_qemuarm64 ?= "1b742cf55fc29d0ffc9d651520ad7d59145bbc07"
SRCREV_machine_qemumips ?= "24b020741e8762ec8aeb5be95f842332083b2028"
SRCREV_machine_qemuppc ?= "1b742cf55fc29d0ffc9d651520ad7d59145bbc07"
SRCREV_machine_qemux86 ?= "1b742cf55fc29d0ffc9d651520ad7d59145bbc07"
SRCREV_machine_qemux86-64 ?= "1b742cf55fc29d0ffc9d651520ad7d59145bbc07"
SRCREV_machine_qemumips64 ?= "100a1682529e34d118d5552c9db773635cd2c621"
SRCREV_machine ?= "1b742cf55fc29d0ffc9d651520ad7d59145bbc07"
SRCREV_meta ?= "5e993963afb54bdc82a02077c29ecdbc0b12368e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.113"

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
