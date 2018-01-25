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

SRCREV_machine_qemuarm ?= "801a684d5e769ca18e6ad9758806a528eccdcb73"
SRCREV_machine_qemuarm64 ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_machine_qemumips ?= "8235c7fda39963abbf6347390fd460e18a7e47b5"
SRCREV_machine_qemuppc ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_machine_qemux86 ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_machine_qemux86-64 ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_machine_qemumips64 ?= "bd9d169cd29aa5f2397ff383cd29f84a1043a693"
SRCREV_machine ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_meta ?= "ef2f5d9a0ac1c5ac60e76b18b0bb3393be450336"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.78"

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
