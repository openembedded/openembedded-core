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

SRCREV_machine_qemuarm ?= "1e1376104bff5a42b4cb02d5086f98f69cb5b835"
SRCREV_machine_qemuarm64 ?= "abc6bf6010cae7127b5849ce83b60f244024408f"
SRCREV_machine_qemumips ?= "0b4d515ecab39edc197039450e8386525cdb616e"
SRCREV_machine_qemuppc ?= "abc6bf6010cae7127b5849ce83b60f244024408f"
SRCREV_machine_qemux86 ?= "abc6bf6010cae7127b5849ce83b60f244024408f"
SRCREV_machine_qemux86-64 ?= "abc6bf6010cae7127b5849ce83b60f244024408f"
SRCREV_machine_qemumips64 ?= "8e7c9bd1fe585e22952b6772cb61f2aed6a09e83"
SRCREV_machine ?= "abc6bf6010cae7127b5849ce83b60f244024408f"
SRCREV_meta ?= "73365069d89a8423a6f9f4b5560ee6bfdfc0b3d1"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.12"

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
