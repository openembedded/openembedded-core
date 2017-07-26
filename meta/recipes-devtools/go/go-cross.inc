inherit cross

DEPENDS += "gcc-cross-${TARGET_ARCH}"

PN = "go-cross-${TARGET_ARCH}"

# Ignore how TARGET_ARCH is computed.
TARGET_ARCH[vardepvalue] = "${TARGET_ARCH}"
# Ignore target os libc for cross compiler
#TARGET_OS[vardepexclude] = "LIBCEXTENSION"
FILESEXTRAPATHS =. "${FILE_DIRNAME}/go-cross:"

GOROOT_FINAL = "${libdir}/go"
export GOROOT_FINAL

