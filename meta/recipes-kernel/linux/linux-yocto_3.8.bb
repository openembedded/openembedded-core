require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "bf458ca0e48f4f57cbb02b52070a000f361eec84"
SRCREV_machine_qemumips  ?= "c7297b41016919c95a2c779cafc0aad736f6a400"
SRCREV_machine_qemuppc ?= "f8848d222d1fcd1ebc517d28f289735621708449"
SRCREV_machine_qemux86 ?= "42ddf06111efe45f3c36012d5a04a1eeb9781f42"
SRCREV_machine_qemux86-64 ?= "42ddf06111efe45f3c36012d5a04a1eeb9781f42"
SRCREV_machine ?= "42ddf06111efe45f3c36012d5a04a1eeb9781f42"
SRCREV_meta ?= "1b534b2f8bbe9b8a773268cfa30a4850346f6f5f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
