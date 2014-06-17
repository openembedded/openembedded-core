require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "57c7d98c639aa69ed4640aaf0077dfc6f6213b62"
SRCREV_machine_qemumips ?= "0c95bd5e4fd9834de451197edb2f0acb7d93d1e8"
SRCREV_machine_qemuppc ?= "9a15b569a7c7340e895327ba464e8cd852914c2b"
SRCREV_machine_qemux86 ?= "4aa0cb556f1a3457d0cb3285d0ba2aa073b745eb"
SRCREV_machine_qemux86-64 ?= "8e552bfb9824bbfdda3b5353dab290519a7332c7"
SRCREV_machine_qemumips64 ?= "42cb63ec0929a3e488b3c61cef31c6ee5b0e1005"
SRCREV_machine ?= "8e552bfb9824bbfdda3b5353dab290519a7332c7"
SRCREV_meta ?= "135b9fb4b8d66b8df9abcd22f325e77614d35d2e"

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
