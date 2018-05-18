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

SRCREV_machine_qemuarm ?= "557352e573a3e23cf643d8b8b5e59bfaf301b4e4"
SRCREV_machine_qemuarm64 ?= "204694d106a8bd75d8b6333fac9443c5fe809bb5"
SRCREV_machine_qemumips ?= "a262c44eef3d3f0bbab797bbb5e2717b7ccb4d2f"
SRCREV_machine_qemuppc ?= "204694d106a8bd75d8b6333fac9443c5fe809bb5"
SRCREV_machine_qemux86 ?= "204694d106a8bd75d8b6333fac9443c5fe809bb5"
SRCREV_machine_qemux86-64 ?= "204694d106a8bd75d8b6333fac9443c5fe809bb5"
SRCREV_machine_qemumips64 ?= "f1e2c54153838e94f1730beba7c8e28af739af68"
SRCREV_machine ?= "204694d106a8bd75d8b6333fac9443c5fe809bb5"
SRCREV_meta ?= "d2a0a87040e7fef4ca1165e73483b119830732ed"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

DEPENDS += "openssl-native util-linux-native"

LINUX_VERSION ?= "4.12.23"

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
