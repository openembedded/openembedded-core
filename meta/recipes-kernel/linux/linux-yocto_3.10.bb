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

SRCREV_machine_qemuarm ?= "978f148bf7dd253c901d4b16401309fc50eed0a9"
SRCREV_machine_qemumips ?= "0d1b0e40b279433b1a96ed8ee02a10f6d2c7bea7"
SRCREV_machine_qemuppc ?= "ae9aa3e76bcc720993b02830b1cae9b15ae9b6c6"
SRCREV_machine_qemux86 ?= "e9cdab78bed262dbeadc7f403989f20972bcddde"
SRCREV_machine_qemux86-64 ?= "e9cdab78bed262dbeadc7f403989f20972bcddde"
SRCREV_machine_qemumips64 ?= "38d9a2f8f334e1896629572f25968f34d8fb188d"
SRCREV_machine ?= "e9cdab78bed262dbeadc7f403989f20972bcddde"
SRCREV_meta ?= "713abc0efa9fc21234b2f342d0a849e4a4a36c65"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.28"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
