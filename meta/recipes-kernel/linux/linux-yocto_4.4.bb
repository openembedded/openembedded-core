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

SRCREV_machine_qemuarm ?= "7f8b040efb7b442799a0cdd5d9b1bc31d852494b"
SRCREV_machine_qemuarm64 ?= "242f3e2d6ec292c84813b0e8a577cc24a55335e7"
SRCREV_machine_qemumips ?= "3aa28eea959c0b1a408efc683c94941003aecfe8"
SRCREV_machine_qemuppc ?= "242f3e2d6ec292c84813b0e8a577cc24a55335e7"
SRCREV_machine_qemux86 ?= "242f3e2d6ec292c84813b0e8a577cc24a55335e7"
SRCREV_machine_qemux86-64 ?= "242f3e2d6ec292c84813b0e8a577cc24a55335e7"
SRCREV_machine_qemumips64 ?= "816cc8e84ef0e1f2669ecc432bdd0362135bfe2b"
SRCREV_machine ?= "242f3e2d6ec292c84813b0e8a577cc24a55335e7"
SRCREV_meta ?= "812e62b35ac2b88bbee80b7c8436d9df7065a90e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.9"

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
