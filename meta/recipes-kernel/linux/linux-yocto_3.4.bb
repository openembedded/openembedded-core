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

SRCREV_machine_qemuarm ?= "64f9a290683b1ab08136ef5d54d1845a961a6940"
SRCREV_machine_qemumips  ?= "ad7aa3ecf8ab00055d31c3f7b059c8b8a8633e19"
SRCREV_machine_qemuppc ?= "7aaa5a4debe4b1987d2d11707313e682b185e876"
SRCREV_machine_qemux86 ?= "95a3ff6e3c2eec2fafe9d1813048fcdb468a678e"
SRCREV_machine_qemux86-64 ?= "95a3ff6e3c2eec2fafe9d1813048fcdb468a678e"
SRCREV_machine ?= "95a3ff6e3c2eec2fafe9d1813048fcdb468a678e"
SRCREV_meta ?= "7c9e1e0117e7ca1f7451870dad5db50adc21732e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.85"

PR = "${INC_PR}.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
