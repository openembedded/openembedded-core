KBRANCH ?= "v5.13/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.13/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.13/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.13/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.13/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.13/standard/base"
KBRANCH:qemuriscv32  ?= "v5.13/standard/base"
KBRANCH:qemux86  ?= "v5.13/standard/base"
KBRANCH:qemux86-64 ?= "v5.13/standard/base"
KBRANCH:qemumips64 ?= "v5.13/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "1ba4dc7805cc92639acabfb0151f43c0b076c982"
SRCREV_machine:qemuarm64 ?= "370a8e98fbc5ec6da6630edd765a900baf61d0d9"
SRCREV_machine:qemumips ?= "76ac48cc6e9f3320d7358e979389584bfc513bd1"
SRCREV_machine:qemuppc ?= "8fc360f43d203e3577416b4ca48d8dfe7f1f85b8"
SRCREV_machine:qemuriscv64 ?= "4f742215802dad4a6457e2ef2cad96ef037d666c"
SRCREV_machine:qemuriscv32 ?= "4f742215802dad4a6457e2ef2cad96ef037d666c"
SRCREV_machine:qemux86 ?= "4f742215802dad4a6457e2ef2cad96ef037d666c"
SRCREV_machine:qemux86-64 ?= "4f742215802dad4a6457e2ef2cad96ef037d666c"
SRCREV_machine:qemumips64 ?= "9e06c12d9b1294cd17c24da363ec93e66d5ed5a6"
SRCREV_machine ?= "4f742215802dad4a6457e2ef2cad96ef037d666c"
SRCREV_meta ?= "c38435a3cacf424fa686ecac9a95ef8349b83bb3"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
DEFAULT_PREFERENCE:class-devupstream = "-1"
SRCREV_machine:class-devupstream ?= "7aeadb5bb82ad21ffbcd54c81d77727b7a05e6c1"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v5.13/base"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13.13"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
