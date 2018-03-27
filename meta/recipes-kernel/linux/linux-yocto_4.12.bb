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

SRCREV_machine_qemuarm ?= "d2bdc11312fc02d4ad34e044cf1bb88da2b85591"
SRCREV_machine_qemuarm64 ?= "1a600c736163e7350ee4da9172f5321d06a72889"
SRCREV_machine_qemumips ?= "15e9d4817db80cee890207e849776beeda7759e5"
SRCREV_machine_qemuppc ?= "1a600c736163e7350ee4da9172f5321d06a72889"
SRCREV_machine_qemux86 ?= "1a600c736163e7350ee4da9172f5321d06a72889"
SRCREV_machine_qemux86-64 ?= "1a600c736163e7350ee4da9172f5321d06a72889"
SRCREV_machine_qemumips64 ?= "68bcd22421aa288aff3dc9cf3035539788b04353"
SRCREV_machine ?= "1a600c736163e7350ee4da9172f5321d06a72889"
SRCREV_meta ?= "2feb169ba66721f3794bacd97c8360ad594ce133"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

DEPENDS += "openssl-native util-linux-native"

LINUX_VERSION ?= "4.12.21"

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
