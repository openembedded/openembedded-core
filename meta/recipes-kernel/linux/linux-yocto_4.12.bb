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

SRCREV_machine_qemuarm ?= "63eabe4fc5b8645a2a1258ee9b2ec0e5c62b4236"
SRCREV_machine_qemuarm64 ?= "1348b764f8ca8803b6b833c69f5cb795fddf24b8"
SRCREV_machine_qemumips ?= "64ac80375bc1c2e87002994c3112f30aa614d73e"
SRCREV_machine_qemuppc ?= "1348b764f8ca8803b6b833c69f5cb795fddf24b8"
SRCREV_machine_qemux86 ?= "1348b764f8ca8803b6b833c69f5cb795fddf24b8"
SRCREV_machine_qemux86-64 ?= "1348b764f8ca8803b6b833c69f5cb795fddf24b8"
SRCREV_machine_qemumips64 ?= "d481a271022e211857045b48d595826d8df051c4"
SRCREV_machine ?= "1348b764f8ca8803b6b833c69f5cb795fddf24b8"
SRCREV_meta ?= "daacede608e07d391229ba0a6d74d6e082bead5f"

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
