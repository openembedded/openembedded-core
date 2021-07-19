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

SRCREV_machine_qemuarm ?= "b378a7f6a2a3542bf8cf0dc21f21e183fc8aedbc"
SRCREV_machine_qemuarm64 ?= "eb735648264252f9865fae18c509c067e32ad528"
SRCREV_machine_qemumips ?= "9efee711588fc23832dcb32bb3ead012fd72ec34"
SRCREV_machine_qemuppc ?= "e8966ba4bbaa9e90b21d8576cdd3bab8fe161782"
SRCREV_machine_qemuriscv64 ?= "570e7aedcd00e7e8a218e37c3dfb3b1fda83d111"
SRCREV_machine_qemuriscv32 ?= "570e7aedcd00e7e8a218e37c3dfb3b1fda83d111"
SRCREV_machine_qemux86 ?= "570e7aedcd00e7e8a218e37c3dfb3b1fda83d111"
SRCREV_machine_qemux86-64 ?= "570e7aedcd00e7e8a218e37c3dfb3b1fda83d111"
SRCREV_machine_qemumips64 ?= "3f0308c87d201ae82c6561da52c57f8a23f8a3f0"
SRCREV_machine ?= "570e7aedcd00e7e8a218e37c3dfb3b1fda83d111"
SRCREV_meta ?= "4dcaa4bcd042d398e210b702449dd35fdf9f0078"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE_class-devupstream = "-1"
SRCREV_class-devupstream ?= "f86aa267e180b23d8d24ee6886bd3bf9a360112e"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13.3"

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
