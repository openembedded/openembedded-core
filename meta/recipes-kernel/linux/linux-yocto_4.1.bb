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

SRCREV_machine_qemuarm ?= "c92614c6b90f8ab1202b20cc843a9782c9f8f8d5"
SRCREV_machine_qemuarm64 ?= "a8abc111a96dd07a2884ebbba668ef8dec15f264"
SRCREV_machine_qemumips ?= "6b4b777e8467f01b00c4494ffedf1e9134bc52c5"
SRCREV_machine_qemuppc ?= "a8abc111a96dd07a2884ebbba668ef8dec15f264"
SRCREV_machine_qemux86 ?= "a8abc111a96dd07a2884ebbba668ef8dec15f264"
SRCREV_machine_qemux86-64 ?= "a8abc111a96dd07a2884ebbba668ef8dec15f264"
SRCREV_machine_qemumips64 ?= "90222ca39af5a3252fcba24baec341140e9e7c07"
SRCREV_machine ?= "a8abc111a96dd07a2884ebbba668ef8dec15f264"
SRCREV_meta ?= "3d8f1378d07dbc052ca8a7c22297339ad7998b5e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.8"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuarm=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
