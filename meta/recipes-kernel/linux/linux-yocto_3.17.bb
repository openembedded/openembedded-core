KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b3934b47d8a16fe323546d7dca3529bf75ef6245"
SRCREV_machine_qemumips ?= "9b74a60868ec2e45ecaea70547331b1ef6776b14"
SRCREV_machine_qemuppc ?= "d63220db68089d0604cadd2d0c3d345dfb0dfdb1"
SRCREV_machine_qemux86 ?= "460fa8aefe8c4f695245ad3a21da96af2c341f21"
SRCREV_machine_qemux86-64 ?= "460fa8aefe8c4f695245ad3a21da96af2c341f21"
SRCREV_machine_qemumips64 ?= "9faa5776183318eb41c191bacd05913166d19eb5"
SRCREV_machine ?= "460fa8aefe8c4f695245ad3a21da96af2c341f21"
SRCREV_meta ?= "6050ee24b0b51ccaf35894afc5def0b59f434977"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc6"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
