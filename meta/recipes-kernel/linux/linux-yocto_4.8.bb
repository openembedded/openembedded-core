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

SRCREV_machine_qemuarm ?= "f46e49344b4c66a6f0917c5a2e9a1d146176519d"
SRCREV_machine_qemuarm64 ?= "67813e7efa3a4614e209c2f058d92ef9a636441a"
SRCREV_machine_qemumips ?= "5a299402ae58d61429ab091f94748550efe45858"
SRCREV_machine_qemuppc ?= "67813e7efa3a4614e209c2f058d92ef9a636441a"
SRCREV_machine_qemux86 ?= "67813e7efa3a4614e209c2f058d92ef9a636441a"
SRCREV_machine_qemux86-64 ?= "67813e7efa3a4614e209c2f058d92ef9a636441a"
SRCREV_machine_qemumips64 ?= "674818dad577cdfc23c6c857aa2b769fc64e379c"
SRCREV_machine ?= "67813e7efa3a4614e209c2f058d92ef9a636441a"
SRCREV_meta ?= "552a83790b1797b6dd4e4c48ff5bc8f215ed57da"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8"

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
