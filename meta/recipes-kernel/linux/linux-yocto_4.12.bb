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

SRCREV_machine_qemuarm ?= "b6b85e6055b8af3ef9a8e74f4edb5c0a69bd031b"
SRCREV_machine_qemuarm64 ?= "0b17e1b52f9953a5672890e478de94ac8c44775f"
SRCREV_machine_qemumips ?= "788655446537fbf4a7c7cb720b4ea3dfa0ca9d20"
SRCREV_machine_qemuppc ?= "0b17e1b52f9953a5672890e478de94ac8c44775f"
SRCREV_machine_qemux86 ?= "0b17e1b52f9953a5672890e478de94ac8c44775f"
SRCREV_machine_qemux86-64 ?= "0b17e1b52f9953a5672890e478de94ac8c44775f"
SRCREV_machine_qemumips64 ?= "d3f5808a2f7787e02057df9fea1fa97fb939fd6c"
SRCREV_machine ?= "0b17e1b52f9953a5672890e478de94ac8c44775f"
SRCREV_meta ?= "b9735e238841b9932bb324e8b4d9fb536d812ed1"

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
