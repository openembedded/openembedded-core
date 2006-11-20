#@TYPE: Distribution
#@NAME: OpenZaurus
#@DESCRIPTION: Distribution configuration for OpenZaurus (http://www.openzaurus.org)

DISTRO = "openzaurus"
DISTRO_NAME = "OpenZaurus"

INHERIT += " package_ipk debian multimachine"
# For some reason, this doesn't work
# TARGET_OS ?= "linux"
TARGET_OS = "linux"
TARGET_FPU = "soft"

BOOTSTRAP_EXTRA_RDEPENDS += "openzaurus-version"

PARALLEL_INSTALL_MODULES = "1"

DISTRO_CHECK := "${@bb.data.getVar("DISTRO_VERSION",d,1) or bb.fatal('Remove this line or set a dummy DISTRO_VERSION if you really want to build an unversioned distro')}"

# Set minimal version of BitBake needed
BB_MIN_VERSION = "1.4.4"
INHERIT += "sanity"

# We want images supporting the following features (for task-base)
DISTRO_FEATURES = "nfs smbfs ipsec wifi ppp alsa bluetooth ext2 irda pcmcia usbgadget usbhost"
