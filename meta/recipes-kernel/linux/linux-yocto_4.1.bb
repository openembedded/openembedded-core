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

SRCREV_machine_qemuarm ?= "c29bbcccfcecbf840e60ddf8c16eb5f34d991eb1"
SRCREV_machine_qemuarm64 ?= "73bd4dd4a46970e4a599d3d02d60e40ab37b4498"
SRCREV_machine_qemumips ?= "aba568f531b31b2f802d1c08f4821177a0354da7"
SRCREV_machine_qemuppc ?= "73bd4dd4a46970e4a599d3d02d60e40ab37b4498"
SRCREV_machine_qemux86 ?= "73bd4dd4a46970e4a599d3d02d60e40ab37b4498"
SRCREV_machine_qemux86-64 ?= "73bd4dd4a46970e4a599d3d02d60e40ab37b4498"
SRCREV_machine_qemumips64 ?= "a65235597cef00d9a77def57622ac2dc31bb850e"
SRCREV_machine ?= "73bd4dd4a46970e4a599d3d02d60e40ab37b4498"
SRCREV_meta ?= "cab4fec4b7ab0efae0f44c1ec1836c035a9b78fe"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.27"

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
