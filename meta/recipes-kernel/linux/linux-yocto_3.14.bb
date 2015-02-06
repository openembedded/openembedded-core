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

SRCREV_machine_qemuarm ?= "1a9f9edade8d8c0120d2f47dfd15edd41c7a3439"
SRCREV_machine_qemuarm64 ?= "f65678ef48c5d41af914d2769e4dd01411c1df96"
SRCREV_machine_qemumips ?= "68e9a20079f3cc333c3b7d945bb65fa74772aa37"
SRCREV_machine_qemuppc ?= "a992646d267f012f87967f1c8b88608c950c7e58"
SRCREV_machine_qemux86 ?= "2f37d969c5d3b28d936ac3533862e2ab034d4f37"
SRCREV_machine_qemux86-64 ?= "f65678ef48c5d41af914d2769e4dd01411c1df96"
SRCREV_machine_qemumips64 ?= "e93012684a290e9b60d61f58c2dbd36ab3f2e549"
SRCREV_machine ?= "f65678ef48c5d41af914d2769e4dd01411c1df96"
SRCREV_meta ?= "6eddbf47875ef48ddc5864957a7b63363100782b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.29"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
