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

SRCREV_machine_qemuarm ?= "aea95f103a4f5fccc342bb763af9cfba0623fccc"
SRCREV_machine_qemuarm64 ?= "60332e45b2685ed78ed61f3c27ce931e12b1dfdf"
SRCREV_machine_qemumips ?= "df85b27d70cb259662a00f5ad860eb3deba63b7f"
SRCREV_machine_qemuppc ?= "60332e45b2685ed78ed61f3c27ce931e12b1dfdf"
SRCREV_machine_qemux86 ?= "60332e45b2685ed78ed61f3c27ce931e12b1dfdf"
SRCREV_machine_qemux86-64 ?= "60332e45b2685ed78ed61f3c27ce931e12b1dfdf"
SRCREV_machine_qemumips64 ?= "d371bd9361507ef0b03544f2190a63211fe31c87"
SRCREV_machine ?= "60332e45b2685ed78ed61f3c27ce931e12b1dfdf"
SRCREV_meta ?= "a4f88c3fad887e1c559d03ae1b531ca267137b69"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.22"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
