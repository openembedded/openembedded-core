inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.8"

SRCREV_machine_qemuarm ?= "d2f4973505e9cebcf2fc0058434214927deed5e6"
SRCREV_machine_qemumips ?= "42968ceaecd71ae57157676aa63542db409732cb"
SRCREV_machine_qemuppc ?= "16eb6506148ff163cfc4f4516110275726831014"
SRCREV_machine_qemux86 ?= "fea3842615c13a54180b6600783b222f499002ef"
SRCREV_machine_qemux86-64 ?= "5f86f8f0a3102e1c7d0164abefcd50d825aa468f"
SRCREV_machine ?= "a811486d28dd9b0e1af0672a65ad9fa97873b82a"
SRCREV_meta ?= "ae3e64c077972fe87f09946bd215620df68ca327"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
