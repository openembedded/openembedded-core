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

SRCREV_machine_qemuarm ?= "5400ff2d394e933487c7d3d16d1a295d850232e5"
SRCREV_machine_qemuarm64 ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_machine_qemumips ?= "9e9b14f6ef913ea00a01dc0766666f2c86f5bc63"
SRCREV_machine_qemuppc ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_machine_qemux86 ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_machine_qemux86-64 ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_machine_qemumips64 ?= "0175d25d470adf5604a59b020a6de86493b1a78e"
SRCREV_machine ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_meta ?= "125963a34eebf4ee3d2b62d97c8090d912c8eb03"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.15.18"

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
