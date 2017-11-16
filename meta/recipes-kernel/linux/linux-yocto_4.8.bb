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

SRCREV_machine_qemuarm ?= "d0a2f088f24c67fc2677df9c285dfedf9311be0c"
SRCREV_machine_qemuarm64 ?= "2ba5f01e49d92febf8413992f44d17d53d78ec15"
SRCREV_machine_qemumips ?= "4f0218a05972db39ca645670954346f5b0f9ff10"
SRCREV_machine_qemuppc ?= "b32a9d28b427588e3f15292cfda7d2207acf80ce"
SRCREV_machine_qemux86 ?= "27efc3ba684a65413ed295140ea070508aac9def"
SRCREV_machine_qemux86-64 ?= "27efc3ba684a65413ed295140ea070508aac9def"
SRCREV_machine_qemumips64 ?= "50985b5d1574aad8fac520487aa7901b593db730"
SRCREV_machine ?= "27efc3ba684a65413ed295140ea070508aac9def"
SRCREV_meta ?= "1c60e003c70292e04f18d5123c7f3f26ffae5c3f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.26"

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
