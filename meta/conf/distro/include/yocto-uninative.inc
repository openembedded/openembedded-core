#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL = "http://downloads.yoctoproject.org/releases/uninative/1.2/"
UNINATIVE_CHECKSUM[i686] ?= "8b2f16d16cb037dd9905431875f5c355367dded84b18c3b98ca0e639b96fb8d1"
UNINATIVE_CHECKSUM[x86_64] ?= "2919dd81e4cafcc0fe73736363c6232dc8bfc016a8c765481d98ae4de8193d24"
