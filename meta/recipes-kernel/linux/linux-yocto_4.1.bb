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

SRCREV_machine_qemuarm ?= "2b83b94ab36113da6d63e525e0686d1e9fefbeca"
SRCREV_machine_qemuarm64 ?= "b5f6aa9957ddc28eafede6678b4c9ff49240bc19"
SRCREV_machine_qemumips ?= "a5c91d6ff822f3920fd9a20b0deefa6e23acef32"
SRCREV_machine_qemuppc ?= "b5f6aa9957ddc28eafede6678b4c9ff49240bc19"
SRCREV_machine_qemux86 ?= "b5f6aa9957ddc28eafede6678b4c9ff49240bc19"
SRCREV_machine_qemux86-64 ?= "b5f6aa9957ddc28eafede6678b4c9ff49240bc19"
SRCREV_machine_qemumips64 ?= "2299caefd7d36e0fe868316045ceb58b9ae369f9"
SRCREV_machine ?= "b5f6aa9957ddc28eafede6678b4c9ff49240bc19"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
