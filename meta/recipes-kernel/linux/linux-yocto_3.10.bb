require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "f1714b2190a2a66caa9b775c7c9e80e731620b7d"
SRCREV_machine_qemumips  ?= "0ff63d9064c97748eb99ecfe83eaca2288431502"
SRCREV_machine_qemuppc ?= "fe8f8e061e437fd12187e002123b019eb4876405"
SRCREV_machine_qemux86 ?= "05de57c692983035c9ec2cdf53234f73d2b15f23"
SRCREV_machine_qemux86-64 ?= "05de57c692983035c9ec2cdf53234f73d2b15f23"
SRCREV_machine_qemumips64 ?= "e4fb43c587b9ee44b1032d234594646124e29cb2"
SRCREV_machine ?= "05de57c692983035c9ec2cdf53234f73d2b15f23"
SRCREV_meta ?= "43dd30e1955545a264fda63b4f66bb8f5cd875f9"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
