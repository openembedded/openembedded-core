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

SRCREV_machine_qemuarm ?= "2f133a201f2bbde4ce33731d1596d5144360bf87"
SRCREV_machine_qemuarm64 ?= "f6ab3612272bb22bac371403121609c66c50cd45"
SRCREV_machine_qemumips ?= "d1cbc627631f706d19e8e4518bfc38b266f3fb9f"
SRCREV_machine_qemuppc ?= "d1699ce26291a3e07113057ad7e235f7891e73fb"
SRCREV_machine_qemux86 ?= "f6ab3612272bb22bac371403121609c66c50cd45"
SRCREV_machine_qemux86-64 ?= "f6ab3612272bb22bac371403121609c66c50cd45"
SRCREV_machine_qemumips64 ?= "725278e5cbc402139051032a7b2137f6707c46d6"
SRCREV_machine ?= "f6ab3612272bb22bac371403121609c66c50cd45"
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
