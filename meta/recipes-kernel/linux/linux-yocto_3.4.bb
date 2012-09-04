require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "67376c621baf82ce51657246896647ababf5aa7c"
SRCREV_machine_qemumips  ?= "7a8c1836ce33d49ff68e549e9415538a7419e3dc"
SRCREV_machine_qemuppc ?= "5a3c4cf87fd94add818880a158e8e5a9e07303c9"
SRCREV_machine_qemux86 ?= "a4c7a048fe3407e8eea0020db4a9c41d3feb8247"
SRCREV_machine_qemux86-64 ?= "a4c7a048fe3407e8eea0020db4a9c41d3feb8247"
SRCREV_machine ?= "a4c7a048fe3407e8eea0020db4a9c41d3feb8247"
SRCREV_meta ?= "463299bc2e533e1bd38b0053ae7b210980f269c3"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.9"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
