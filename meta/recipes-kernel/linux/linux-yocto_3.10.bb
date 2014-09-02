KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "cf951fb02f9eb89bd36f30fd6d426d6a06be8739"
SRCREV_machine_qemumips ?= "82c743bcc8103c82f8cb673eb810a9329a9441d7"
SRCREV_machine_qemuppc ?= "1deb0e97e9262b02b3ff9b707b13c42c8fa95a13"
SRCREV_machine_qemux86 ?= "e4f08d724d6663e6d23d19668c97f9e6792c94d2"
SRCREV_machine_qemux86-64 ?= "e4f08d724d6663e6d23d19668c97f9e6792c94d2"
SRCREV_machine_qemumips64 ?= "8affc1996043dfe2061953c074c714a9885fde6e"
SRCREV_machine ?= "e4f08d724d6663e6d23d19668c97f9e6792c94d2"
SRCREV_meta ?= "bacd863f7d1ce9135b8dde4394120f66707c7747"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.43"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
