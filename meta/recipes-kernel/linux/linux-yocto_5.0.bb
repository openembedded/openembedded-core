KBRANCH ?= "v5.0/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.0/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.0/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.0/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.0/standard/qemuppc"
KBRANCH_qemux86  ?= "v5.0/standard/base"
KBRANCH_qemux86-64 ?= "v5.0/standard/base"
KBRANCH_qemumips64 ?= "v5.0/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "0cf286c38da5959520f6c01e606c722deb069fd1"
SRCREV_machine_qemuarm64 ?= "fa6f061d0da0c15c8685e6fad5b8ccc96a2e212c"
SRCREV_machine_qemumips ?= "577fd817ec6c9566277b341ac5911ea657ec5ba4"
SRCREV_machine_qemuppc ?= "fa6f061d0da0c15c8685e6fad5b8ccc96a2e212c"
SRCREV_machine_qemux86 ?= "fa6f061d0da0c15c8685e6fad5b8ccc96a2e212c"
SRCREV_machine_qemux86-64 ?= "fa6f061d0da0c15c8685e6fad5b8ccc96a2e212c"
SRCREV_machine_qemumips64 ?= "f9e63bc3f0ee65f03b3f9963a938668e6abf9425"
SRCREV_machine ?= "fa6f061d0da0c15c8685e6fad5b8ccc96a2e212c"
SRCREV_meta ?= "2bc19ce252b1ce753056b32c55f55f515698b42a"

# remap qemuarm to qemuarma15 for the 5.0 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.0;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.0.17"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"
KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
