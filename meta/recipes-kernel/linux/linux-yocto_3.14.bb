KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "6166316d47b859aa38bfecc61f4808828af03937"
SRCREV_machine_qemumips ?= "4ececcc09c6550a0896728163907e729d817c2fd"
SRCREV_machine_qemuppc ?= "1cc5b09f8bb7f40b289d149d370c62dcc8109501"
SRCREV_machine_qemux86 ?= "38cd560d5022ed2dbd1ab0dca9642e47c98a0aa1"
SRCREV_machine_qemux86-64 ?= "02120556b0ebc20c30374ccf211e8e4ceac2bb1c"
SRCREV_machine_qemumips64 ?= "737272b1dfd361d9ea19812a9717e2798e3c4576"
SRCREV_machine ?= "02120556b0ebc20c30374ccf211e8e4ceac2bb1c"
SRCREV_meta ?= "a227f20eff056e511d504b2e490f3774ab260d6f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.24"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
