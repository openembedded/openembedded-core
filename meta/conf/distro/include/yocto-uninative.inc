#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL = "http://downloads.yoctoproject.org/releases/uninative/1.1/"
UNINATIVE_CHECKSUM[i686] ?= "1973aa8d07ba4fdae197ea3ffba1274c57ff7923679c364ec8e2b66c73483234"
UNINATIVE_CHECKSUM[x86_64] ?= "84e89ed675156d13a27fe56e8433f5fa9662802349bb0bbe9e6eba577fc140d3"
