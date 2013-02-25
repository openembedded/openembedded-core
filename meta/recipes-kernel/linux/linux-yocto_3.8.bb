require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "351d133943b50a9dfeee07661d44254722a19f04"
SRCREV_machine_qemumips  ?= "d529d082041142435587bd442b1235dbe1c72950"
SRCREV_machine_qemuppc ?= "8dcd155ad408658e9180d1630da2ac7e0ee70542"
SRCREV_machine_qemux86 ?= "b170394a475b96ecc92cbc9e4b002bed0a9f69c5"
SRCREV_machine_qemux86-64 ?= "b170394a475b96ecc92cbc9e4b002bed0a9f69c5"
SRCREV_machine ?= "b170394a475b96ecc92cbc9e4b002bed0a9f69c5"
SRCREV_meta ?= "c2ed0f16fdec628242a682897d5d86df4547cf24"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
