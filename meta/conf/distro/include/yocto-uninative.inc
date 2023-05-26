#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_MAXGLIBCVERSION = "2.37"
UNINATIVE_VERSION = "3.10"

UNINATIVE_URL ?= "http://downloads.yoctoproject.org/releases/uninative/${UNINATIVE_VERSION}/"
UNINATIVE_CHECKSUM[aarch64] ?= "b11f7dff0ff5e3c971f22e89a105efb0f8d2006fd71d1ea3e5da771e416b0e01"
UNINATIVE_CHECKSUM[i686] ?= "ad7daa7fca7693ca07eea5279f830333530171d357759b380954a25254edd8dd"
UNINATIVE_CHECKSUM[x86_64] ?= "ce8ff7e3247da312fbf84008f415bc1b4d113ac9fe4ec91581e4a20cd8b475d3"
