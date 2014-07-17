require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "e3cbee86dcbc6c9b23a7cf69fe579da77c3836d1"
SRCREV_machine_qemumips ?= "431de4758042fab2d62269574bb4ec3a783b43a0"
SRCREV_machine_qemuppc ?= "1fc7009c9c8de594d75090b188c11a6ddd0d369e"
SRCREV_machine_qemux86 ?= "116dacb5cebba538bc70e8056ebfa81d7ca6c061"
SRCREV_machine_qemux86-64 ?= "686b9ddf58ea6b533be70fe9f4a6407557b263d2"
SRCREV_machine_qemumips64 ?= "966c54ceb8cb797eafe987f9a16d306735057b42"
SRCREV_machine ?= "686b9ddf58ea6b533be70fe9f4a6407557b263d2"
SRCREV_meta ?= "b2af4e3528e65583c98f3a08c6edb0cad7a120b0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.5"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
