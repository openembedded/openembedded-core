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

SRCREV_machine_qemuarm ?= "d1cea997ae512ee325600a247d75027e65655e0a"
SRCREV_machine_qemuarm64 ?= "4434aa71ff7043c570f9eae493df1ccadbda9b85"
SRCREV_machine_qemumips ?= "c37155f99472e7dc9f94b3bda72c73a3f718fdbf"
SRCREV_machine_qemuppc ?= "521b9fd001dc25a446d39f349cdfb7f9f5697d05"
SRCREV_machine_qemux86 ?= "f7cbba6012312f5bf9b7279bafa8bb09d18b547d"
SRCREV_machine_qemux86-64 ?= "4434aa71ff7043c570f9eae493df1ccadbda9b85"
SRCREV_machine_qemumips64 ?= "a777f11a26f075b71becb47b5133252c5d8fafff"
SRCREV_machine ?= "4434aa71ff7043c570f9eae493df1ccadbda9b85"
SRCREV_meta ?= "162dfe3bb092c1a792e5ed224fe09672e9814b24"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.36"

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
