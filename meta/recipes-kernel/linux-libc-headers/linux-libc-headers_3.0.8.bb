require linux-libc-headers.inc

PR = "r1"

SRC_URI += " file://connector-msg-size-fix.patch"
SRC_URI[md5sum] = "eac4d398a0ecd98214487cd47a228998"
SRC_URI[sha256sum] = "4ed16da319848f681f711dbda2ac2cf1b306a280ec22f90bae190cf23b533add"
