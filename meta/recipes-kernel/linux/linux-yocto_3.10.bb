require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "7744145fcc3e4b6b6e308a1b86dc1c8b237b7575"
SRCREV_machine_qemumips ?= "2b8e8c694c38729ce724facdb0ab20751d061a2e"
SRCREV_machine_qemuppc ?= "9a7537cb9bcc5c835dae6ac84fbbe7298473f69c"
SRCREV_machine_qemux86 ?= "a9ec82e355130160f9094e670bd5be0022a84194"
SRCREV_machine_qemux86-64 ?= "a9ec82e355130160f9094e670bd5be0022a84194"
SRCREV_machine_qemumips64 ?= "0cd8e958d0ec7e01fd21fc7891da7eed688b0d37"
SRCREV_machine ?= "a9ec82e355130160f9094e670bd5be0022a84194"
SRCREV_meta ?= "d9cd83c0292bd4e2a6754a96761027252e726a42"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.19"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
