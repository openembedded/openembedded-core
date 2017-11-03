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

SRCREV_machine_qemuarm ?= "afd1340d4b7c2180ee0e5dbf79499fedf1a023a6"
SRCREV_machine_qemuarm64 ?= "26c1863a744836d9171dff262b864cb67f4d537c"
SRCREV_machine_qemumips ?= "6d0052275c433f5a66282ad9f53525a7b6d36dfd"
SRCREV_machine_qemuppc ?= "26c1863a744836d9171dff262b864cb67f4d537c"
SRCREV_machine_qemux86 ?= "26c1863a744836d9171dff262b864cb67f4d537c"
SRCREV_machine_qemux86-64 ?= "26c1863a744836d9171dff262b864cb67f4d537c"
SRCREV_machine_qemumips64 ?= "c8adcd782e648cb70780443ee90e2f56e2209c5a"
SRCREV_machine ?= "26c1863a744836d9171dff262b864cb67f4d537c"
SRCREV_meta ?= "cebe198870d781829bd997a188cc34d9f7a61023"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.14"

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
