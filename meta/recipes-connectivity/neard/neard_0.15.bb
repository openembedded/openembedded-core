require neard.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/network/nfc/${BP}.tar.xz \
           file://neard.in \
           file://Makefile.am-fix-parallel-issue.patch \
           file://Makefile.am-do-not-ship-version.h.patch \
          "
SRC_URI[md5sum] = "b746ce62eeef88e8de90765e00a75a1c"
SRC_URI[sha256sum] = "651f6513d32cdaf8a426255d03aff38a6620a89b0567ec2b36606c6330a93353"

