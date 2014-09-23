KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "8aa46f1d04208040be52feb422f2aae48655b51f"
SRCREV_machine_qemumips ?= "b32a452b97972bcaba5a67383f209cec06dfe089"
SRCREV_machine_qemuppc ?= "72b05f144a4d5331ae33bff0b6f4a70fb90d2b3a"
SRCREV_machine_qemux86 ?= "724ab25b085554741753709941e768cae1a2121e"
SRCREV_machine_qemux86-64 ?= "724ab25b085554741753709941e768cae1a2121e"
SRCREV_machine_qemumips64 ?= "6dabf96a2fe457e4cb8821721936e9fb72556494"
SRCREV_machine ?= "724ab25b085554741753709941e768cae1a2121e"
SRCREV_meta ?= "62a1fd63023ab6b18114dd093be86d0854474c41"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-dev.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17-rc5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
