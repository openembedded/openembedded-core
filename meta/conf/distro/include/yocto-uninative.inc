#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL = "http://downloads.yoctoproject.org/releases/uninative/1.3/"
UNINATIVE_CHECKSUM[i686] ?= "ace5a6a0a91d5848de7bc8128379c2a987811454b6cb321ea9003ce91e6652d4"
UNINATIVE_CHECKSUM[x86_64] ?= "8e8b3db668fcc5bfdfcd268c0999ba2f4e85148a9a147f856d7b684f802c9797"

