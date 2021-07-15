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

SRCREV_machine_qemuarm ?= "5eaf18f1e3fb22c99d8a1cf7cd4148f10fa01d3c"
SRCREV_machine_qemuarm64 ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemumips ?= "8f960c63f6bd32557af074932f53f56bd9af423d"
SRCREV_machine_qemuppc ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemuriscv64 ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemuriscv32 ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemux86 ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemux86-64 ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_machine_qemumips64 ?= "c4651adf3e0a55fed797f0963a94aa02974dc591"
SRCREV_machine ?= "fbed80d1efbf9d19b06aa48cdee84342c0dda1c9"
SRCREV_meta ?= "ff973ffbf57891daa4060bfb7ef8f53192950b22"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE_class-devupstream = "-1"
SRCREV__class-devupstream ?= "d6fc894baac777c38a95a31f65343bea8b1a2678"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13.2"

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
