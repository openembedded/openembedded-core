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

SRCREV_machine_qemuarm ?= "435a6d77355ee83be321d9ee69d448eb530fb8e1"
SRCREV_machine_qemuarm64 ?= "10527a38c88d3e2c314c9e49d1eecb9da56c2d85"
SRCREV_machine_qemumips ?= "fb72c22307829d134fc6584fe4735377110a2fc2"
SRCREV_machine_qemuppc ?= "10527a38c88d3e2c314c9e49d1eecb9da56c2d85"
SRCREV_machine_qemux86 ?= "10527a38c88d3e2c314c9e49d1eecb9da56c2d85"
SRCREV_machine_qemux86-64 ?= "10527a38c88d3e2c314c9e49d1eecb9da56c2d85"
SRCREV_machine_qemumips64 ?= "2fd1a928c13bd7eac5e1086859355314e210b909"
SRCREV_machine ?= "10527a38c88d3e2c314c9e49d1eecb9da56c2d85"
SRCREV_meta ?= "28bff6d39803ad83f83e353f158fc1aa14d5492d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.3"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
