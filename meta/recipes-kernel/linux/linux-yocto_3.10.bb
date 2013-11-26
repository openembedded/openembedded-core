require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "9aac62bcd594648a7da61fa89e7da14eb2747e31"
SRCREV_machine_qemumips  ?= "141675948177bed049477ea53203dd0fc3c105c4"
SRCREV_machine_qemuppc ?= "ebcaee076da19ffe0e317089566d22c199f16fad"
SRCREV_machine_qemux86 ?= "2d96adf459037aec2c0027503c3eddfc20de9ad4"
SRCREV_machine_qemux86-64 ?= "2d96adf459037aec2c0027503c3eddfc20de9ad4"
SRCREV_machine_qemumips64 ?= "7acc7b9b44fb0b97a50e75387a11a24dfd5c242a"
SRCREV_machine ?= "2d96adf459037aec2c0027503c3eddfc20de9ad4"
SRCREV_meta ?= "41072c6288b1ab468670ebf7b3d411fbc177317c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.19"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
