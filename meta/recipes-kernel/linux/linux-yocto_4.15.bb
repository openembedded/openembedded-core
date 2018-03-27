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

SRCREV_machine_qemuarm ?= "8b07f34007acee0fc978cec49e2fcf63026ebd43"
SRCREV_machine_qemuarm64 ?= "12f3957167b0001adc7c2c8b3d45127aafc37e6b"
SRCREV_machine_qemumips ?= "d4b3984ac600d413a017e9890efbfc55948d6d0a"
SRCREV_machine_qemuppc ?= "12f3957167b0001adc7c2c8b3d45127aafc37e6b"
SRCREV_machine_qemux86 ?= "12f3957167b0001adc7c2c8b3d45127aafc37e6b"
SRCREV_machine_qemux86-64 ?= "12f3957167b0001adc7c2c8b3d45127aafc37e6b"
SRCREV_machine_qemumips64 ?= "77ae4b3b45eab133a5daf1e90828e5dfe62062e1"
SRCREV_machine ?= "12f3957167b0001adc7c2c8b3d45127aafc37e6b"
SRCREV_meta ?= "e09bcd01af6a000647f7d6ec9b864c6ff5ffac89"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.15.7"

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
