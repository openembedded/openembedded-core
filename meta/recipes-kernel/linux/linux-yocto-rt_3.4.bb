require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/preempt-rt/base"
KBRANCH_qemuppc = "standard/preempt-rt/qemuppc"

LINUX_VERSION ?= "3.4.9"
LINUX_KERNEL_TYPE = "preempt-rt"

KMETA = "meta"

SRCREV_machine ?= "52518e60a7220385f31cdd09b83dacd84581c88c"
SRCREV_machine_qemuppc ?= "e6db5e91e1a5b31d26ac67778df7fb1fd5e42aa5"
SRCREV_meta ?= "463299bc2e533e1bd38b0053ae7b210980f269c3"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"
