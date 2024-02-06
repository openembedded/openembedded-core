KBRANCH ?= "v6.6/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.6.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.6/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.6/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.6/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.6/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.6/standard/base"
KBRANCH:qemuriscv32  ?= "v6.6/standard/base"
KBRANCH:qemux86  ?= "v6.6/standard/base"
KBRANCH:qemux86-64 ?= "v6.6/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.6/standard/base"
KBRANCH:qemumips64 ?= "v6.6/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "d35039de8e71f9cd91e0056a3b107f5b02766bdb"
SRCREV_machine:qemuarm64 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemuloongarch64 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemumips ?= "7b5332f480366b75d8c676482731a6b388ff5b0f"
SRCREV_machine:qemuppc ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemuriscv64 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemuriscv32 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemux86 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemux86-64 ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_machine:qemumips64 ?= "5915b5fa9b827e29cf1946dc0f92ab2b481de011"
SRCREV_machine ?= "43f7233f561431ae4c0e745985921bc84095a39e"
SRCREV_meta ?= "c32ab927d38291b1d2003ff491aa2ad4b60e1241"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "62b19b564504d027296502e0e30baf5fe0ef080a"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.6/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.6.13"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "arm/versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

