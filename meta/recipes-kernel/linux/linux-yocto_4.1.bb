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

SRCREV_machine_qemuarm ?= "494bf106c15685dafa318f418ce86228728006f1"
SRCREV_machine_qemuarm64 ?= "75d56a13f86fc48002e4a3f9ed60546db30432b7"
SRCREV_machine_qemumips ?= "3d9c0d5f502f7ae6f74766b2e9980df2cf5f661c"
SRCREV_machine_qemuppc ?= "75d56a13f86fc48002e4a3f9ed60546db30432b7"
SRCREV_machine_qemux86 ?= "75d56a13f86fc48002e4a3f9ed60546db30432b7"
SRCREV_machine_qemux86-64 ?= "75d56a13f86fc48002e4a3f9ed60546db30432b7"
SRCREV_machine_qemumips64 ?= "6c9b0acfa18a1c86da12ae17da643cde03dd82cd"
SRCREV_machine ?= "75d56a13f86fc48002e4a3f9ed60546db30432b7"
SRCREV_meta ?= "0845ec79bc2fbc45efcf4c44138fd698039960c5"

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
