require linux-libc-headers.inc

SRC_URI_append_libc-musl = "\
    file://0001-libc-compat.h-fix-some-issues-arising-from-in6.h.patch \
    file://0002-libc-compat.h-prevent-redefinition-of-struct-ethhdr.patch \
    file://0003-remove-inclusion-of-sysinfo.h-in-kernel.h.patch \
    file://0001-libc-compat.h-musl-_does_-define-IFF_LOWER_UP-DORMAN.patch \
   "

SRC_URI[md5sum] = "4e8bb562f8fd33d5ef1feb0435ed2b02"
SRC_URI[sha256sum] = "4ab46d1b5a0f8ef83b80760f89ae4f5c88431b19b3cf79ffa0c66d6b33e45772"
