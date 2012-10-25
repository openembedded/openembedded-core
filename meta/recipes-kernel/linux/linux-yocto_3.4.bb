require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "cec289be595a5889740ad4f0a58d12579f528d5e"
SRCREV_machine_qemumips  ?= "01ab58eb9dc64ceccb2d7be80ef4a511479fc899"
SRCREV_machine_qemuppc ?= "c820da3c7fa5176507c116352ee3bd1e682f36bd"
SRCREV_machine_qemux86 ?= "218bd8d2022b9852c60d32f0d770931e3cf343e2"
SRCREV_machine_qemux86-64 ?= "218bd8d2022b9852c60d32f0d770931e3cf343e2"
SRCREV_machine ?= "218bd8d2022b9852c60d32f0d770931e3cf343e2"
SRCREV_meta ?= "68a635bf8dfb64b02263c1ac80c948647cc76d5f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.11"

PR = "${INC_PR}.3"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES_append = " features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm"
KERNEL_FEATURES_append_qemux86-64=" cfg/paravirt_kvm"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32", "" ,d)}"
