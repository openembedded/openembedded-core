require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemu-ppc32"

LINUX_VERSION ?= "3.2.18"
LINUX_KERNEL_TYPE = "preempt-rt"

KMETA = "meta"

SRCREV_machine ?= "01c5c310886e87e785db5c3bb776deb5ed2e03b2"
SRCREV_machine_qemuppc ?= "9f5b45a730b902ed8a0809c9542512188e8fcabf"
SRCREV_meta ?= "07ee09b520579b9f29bd15fefb01fd28b34c6064"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"
