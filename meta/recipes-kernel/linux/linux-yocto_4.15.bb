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

SRCREV_machine_qemuarm ?= "c9e3d532f245dcb00a022b7fc468bdefdd9b3ec8"
SRCREV_machine_qemuarm64 ?= "3247d5a26cab529bd4acc137c119bcdf71c3d5ae"
SRCREV_machine_qemumips ?= "88e64d2a3f8f12f3fdc759921ec2067f1e730f97"
SRCREV_machine_qemuppc ?= "3247d5a26cab529bd4acc137c119bcdf71c3d5ae"
SRCREV_machine_qemux86 ?= "3247d5a26cab529bd4acc137c119bcdf71c3d5ae"
SRCREV_machine_qemux86-64 ?= "3247d5a26cab529bd4acc137c119bcdf71c3d5ae"
SRCREV_machine_qemumips64 ?= "38face5dfd94733484822f392f9469fed18cca7e"
SRCREV_machine ?= "3247d5a26cab529bd4acc137c119bcdf71c3d5ae"
SRCREV_meta ?= "03267fd15a97748105bfec2866820a8c1512fe70"

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
