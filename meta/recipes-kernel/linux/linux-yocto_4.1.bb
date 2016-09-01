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

SRCREV_machine_qemuarm ?= "9c5e96205316d167379d101d808a6cda93adc335"
SRCREV_machine_qemuarm64 ?= "0847ed67f89b5a8bbaaa0a7b6cfa2a99ef34834f"
SRCREV_machine_qemumips ?= "9e531cb59dd7dfa2026637cc456a7bdcbdb50275"
SRCREV_machine_qemuppc ?= "32968ddd9357f72dfd427f5b3eda62c4a49833c7"
SRCREV_machine_qemux86 ?= "0847ed67f89b5a8bbaaa0a7b6cfa2a99ef34834f"
SRCREV_machine_qemux86-64 ?= "0847ed67f89b5a8bbaaa0a7b6cfa2a99ef34834f"
SRCREV_machine_qemumips64 ?= "106ed721d21c595dfb993a8990c3a29830dfc166"
SRCREV_machine ?= "0847ed67f89b5a8bbaaa0a7b6cfa2a99ef34834f"
SRCREV_meta ?= "9d0f0db8442a5ccb3f2ce81e2e43d96b6515a45a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.30"

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
