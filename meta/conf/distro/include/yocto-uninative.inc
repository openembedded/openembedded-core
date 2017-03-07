#
# This include points at uninative tarballs generated and maintained
# by the Yocto Project. These file can be included by distros if they
# want to use/maintain a unified sstate feed for native/cross artefacts
# rather than the feeds having native/cross artefacts which are specific
# to the distro running on the build machine.
#

UNINATIVE_URL ?= "http://downloads.yoctoproject.org/releases/uninative/1.5/"
UNINATIVE_CHECKSUM[i686] ?= "4cda652ba90f98e4c34e4c5a7e9f6364b51d3538228a4ffc24d0168fdf331369"
UNINATIVE_CHECKSUM[x86_64] ?= "22f5a061d077379f5728197bfcc239aa087332293038df48535c7badcb6939fd"
