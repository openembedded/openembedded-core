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

SRCREV_machine_qemuarm ?= "6f2317f8a00a3eb6a2b28ca51f336f61eb6fe160"
SRCREV_machine_qemuarm64 ?= "13852755ecbf491848afbe40e66fc152bc70915b"
SRCREV_machine_qemumips ?= "9fc8fc49e5065f1ee76e964a4c0257291ab3e62a"
SRCREV_machine_qemuppc ?= "13852755ecbf491848afbe40e66fc152bc70915b"
SRCREV_machine_qemux86 ?= "13852755ecbf491848afbe40e66fc152bc70915b"
SRCREV_machine_qemux86-64 ?= "13852755ecbf491848afbe40e66fc152bc70915b"
SRCREV_machine_qemumips64 ?= "d1c1f36412d196b560ed9f19392e291b5492b94c"
SRCREV_machine ?= "13852755ecbf491848afbe40e66fc152bc70915b"
SRCREV_meta ?= "29c7a2a5b02c1376b3345eeedea12c42806e89d9"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.13"

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
