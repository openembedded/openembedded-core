require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b38b84aebf889d84e65e81ac11122b977f0c5155"
SRCREV_machine_qemumips ?= "c9d827207d8dfab330787659b2842485dbd36d77"
SRCREV_machine_qemuppc ?= "58b7cb00580985410ba8491c61e80d2572552ed9"
SRCREV_machine_qemux86 ?= "5b327970eb1dba02c65cb8330dc8f3049c4fa580"
SRCREV_machine_qemux86-64 ?= "5724bf17acbf54cf61003ab242448fd96d189384"
SRCREV_machine_qemumips64 ?= "34837892b66eaa034cd3e3d339cab0ea6f594511"
SRCREV_machine ?= "5724bf17acbf54cf61003ab242448fd96d189384"
SRCREV_meta ?= "54d07ec2566afe2a4c1eee4995781aab5599b5d5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
