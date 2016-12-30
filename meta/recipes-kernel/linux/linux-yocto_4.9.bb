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

SRCREV_machine_qemuarm ?= "0fda844a4c8da3d987b8173128c2b14948133b2e"
SRCREV_machine_qemuarm64 ?= "617635e43afee1aaf98a8f8300596f7a112513c0"
SRCREV_machine_qemumips ?= "22f2f6de114cf1fc58e068d1aff220d633aeaddf"
SRCREV_machine_qemuppc ?= "617635e43afee1aaf98a8f8300596f7a112513c0"
SRCREV_machine_qemux86 ?= "617635e43afee1aaf98a8f8300596f7a112513c0"
SRCREV_machine_qemux86-64 ?= "617635e43afee1aaf98a8f8300596f7a112513c0"
SRCREV_machine_qemumips64 ?= "3183d61d4d00d8b7faf10d11b3da8cf3db432e6f"
SRCREV_machine ?= "617635e43afee1aaf98a8f8300596f7a112513c0"
SRCREV_meta ?= "0cf14593694d33518aabd4846d50adeeb7f559ed"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9"

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
