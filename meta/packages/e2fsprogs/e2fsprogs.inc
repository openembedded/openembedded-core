DESCRIPTION = "EXT2 Filesystem Utilities"
HOMEPAGE = "http://e2fsprogs.sf.net"
LICENSE = "GPL"
SECTION = "base"
DEPENDS = "gettext"

SRC_URI = "${SOURCEFORGE_MIRROR}/e2fsprogs/e2fsprogs-${PV}.tar.gz"

inherit autotools

EXTRA_OECONF = " --enable-dynamic-e2fsck"

e2miscbins = "mke2fs badblocks tune2fs dumpe2fs blkid logsave e2image fsck \
              e2undo chattr lsattr uuidgen mklost+found filefrag uuidd e2initrd_helper"


