#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL ?= "http://downloads.yoctoproject.org/releases/uninative/1.6/"
UNINATIVE_CHECKSUM[i686] ?= "d9daf66785834f888b6f50a1d37a8120815f8380e0e267d311e1dfaf4bb5404e"
UNINATIVE_CHECKSUM[x86_64] ?= "2b4fffa308d9f19e0742a1a404ff42495fb50c165e5ca0458cedca157372691a"
