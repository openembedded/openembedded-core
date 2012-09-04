require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "84d8ee32265eea5d60f57a2f70bd3b9a0fb9213d"
SRCREV_machine_qemumips  ?= "ba0e336d4527080233c3c410989d4f351529ee4e"
SRCREV_machine_qemuppc ?= "e82b8a111430e3820b11f507863c4b8e8734ed8e"
SRCREV_machine_qemux86 ?= "0985844fa6235422c67ef269952fa4e765f252f9"
SRCREV_machine_qemux86-64 ?= "0985844fa6235422c67ef269952fa4e765f252f9"
SRCREV_machine ?= "0985844fa6235422c67ef269952fa4e765f252f9"
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
