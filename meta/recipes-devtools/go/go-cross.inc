inherit cross

DEPENDS += "gcc-cross-${TARGET_ARCH}"

PN = "go-cross-${TARGET_ARCH}"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/go-cross:"

GOROOT_FINAL ="${libdir}/go"
export GOROOT_FINAL

# Incompatible with musl, at least for now
COMPATIBLE_HOST_libc-musl_class-target = "null"

