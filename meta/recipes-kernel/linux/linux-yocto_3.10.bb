require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "07224529d7ceb1e16eb815b8dd4c03ad9d58653d"
SRCREV_machine_qemumips  ?= "7747c5a158a99b8a668fd526f1f7f1f7f7e03d3e"
SRCREV_machine_qemuppc ?= "d63375e4b0a2de3ba110b83f78d6be6e01ec5c9e"
SRCREV_machine_qemux86 ?= "3f6c8243ed67011f70b27952d42657cf3a2c3115"
SRCREV_machine_qemux86-64 ?= "3f6c8243ed67011f70b27952d42657cf3a2c3115"
SRCREV_machine_qemumips64 ?= "9a4f9f4d44fdcd25fe365216b366bc346efb23d0"
SRCREV_machine ?= "3f6c8243ed67011f70b27952d42657cf3a2c3115"
SRCREV_meta ?= "452f0679ea93a6cb4433bebd7177629228a5cf68"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.11"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
