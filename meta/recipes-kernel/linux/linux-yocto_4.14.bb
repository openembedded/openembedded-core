KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "bd85f4880bb890bf9c45ee6c2fd95f077d2bf67e"
SRCREV_machine_qemuarm64 ?= "445a4787bd489eb6b3d5c172b9842dbe5a34d734"
SRCREV_machine_qemumips ?= "3d07ac9aa6ca729674dfb763563202f18f9eedde"
SRCREV_machine_qemuppc ?= "81ba8dbab3b1bfc371e539956be905809db0e41a"
SRCREV_machine_qemux86 ?= "bc9d4b045fa0254d14ef3a667a200f02cb9af755"
SRCREV_machine_qemux86-64 ?= "bc9d4b045fa0254d14ef3a667a200f02cb9af755"
SRCREV_machine_qemumips64 ?= "3c4acadcbe2ee11043f7d0fce43a5181511d0935"
SRCREV_machine ?= "bc9d4b045fa0254d14ef3a667a200f02cb9af755"
SRCREV_meta ?= "1bd749b7ce4240e83024b10fa4a4a6b9de5a5e5f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.143"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
