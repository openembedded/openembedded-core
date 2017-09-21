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

SRCREV_machine_qemuarm ?= "6d64d98ab2bfa4dabfb6028f99cdc02ae248d746"
SRCREV_machine_qemuarm64 ?= "744d778aeb472124f51371c55073788863d6ba2b"
SRCREV_machine_qemumips ?= "d0f1efc4940a0a35d4b54cbc5933f39f0213dabb"
SRCREV_machine_qemuppc ?= "2acced5ac2d1769e45f417f8905c10a385679b0e"
SRCREV_machine_qemux86 ?= "72d0c1a637e260c25a4299c230910dd1210cd0cf"
SRCREV_machine_qemux86-64 ?= "72d0c1a637e260c25a4299c230910dd1210cd0cf"
SRCREV_machine_qemumips64 ?= "4202750d69c880a21b3e38e58b270486ad7d2786"
SRCREV_machine ?= "72d0c1a637e260c25a4299c230910dd1210cd0cf"
SRCREV_meta ?= "76db2c6ca67b9b3597257684a027344d11b1bc81"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.25"

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
