require linux-libc-headers.inc

SRC_URI += "file://0001-ptrace.h-remove-ptrace_peeksiginfo_args.patch"
SRC_URI += "file://scripts-Makefile.headersinst-install-headers-from-sc.patch"

SRC_URI[md5sum] = "72d0a9b3e60cd86fabcd3f24b1708944"
SRC_URI[sha256sum] = "46c9e55e1fddf40813b8d697d5645037a8e2af5c1a8dff52b3fe82b5021582b8"

