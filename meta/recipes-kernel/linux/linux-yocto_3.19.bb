KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "473e2f3788730c51e82714a9785325b6308f6762"
SRCREV_machine_qemuarm64 ?= "31b35da6a5c8a2b162f6c33202e9b64dd13757d5"
SRCREV_machine_qemumips ?= "d43f1cbf282d020f7aa31d68a54b2876d2c0e81b"
SRCREV_machine_qemuppc ?= "35de413056b86191963ffe686913da31b978a9b3"
SRCREV_machine_qemux86 ?= "31b35da6a5c8a2b162f6c33202e9b64dd13757d5"
SRCREV_machine_qemux86-64 ?= "31b35da6a5c8a2b162f6c33202e9b64dd13757d5"
SRCREV_machine_qemumips64 ?= "d35649ef8cbb0a0404be5c721377b138866181ad"
SRCREV_machine ?= "31b35da6a5c8a2b162f6c33202e9b64dd13757d5"
SRCREV_meta ?= "9e70b482d3773abf92c9c5850e134cbca1d5651f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.19.2"

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
