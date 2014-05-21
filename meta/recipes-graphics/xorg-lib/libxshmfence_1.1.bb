SUMMARY = "Shared memory 'SyncFence' synchronization primitive"

DESCRIPTION = "This library offers a CPU-based synchronization primitive compatible \
with the X SyncFence objects that can be shared between processes \
using file descriptor passing."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=47e508ca280fde97906eacb77892c3ac"

DEPENDS += "virtual/libx11"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "2dd10448c1166e71a176206a8dfabe6d"
SRC_URI[sha256sum] = "dbc2db2925ca9f216fd1e9c63d0974db9f4d49aaf5877ffb606d2d8d7e58cebe"
