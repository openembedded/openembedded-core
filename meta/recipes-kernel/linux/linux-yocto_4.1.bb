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

SRCREV_machine_qemuarm ?= "fa1f75003687404ebe8a07c967db1714c700f9ff"
SRCREV_machine_qemuarm64 ?= "f1069e0dbbc09f930ba8941e38859378ccc22f96"
SRCREV_machine_qemumips ?= "0e2b4693ec90691494752ff7854493f3c7f3fc2f"
SRCREV_machine_qemuppc ?= "f1069e0dbbc09f930ba8941e38859378ccc22f96"
SRCREV_machine_qemux86 ?= "f1069e0dbbc09f930ba8941e38859378ccc22f96"
SRCREV_machine_qemux86-64 ?= "f1069e0dbbc09f930ba8941e38859378ccc22f96"
SRCREV_machine_qemumips64 ?= "ca5c9e5ee5e3b569a55beb05e2f81078d3db2cef"
SRCREV_machine ?= "f1069e0dbbc09f930ba8941e38859378ccc22f96"
SRCREV_meta ?= "9f68667031354532563766a3d04ca8a618e9177a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.26"

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
