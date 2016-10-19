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

SRCREV_machine_qemuarm ?= "de294849e02680399a6dd03fedcc03a69e9a6a04"
SRCREV_machine_qemuarm64 ?= "f4e52341c304e044dbe581a35aad6b930c9410d1"
SRCREV_machine_qemumips ?= "a41dd187e7d42be65780f25997eb890ead6cc7d9"
SRCREV_machine_qemuppc ?= "f4e52341c304e044dbe581a35aad6b930c9410d1"
SRCREV_machine_qemux86 ?= "f4e52341c304e044dbe581a35aad6b930c9410d1"
SRCREV_machine_qemux86-64 ?= "f4e52341c304e044dbe581a35aad6b930c9410d1"
SRCREV_machine_qemumips64 ?= "857685d23d1e8d8a8deb4198b139b95a5bb80825"
SRCREV_machine ?= "f4e52341c304e044dbe581a35aad6b930c9410d1"
SRCREV_meta ?= "bbaf01752b0168a63b164978495fad4ead7e8972"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.22"

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
