KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "aa956be29cf5247a0870728b177ae056bc9f62be"
SRCREV_machine_qemuarm64 ?= "846e0a90dd228d07272bf5b246aab405ec6941f8"
SRCREV_machine_qemumips ?= "931c8308f3327ea0a882349321989be6db3b22a9"
SRCREV_machine_qemuppc ?= "846e0a90dd228d07272bf5b246aab405ec6941f8"
SRCREV_machine_qemux86 ?= "846e0a90dd228d07272bf5b246aab405ec6941f8"
SRCREV_machine_qemux86-64 ?= "846e0a90dd228d07272bf5b246aab405ec6941f8"
SRCREV_machine_qemumips64 ?= "33eebd8a461ae8c619ece37a1de02e48893e0b12"
SRCREV_machine ?= "846e0a90dd228d07272bf5b246aab405ec6941f8"
SRCREV_meta ?= "98729aff41208eed09dc232254d7a1305237fdbe"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.28"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
