KBRANCH ?= "v4.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.15/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.15/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.15/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.15/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.15/standard/base"
KBRANCH_qemux86-64 ?= "v4.15/standard/base"
KBRANCH_qemumips64 ?= "v4.15/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "5fdc3d579c5f50fbaaa5faecae79e20b73810346"
SRCREV_machine_qemuarm64 ?= "9c2e6c0fc71526c45fc7ddf3ec91e2e2f27e3da0"
SRCREV_machine_qemumips ?= "5afbe5a7e271f1c115a7c77bea4a71356c6f2792"
SRCREV_machine_qemuppc ?= "9c2e6c0fc71526c45fc7ddf3ec91e2e2f27e3da0"
SRCREV_machine_qemux86 ?= "9c2e6c0fc71526c45fc7ddf3ec91e2e2f27e3da0"
SRCREV_machine_qemux86-64 ?= "9c2e6c0fc71526c45fc7ddf3ec91e2e2f27e3da0"
SRCREV_machine_qemumips64 ?= "f9a3a72209bde080e4ecb4fbe7a0f99954643131"
SRCREV_machine ?= "9c2e6c0fc71526c45fc7ddf3ec91e2e2f27e3da0"
SRCREV_meta ?= "64ad69e5b4a38e678500d7a70791ac5154c6c154"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.15"

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
