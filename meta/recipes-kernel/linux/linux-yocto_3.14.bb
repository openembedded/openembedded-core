KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "c21684fb4fad62bceafcc97ae592d1d1bd52cf36"
SRCREV_machine_qemumips ?= "626a3a63414413e652daf3288903a58973d34520"
SRCREV_machine_qemuppc ?= "a5e37ff008b4bf2c0cb573f54376cb8b48a79016"
SRCREV_machine_qemux86 ?= "a39fd81fa54776b2ac8c288251846890c3124dee"
SRCREV_machine_qemux86-64 ?= "dbe5b52e93ff114b2c0f5da6f6af91f52c18f2b8"
SRCREV_machine_qemumips64 ?= "c14d3c68c725205a5724cffe9f6f688d1bc38b6e"
SRCREV_machine ?= "dbe5b52e93ff114b2c0f5da6f6af91f52c18f2b8"
SRCREV_meta ?= "0bd3ec19b64191efc9d0ba83d05c9f7f9bf4473a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.19"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
