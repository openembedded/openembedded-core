KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "833523192da4c50dfbae5a06d9fcc578e091daf4"
SRCREV_machine_qemuarm64 ?= "dda8175b9f7ba47d308dd80c2a53e3c7e996d2dd"
SRCREV_machine_qemumips ?= "43e496c5172a6f1035ecc98e2fac9c5874cefcc1"
SRCREV_machine_qemuppc ?= "4507887bfd1ba5b3a337e71315fb8cad3be6670f"
SRCREV_machine_qemux86 ?= "e8410925ef1cdc57f4f367761e94863193014033"
SRCREV_machine_qemux86-64 ?= "e8410925ef1cdc57f4f367761e94863193014033"
SRCREV_machine_qemumips64 ?= "b2df75031185b81757c61e95142e0472314c8389"
SRCREV_machine ?= "e8410925ef1cdc57f4f367761e94863193014033"
SRCREV_meta ?= "426c8aa4862f95e84ec163b942b92726d3ef978f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.24"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
