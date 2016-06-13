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

SRCREV_machine_qemuarm ?= "a6899b7c294ed25d9d37afde858cdd52f41f86b0"
SRCREV_machine_qemuarm64 ?= "9ba8c36e9ea7419d06accab5311e7fb0d56513ff"
SRCREV_machine_qemumips ?= "69936a9614608b7aa893ae78bd69e5cc197816c3"
SRCREV_machine_qemuppc ?= "9ba8c36e9ea7419d06accab5311e7fb0d56513ff"
SRCREV_machine_qemux86 ?= "9ba8c36e9ea7419d06accab5311e7fb0d56513ff"
SRCREV_machine_qemux86-64 ?= "9ba8c36e9ea7419d06accab5311e7fb0d56513ff"
SRCREV_machine_qemumips64 ?= "f0eb9eabbeafa5eadf66893822ea42d80e04afbd"
SRCREV_machine ?= "9ba8c36e9ea7419d06accab5311e7fb0d56513ff"
SRCREV_meta ?= "9f68667031354532563766a3d04ca8a618e9177a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.26"

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
