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

SRCREV_machine_qemuarm ?= "7d0c6a3197a456d36c190f5127b06e8525b6c874"
SRCREV_machine_qemuarm64 ?= "c85c54f5bf53b98afe8105e91bffcdb6c60afe8f"
SRCREV_machine_qemumips ?= "2b50d9fc7d329e661fd9fa317f60ee26746c30ae"
SRCREV_machine_qemuppc ?= "c85c54f5bf53b98afe8105e91bffcdb6c60afe8f"
SRCREV_machine_qemux86 ?= "c85c54f5bf53b98afe8105e91bffcdb6c60afe8f"
SRCREV_machine_qemux86-64 ?= "c85c54f5bf53b98afe8105e91bffcdb6c60afe8f"
SRCREV_machine_qemumips64 ?= "13818cbe0b9f0e2e7542b7c088b5719c7a463da9"
SRCREV_machine ?= "c85c54f5bf53b98afe8105e91bffcdb6c60afe8f"
SRCREV_meta ?= "f9cb76e24e3a73b8eda70ea9bd91ba5a52711521"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.8"

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
