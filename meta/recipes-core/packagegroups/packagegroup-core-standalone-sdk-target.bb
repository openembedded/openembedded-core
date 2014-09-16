SUMMARY = "Target packages for the standalone SDK"
PR = "r8"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} = "\
    libgcc \
    libgcc-dev \
    libgcov-dev \
    libstdc++ \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    qemuwrapper-cross \
    "
