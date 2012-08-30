DESCRIPTION = "Target packages for the standalone SDK"
PR = "r7"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} = "\
    libgcc \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    "
