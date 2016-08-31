#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL = "http://downloads.yoctoproject.org/releases/uninative/1.0.1/"
UNINATIVE_CHECKSUM[i686] ?= "de51bc9162b07694d3462352ab25f636a6b50235438c1b09440d7569e009123b"
UNINATIVE_CHECKSUM[x86_64] ?= "acf1e44a0ac2e855e81da6426197d36358bf7b4e88e552ef933128498c8910f8"
