KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "bc2e4081f235174c696cc4b30dbea51555a1c3d6"
SRCREV_machine_qemuarm64 ?= "7534aeb01883b48cc42eb4900d0a8c64e8160e14"
SRCREV_machine_qemumips ?= "cbbe5f1b5477507aa668b4117a8dedcb407a0571"
SRCREV_machine_qemuppc ?= "038bbef0f64321a0f9d77e9b07dff3ae1e51b51a"
SRCREV_machine_qemux86 ?= "48833301518748d82cbc2412fea3617eeee6d01b"
SRCREV_machine_qemux86-64 ?= "7534aeb01883b48cc42eb4900d0a8c64e8160e14"
SRCREV_machine_qemumips64 ?= "c910c6d8338ab7291f066edc06de83de5b645d8f"
SRCREV_machine ?= "7534aeb01883b48cc42eb4900d0a8c64e8160e14"
SRCREV_meta ?= "b55cfc0308bf7158843db4b8f69f866487a0919e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;branch=${KBRANCH};name=machine; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-3.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "3.14.36"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
