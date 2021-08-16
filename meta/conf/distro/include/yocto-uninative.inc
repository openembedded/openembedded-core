#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_MAXGLIBCVERSION = "2.34"

UNINATIVE_URL ?= "http://downloads.yoctoproject.org/releases/uninative/3.3/"
UNINATIVE_CHECKSUM[aarch64] ?= "372d31264ea7ab8e08e0a9662f003b53e99b3813cc2d9f9a4cc5c2949a1de00b"
UNINATIVE_CHECKSUM[i686] ?= "36436167eba8a5957a0bf9a32402dd1be8b69528c1ff25e711e6895b583b2b42"
UNINATIVE_CHECKSUM[x86_64] ?= "92b5e465f74d7e195e0b60fe4146f0f1475fff87ab2649bf2d57a1526ef58aec"
