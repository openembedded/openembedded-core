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

SRCREV_machine_qemuarm ?= "460340f28dd4ed608fd66692bcbac0bb24fe4aaf"
SRCREV_machine_qemuarm64 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemumips ?= "0ff21b2a5e92defa32b7cb76d39980b63a2eb5ca"
SRCREV_machine_qemuppc ?= "6bd2872ce7e397d285f7df00f4d0efac286e2401"
SRCREV_machine_qemux86 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemux86-64 ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_machine_qemumips64 ?= "248861985b609d1cd24e8d23b7d15abfb31cbdc2"
SRCREV_machine ?= "44af900716206d4cae283aa74e92f4118720724a"
SRCREV_meta ?= "0d6de63d4603b9cc3a4a68391bcb5156b9b0cf96"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.28"

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
