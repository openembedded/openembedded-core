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

SRCREV_machine_qemuarm ?= "e89109c66eecdd78402a6814219658cf48efeba7"
SRCREV_machine_qemuarm64 ?= "7e7ac3f0fda9707e94b3311a4510ae080cd86238"
SRCREV_machine_qemumips ?= "76f8650f009abea482c8b9b54a4f33510e0812d7"
SRCREV_machine_qemuppc ?= "7e7ac3f0fda9707e94b3311a4510ae080cd86238"
SRCREV_machine_qemux86 ?= "7e7ac3f0fda9707e94b3311a4510ae080cd86238"
SRCREV_machine_qemux86-64 ?= "7e7ac3f0fda9707e94b3311a4510ae080cd86238"
SRCREV_machine_qemumips64 ?= "eae7e3959377e0d5514df8add61df074450dbd38"
SRCREV_machine ?= "7e7ac3f0fda9707e94b3311a4510ae080cd86238"
SRCREV_meta ?= "46171de19220c49d670544017cfbeffc1ec70e80"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

DEPENDS += "openssl-native util-linux-native"

LINUX_VERSION ?= "4.12.24"

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
