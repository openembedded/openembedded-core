KBRANCH ?= "v5.13/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.13/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.13/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.13/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.13/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.13/standard/base"
KBRANCH_qemuriscv32  ?= "v5.13/standard/base"
KBRANCH_qemux86  ?= "v5.13/standard/base"
KBRANCH_qemux86-64 ?= "v5.13/standard/base"
KBRANCH_qemumips64 ?= "v5.13/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "60de9ae7ec5086c7aab26fef79c5ee22673e984b"
SRCREV_machine_qemuarm64 ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemumips ?= "4ba021f55cd0f90d34d2432de21282e792e6ad06"
SRCREV_machine_qemuppc ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemuriscv64 ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemuriscv32 ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemux86 ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemux86-64 ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_machine_qemumips64 ?= "1fb4f7d7204bffe99a86e72b3cffe84c770be3ee"
SRCREV_machine ?= "4fd6245b88c0700dfb7ffd951d5a7cd460935330"
SRCREV_meta ?= "f80d46c2e088f4a33430442058aa881b1a31b95c"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE_class-devupstream = "-1"
SRCREV_class-devupstream = "62fb9874f5da54fdb243003b386128037319b219"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
