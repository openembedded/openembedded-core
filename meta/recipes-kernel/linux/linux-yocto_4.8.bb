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

SRCREV_machine_qemuarm ?= "a0241e756033146b308e94e262f0c434d4d5dcdc"
SRCREV_machine_qemuarm64 ?= "72e32211033cff873824d944470d06f590f66a85"
SRCREV_machine_qemumips ?= "b17c74d6df96711a98c48515b6c171dfe492a4a6"
SRCREV_machine_qemuppc ?= "15c034ae478c08219aaefaf8a9b57ee2939715a9"
SRCREV_machine_qemux86 ?= "f5d46e21688b7463c942d9d27789d88d580f566b"
SRCREV_machine_qemux86-64 ?= "f5d46e21688b7463c942d9d27789d88d580f566b"
SRCREV_machine_qemumips64 ?= "7688c6f57bc68c6b39f0bd7a20ea84419ebfaf50"
SRCREV_machine ?= "f5d46e21688b7463c942d9d27789d88d580f566b"
SRCREV_meta ?= "1dc615a67779fdfd36548fd48e54bd19b6e6209e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.10"

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
