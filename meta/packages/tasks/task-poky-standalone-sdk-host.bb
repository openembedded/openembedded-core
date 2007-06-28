DESCRIPTION = "Host packages for the standalone SDK (external toolchain)"
PR = "r0"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

inherit sdk

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    binutils-cross-sdk \
    gcc-cross-sdk \
    g++ \
    cpp \
    libgcc \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    gdb-cross \
    "