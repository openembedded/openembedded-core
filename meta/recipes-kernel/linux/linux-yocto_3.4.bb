require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "a4026fce19f8b2d28f37ea264693bd70126a7ade"
SRCREV_machine_qemumips  ?= "372aba9e80c5f86a1f6795a7b2292a05159ef108"
SRCREV_machine_qemuppc ?= "c456ec65de4d2ac099312b0ed8e4098b29447929"
SRCREV_machine_qemux86 ?= "fff57da7886cf5e99c07adf6649610cb1cd89330"
SRCREV_machine_qemux86-64 ?= "fff57da7886cf5e99c07adf6649610cb1cd89330"
SRCREV_machine ?= "fff57da7886cf5e99c07adf6649610cb1cd89330"
SRCREV_meta ?= "1bab5bd090948b4cc4c4ed57c834603a3cf9f235"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.43"

PR = "${INC_PR}.4"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append = " features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
