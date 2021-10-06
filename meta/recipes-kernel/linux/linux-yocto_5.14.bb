KBRANCH ?= "v5.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.14/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.14/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.14/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.14/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.14/standard/base"
KBRANCH:qemuriscv32  ?= "v5.14/standard/base"
KBRANCH:qemux86  ?= "v5.14/standard/base"
KBRANCH:qemux86-64 ?= "v5.14/standard/base"
KBRANCH:qemumips64 ?= "v5.14/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "c3a887f62f231d6b31f0209712014f9cbc7fd77e"
SRCREV_machine:qemuarm64 ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemumips ?= "77174bdf6581bdb93f0f458601364800670f8531"
SRCREV_machine:qemuppc ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemuriscv64 ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemuriscv32 ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemux86 ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemux86-64 ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_machine:qemumips64 ?= "e86c3a6ad2fde78ad03e0b899286bf603756207d"
SRCREV_machine ?= "c4def465fc44a7f5311d9b942d6cdd33cb4804ca"
SRCREV_meta ?= "884dfea956ec6b166d1f99a295c47338573a974c"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE:class-devupstream = "-1"
SRCREV_machine:class-devupstream ?= "70248e7b378b96f208d5544ee25b808a8ef2ddc2"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.14/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.14;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.14.9"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
