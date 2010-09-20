DESCRIPTION = "Target packages for the standalone SDK"
PR = "r6"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    libgcc \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    "
