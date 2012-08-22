require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "yocto/standard/base"
KBRANCH_qemux86  = "yocto/standard/common-pc/base"
KBRANCH_qemux86-64  = "yocto/standard/common-pc-64/base"
KBRANCH_qemuppc  = "yocto/standard/qemu-ppc32"
KBRANCH_qemumips = "yocto/standard/mti-malta32-be"
KBRANCH_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KMETA = "meta"

LINUX_VERSION ?= "3.0.32"

SRCREV_machine_qemuarm ?= "f2d606dfbc61d10847783f1e7c1dcb0ecabf3220"
SRCREV_machine_qemumips  ?= "57f4bbfb4c65e4c8e349401b877f1661fb026ed6"
SRCREV_machine_qemuppc ?= "d8779a6245d13c3b56eabac36a14c8896f448566"
SRCREV_machine_qemux86 ?= "33d5d1ea371dad280e5bcfadd12c3a360c6bc5b8"
SRCREV_machine_qemux86-64 ?= "fe23c7dd94eb94dd5887028683093615ac921086"
SRCREV_machine ?= "cef17a18d72eae749dc78de3c83772f52815d842"
SRCREV_meta ?= "bf5ee4945ee6d748e6abe16356f2357f76b5e2f0"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"
