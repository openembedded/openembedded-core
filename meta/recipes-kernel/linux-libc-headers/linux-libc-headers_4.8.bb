require linux-libc-headers.inc

SRC_URI_forcevariable = "${KERNELORG_MIRROR}/linux/kernel/v${HEADER_FETCH_VER}/testing/linux-4.8-rc4.tar.${KORG_ARCHIVE_COMPRESSION}"
PV = "4.8-rc4"

SRC_URI_append_libc-musl = "\
    file://0001-libc-compat.h-fix-some-issues-arising-from-in6.h.patch \
    file://0002-libc-compat.h-prevent-redefinition-of-struct-ethhdr.patch \
    file://0003-remove-inclusion-of-sysinfo.h-in-kernel.h.patch \
   "

SRC_URI[md5sum] = "269067213995c7742730ce53b8140c90"
SRC_URI[sha256sum] = "8f07a0d76a7d1a30490582ba9642b7f28370102bc4acc4bb4b2586c8eabf4447"
