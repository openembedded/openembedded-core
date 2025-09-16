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

SRCREV_machine:qemuarm ?= "29139e9a27fc305eb999bf2d02086deb441753ba"
SRCREV_machine:qemuarm64 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemuloongarch64 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemumips ?= "cd9be6bce5b979643b3ca76bbaf0fbe9680cc6c1"
SRCREV_machine:qemuppc ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemuriscv64 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemuriscv32 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemux86 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemux86-64 ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_machine:qemumips64 ?= "14d54ff925a0fff26c30f6fe7eda8b0310f8f512"
SRCREV_machine ?= "4f0f726a99be121940c4fb44634584d4027073b3"
SRCREV_meta ?= "d32229904a43cdac64d57932f997990519b6b773"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "131e2001572ba68b6728bcba91c58647168d237f"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.16/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.16;destsuffix=${KMETA};protocol=https"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.16.7"

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
