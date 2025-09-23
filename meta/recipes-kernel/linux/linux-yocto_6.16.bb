KBRANCH ?= "v6.16/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.16.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.16/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.16/standard/base"
KBRANCH:qemumips ?= "v6.16/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.16/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.16/standard/base"
KBRANCH:qemuriscv32  ?= "v6.16/standard/base"
KBRANCH:qemux86  ?= "v6.16/standard/base"
KBRANCH:qemux86-64 ?= "v6.16/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.16/standard/base"
KBRANCH:qemumips64 ?= "v6.16/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "5fa52c596c9c560c6969fbcf0927c2d546e5d9a9"
SRCREV_machine:qemuarm64 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemuloongarch64 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemumips ?= "f421b1fee99c72d86eb152bb4828fdc078a268a8"
SRCREV_machine:qemuppc ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemuriscv64 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemuriscv32 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemux86 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemux86-64 ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_machine:qemumips64 ?= "e6f7e24a7b76c6d1b3eaa3716fa1c4185de94aa5"
SRCREV_machine ?= "ff2318af3a2513c944b2598ab3df356dd7f15f34"
SRCREV_meta ?= "cc3c965edfdad3a665bc69430e58ea726e02063f"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "62dae019823123ce3baa50e680219e2beb9a63a5"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.16/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.16;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.16.8"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "arm/versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall = " cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86 = " cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64 = " cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc features/nf_tables/nft_test.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc features/gpio/sim.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("KERNEL_DEBUG", "True", " features/reproducibility/reproducibility.scc features/debug/debug-btf.scc", "", d)}"
# libteam ptests from meta-oe needs it
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/net/team/team.scc", "", d)}"
# openl2tp tests from meta-networking needs it
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " cgl/cfg/net/l2tp.scc", "", d)}"
KERNEL_FEATURES:append:powerpc = " arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 = " arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le = " arch/powerpc/powerpc-debug.scc"
# Do not add debug info for riscv32, it fails during depmod
# ERROR: modpost: __ex_table+0x17a4 references non-executable section '.debug_loclists'
# Check again during next major version upgrade
KERNEL_FEATURES:remove:riscv32 = "features/debug/debug-kernel.scc"
INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"
