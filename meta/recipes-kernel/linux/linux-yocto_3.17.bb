KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "12991e8ac40c51ef3e337f17f12aa59c6500fc7f"
SRCREV_machine_qemumips ?= "1f4735ed314defc95ab9929258ad12844cc2c676"
SRCREV_machine_qemuppc ?= "3231dececf867215ecab7c7f9ec3a7a7640a3f32"
SRCREV_machine_qemux86 ?= "0409b1fbed221e61212e17b7637fa54f908d83f6"
SRCREV_machine_qemux86-64 ?= "0409b1fbed221e61212e17b7637fa54f908d83f6"
SRCREV_machine_qemumips64 ?= "7005c584078109dae03567e9c597a847a2bc5136"
SRCREV_machine ?= "0409b1fbed221e61212e17b7637fa54f908d83f6"
SRCREV_meta ?= "f24c2bf9c298595d00a9d8600841f2b0206e1fba"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17.8"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
