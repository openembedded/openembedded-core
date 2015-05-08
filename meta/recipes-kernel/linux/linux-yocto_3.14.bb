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

SRCREV_machine_qemuarm ?= "e4f91293908648d495b4f9735768633569e39b9f"
SRCREV_machine_qemuarm64 ?= "b991a586470900e3de824f5ea75653aa2150594d"
SRCREV_machine_qemumips ?= "6623389731309b0ac591736d5b438de833f195c7"
SRCREV_machine_qemuppc ?= "3217fbcd002914fba4914d2e61dabb378081fd25"
SRCREV_machine_qemux86 ?= "8794bd6a473199b2baa7a405d44cdbfa5351b60b"
SRCREV_machine_qemux86-64 ?= "b991a586470900e3de824f5ea75653aa2150594d"
SRCREV_machine_qemumips64 ?= "6649ef219b3bf6da1678547b9da376367520e80a"
SRCREV_machine ?= "b991a586470900e3de824f5ea75653aa2150594d"
SRCREV_meta ?= "46df2668fa657162d11f96a6f1af138c562a03aa"

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
