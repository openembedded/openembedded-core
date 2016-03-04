#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL = "http://downloads.yoctoproject.org/releases/uninative/0.95/"
UNINATIVE_CHECKSUM[i686] ?= "dd09aebeda3eac4e9140cdc327f28ed1"
UNINATIVE_CHECKSUM[x86_64] ?= "4b6fb2106c2f18b8f9c8ac67e1cf9e57"
