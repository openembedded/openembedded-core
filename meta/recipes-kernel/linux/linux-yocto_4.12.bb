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

SRCREV_machine_qemuarm ?= "9f857972035c7e3ae237d25a581eb695ffe78249"
SRCREV_machine_qemuarm64 ?= "1d685baca196583254d75f5cc7bc0ffedb8753ad"
SRCREV_machine_qemumips ?= "6527cf00278f043ba8f874ac7262261dd57e9a79"
SRCREV_machine_qemuppc ?= "1d685baca196583254d75f5cc7bc0ffedb8753ad"
SRCREV_machine_qemux86 ?= "1d685baca196583254d75f5cc7bc0ffedb8753ad"
SRCREV_machine_qemux86-64 ?= "1d685baca196583254d75f5cc7bc0ffedb8753ad"
SRCREV_machine_qemumips64 ?= "ad347d33914f66c4b2aa6fca0c9f0a92773885cc"
SRCREV_machine ?= "1d685baca196583254d75f5cc7bc0ffedb8753ad"
SRCREV_meta ?= "e3c9041f48ae980cc4461f969d0e3ed9bb43be5b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.16"

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
