#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_MAXGLIBCVERSION = "2.27"

UNINATIVE_URL ?= "http://downloads.yoctoproject.org/releases/uninative/2.1/"
UNINATIVE_CHECKSUM[i686] ?= "a90db17d8845ece764aaac19348cd78504c0e554cb998da39ebad03aeef76185"
UNINATIVE_CHECKSUM[x86_64] ?= "d43555df8d5a056ef5b20c7d8e0287207974e09df1e969a3e19302c8b4ea6b4b"
