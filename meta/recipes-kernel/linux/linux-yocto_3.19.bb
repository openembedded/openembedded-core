KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "e5aa4ad4249e079b8459a90b4ffa50d04b474458"
SRCREV_machine_qemuarm64 ?= "d3c0b958e68824c7037005e2a84e5661ba98d5b0"
SRCREV_machine_qemumips ?= "3b8cc925c5312b37cb824dfe58c2a4dcbb2b9732"
SRCREV_machine_qemuppc ?= "4ac61cf97719f01c5cb226881956e77624fb534c"
SRCREV_machine_qemux86 ?= "d3c0b958e68824c7037005e2a84e5661ba98d5b0"
SRCREV_machine_qemux86-64 ?= "d3c0b958e68824c7037005e2a84e5661ba98d5b0"
SRCREV_machine_qemumips64 ?= "f5bf944a0edde0e379af4296917d33a562e6586b"
SRCREV_machine ?= "d3c0b958e68824c7037005e2a84e5661ba98d5b0"
SRCREV_meta ?= "727cfce45077a1f73f1dbc42e8093fde0c5e36a7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19.5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
